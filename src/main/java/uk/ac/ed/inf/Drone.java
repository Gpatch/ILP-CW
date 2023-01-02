package uk.ac.ed.inf;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.javatuples.Pair;
import org.javatuples.Triplet;

/**
 * Represents a drone which handles generating paths and delivering orders
 */
public class Drone {
    private int battery;
    private final LngLat APPLETON_TOWER_LOCATION;
    private final String DATE;
    private final Order[] orders;
    private ArrayList<Order> deliveredOrders;
    private Map<Restaurant, ArrayList<Triplet<LngLat, Double, LngLat>>> pathsToRestaurants;

    private long ticksSinceStartOfCalculation;
    private long clockInNanos;

    /**
     * A public constructor for the drone used to initialise the values and request and store the orders from the
     * rest server
     * @param serverBaseAddress of the rest server from which data is retrieved
     * @param date for which orders to be retrieved from the server
     */
    public Drone(String serverBaseAddress, String date) {
        battery = 2000;
        APPLETON_TOWER_LOCATION = new LngLat(-3.186874, 55.944494);
        DATE = date;
        orders = Order.initOrders(serverBaseAddress, date, Restaurant.requestRestaurants(serverBaseAddress));
        deliveredOrders = new ArrayList<>();
        pathsToRestaurants = new HashMap<>();

        ticksSinceStartOfCalculation = 0;
        clockInNanos = System.nanoTime();
    }

    private long getNewTicks(){
        long newTicks;
        long currentClock = System.nanoTime();
        long diff = currentClock - clockInNanos;
        clockInNanos = currentClock;

        newTicks = ticksSinceStartOfCalculation + diff;
        return newTicks;
    }

    /**
     * Generates a total path from the Appleton Tower to the restaurant and back.
     * The path from the AT to restaurant is generated with A* algorithm,
     * whereas the path from the restaurant ot the AT is calculated be reversing the
     * previous path and adding it to the total path.
     * For reference: https://en.wikipedia.org/wiki/A*_search_algorithm
     * @param restaurant for which path to be calculated
     * @return a list of triplets which represents a total path for the specific restaurant.
     * Triplet represents [new_location, angle, previous_location]
     */
    public ArrayList<Triplet<LngLat, Double, LngLat>> AStar(Restaurant restaurant){
        CentralArea centralArea = CentralArea.getInstance();
        NoFlyZone[] noFlyZones = NoFlyZone.requestNoFlyZones();

        LngLat goalLocation = null;

        PriorityQueue<Pair<Pair<LngLat, Double>, Double>> frontier = new PriorityQueue<>(Comparator.comparing(Pair::getValue1));
        frontier.add(new Pair<>(new Pair<>(APPLETON_TOWER_LOCATION, Compass.NULL.angle), 0.0));

        Map<LngLat, Pair<LngLat, Double>> cameFrom = new HashMap<>();
        cameFrom.put(APPLETON_TOWER_LOCATION, null);

        Map<LngLat, Double> costSoFar = new HashMap<>();
        costSoFar.put(APPLETON_TOWER_LOCATION, 0.0);


        while(!frontier.isEmpty()){
            int centralAreaIntersections = 0;
            boolean inCentralArea = true;
            Pair<Pair<LngLat, Double>, Double> currentLocValPair = frontier.remove();
            LngLat currentLoc = currentLocValPair.getValue0().getValue0();

            if(currentLoc.closeTo(restaurant.getLocation())){
                goalLocation = currentLoc;
                break;
            }

            for(Compass c : Arrays.stream(Compass.values()).toList().subList(0, Compass.values().length-1)){
                LngLat nextLoc = currentLoc.nextPosition(c);
                if(isInNoFlyZone(noFlyZones, nextLoc)){continue;}

                //Making sure the drone does not intersect with the central area edge more than once while travelling
                // to the restaurant
                if(inCentralArea != nextLoc.inArea(centralArea.getCentralPoints())){
                    inCentralArea = !inCentralArea;
                    centralAreaIntersections +=1;
                    if(centralAreaIntersections > 1){
                        centralAreaIntersections -=1;
                        continue;
                    }
                }

                double nextLocCost = currentLoc.distanceTo(nextLoc);
                double totalNewCost = costSoFar.get(currentLoc) + nextLocCost;
                if(!costSoFar.containsKey(nextLoc) || totalNewCost < costSoFar.get(nextLoc)){
                    costSoFar.put(nextLoc, totalNewCost);
                    double priority = totalNewCost + nextLoc.distanceTo(restaurant.getLocation());
                    frontier.add(new Pair<>(new Pair<>(nextLoc, c.angle), priority));
                    cameFrom.put(nextLoc, new Pair<>(currentLoc, c.angle));
                }
            }
        }
        return reconstructPath(cameFrom, goalLocation);
    }


    private boolean isInNoFlyZone(NoFlyZone[] noFlyZones, LngLat location){
        boolean inside = false;
        for(NoFlyZone zone : noFlyZones){
            if(location.inArea(zone.getCoordinates())){
                inside = true;
                break;
            }
        }
        return inside;
    }

    // Used in A* algorithm to reconstruct the  path from AT to restaurant by backtracking
    private ArrayList<Triplet<LngLat, Double, LngLat>> reconstructPath(Map<LngLat, Pair<LngLat, Double>> cameFrom, LngLat goalLocation){
        ArrayList<Triplet<LngLat, Double, LngLat>> path = new ArrayList<>();
        Pair<LngLat, Double> loc = cameFrom.get(goalLocation);
        path.add(new Triplet<>(goalLocation, Compass.NULL.angle, goalLocation));
        path.add(new Triplet<>(goalLocation, loc.getValue1(), loc.getValue0()));

        while(loc.getValue0() != APPLETON_TOWER_LOCATION){
            Pair<LngLat, Double> nextLoc = cameFrom.get(loc.getValue0());
            path.add(new Triplet<>(loc.getValue0(), nextLoc.getValue1(), nextLoc.getValue0()));
            loc = cameFrom.get(loc.getValue0());
        }
        Collections.reverse(path);
        return path;
    }

    // Runs A* algorithm for every valid order for which restaurant path has not been calculated yet.
    // And stores the calculated paths for every unique restaurant of valid order.
    private void buildPathsToRestaurants(){
        for(Order order : orders){
            if(!pathsToRestaurants.containsKey(order.getRestaurant())) {
                if(order.getOutcome() == OrderOutcome.ValidButNotDelivered) {
                    pathsToRestaurants.put(order.getRestaurant(), AStar(order.getRestaurant()));
                }
            }
        }
    }

    //Get total path for a single order. Meaning from AT to restaurant and back.
    private ArrayList<Triplet<LngLat, Double, LngLat>> getTotalPathForOrder(Order order){
        ArrayList<Triplet<LngLat, Double, LngLat>> path = new ArrayList<>();

        if(pathsToRestaurants.containsKey(order.getRestaurant())) {
            ArrayList<Triplet<LngLat, Double, LngLat>> pathToRestaurant = pathsToRestaurants.get(order.getRestaurant());
            ArrayList<Triplet<LngLat, Double, LngLat>> pathFromRestaurant = new ArrayList<>();

            for (int i = pathToRestaurant.size() - 2; i >= 0; i--) {
                LngLat from = pathToRestaurant.get(i).getValue0();
                LngLat to = pathToRestaurant.get(i).getValue2();
                Double angle = (pathToRestaurant.get(i).getValue1() + 180) % 360;
                pathFromRestaurant.add(new Triplet<>(to, angle, from));
            }
            pathFromRestaurant.add(new Triplet<>(APPLETON_TOWER_LOCATION, Compass.NULL.angle, APPLETON_TOWER_LOCATION));
            path.addAll(pathToRestaurant);
            path.addAll(pathFromRestaurant);
        }
        return path;
    }

    // Returns the total path for delivered orders for the whole day
    public ArrayList<LngLat> getTotalPath(){
        ArrayList<LngLat> path = new ArrayList<>();
        for(Order order : deliveredOrders){
            List<LngLat> orderPath = getTotalPathForOrder(order).stream().map(Triplet::getValue2).collect(Collectors.toList());
            path.addAll(orderPath);
        }
        return path;
    }


    /**
     * Method which delivers the orders. First builds all paths for restaurants of valid orders.
     * Valid orders are sorted in the priority queue based on the least number
     * of moves required to deliver them. Orders in ascending order of moves required are being delivered until the drone
     * does not have enough battery to deliver the next order. Creates and writes the information about the drone path into
     * the drone-date.json file
     */
    public void deliverOrders(){
        PriorityQueue<Pair<Order, Integer>> orderMovementCost = new PriorityQueue<>(Comparator.comparing(Pair::getValue1));
        buildPathsToRestaurants();

        //Populating priority queue with valid orders
        for(Order order : orders){
            if(order.getOutcome() == OrderOutcome.ValidButNotDelivered) {
                Pair<Order, Integer> currentOrderValuePair = new Pair<>(order, getTotalPathForOrder(order).size());
                orderMovementCost.add(currentOrderValuePair);
            }
        }

        //Setting up for writing into json file
        JsonFactory factory = new JsonFactory();
        StringWriter jsonObjectWriter = new StringWriter();
        JsonGenerator generator;

        try{
            generator = factory.createGenerator(jsonObjectWriter);
        } catch (IOException e) {
            System.err.println("Error while initialising JSON generator!");
            System.exit(ErrorCodes.JSON_PROCESS.code);
            return;
        }

        try{
            generator.useDefaultPrettyPrinter();
            generator.writeStartArray();

            //Extracting orders from priority queue until out of battery
            while(battery > 0 && orderMovementCost.size() > 0){
                Pair<Order, Integer> currentOrderMovement = orderMovementCost.remove();
                Order order = currentOrderMovement.getValue0();
                int steps = currentOrderMovement.getValue1();

                if(battery - steps < 0){break;}

                order.setOrderOutcome(OrderOutcome.Delivered);
                battery -= steps;
                deliveredOrders.add(order);

                ArrayList<Triplet<LngLat, Double, LngLat>> path = getTotalPathForOrder(order);

                //Add json entry for every order step
                for(Triplet<LngLat, Double, LngLat> loc : path){
                    ticksSinceStartOfCalculation = getNewTicks();
                    JSONGenerator.addFlightpathObjectToJSON(generator, order, loc, ticksSinceStartOfCalculation);
                }
            }
            generator.writeEndArray();
            generator.close();

        } catch (IOException e) {
        System.err.println("Error while trying to write into drone-"+ DATE + ".json");
        System.exit(ErrorCodes.READ_WRITE_FILE.code);
        }

        String json = jsonObjectWriter.toString();
        JSONGenerator.generateFlightPathJSON(DATE, json);
    }


    public Order[] getOrders() {
        return orders;
    }
    public int getBattery(){return battery;}
}
