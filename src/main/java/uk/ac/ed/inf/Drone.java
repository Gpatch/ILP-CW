package uk.ac.ed.inf;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.javatuples.Pair;
public class Drone {
    public int battery;
    public LngLat location;
    public String date;
    public Order[] orders;
    public Restaurant[] restaurants;
    public CentralArea centralArea;
    public NoFlyZone[] noFlyZones;

    public Map<Restaurant, ArrayList<LngLat>> pathsToRestaurants = new HashMap<>();



    public Drone(String serverBaseAddress, String date) {
        battery = 2000;
        location = new LngLat(-3.186874, 55.944494); //Appleton Tower
        this.date = date;
        restaurants = new Request(serverBaseAddress, Endpoint.RESTAURANTS.endpoint).getRestaurants();
        orders = Order.initOrders(serverBaseAddress, date, restaurants);

        centralArea = CentralArea.getInstance();
        centralArea.initPoints(serverBaseAddress, Endpoint.CENTRAL_AREA.endpoint);

        noFlyZones = new Request(serverBaseAddress, Endpoint.NO_FLY_ZONES.endpoint).getNoFlyZones();

    }



    public ArrayList<LngLat> AStar(Restaurant restaurant){
        int centralAreaIntersections = 0;
        boolean inCentralArea = true;
        LngLat finished = null;
        ArrayList<LngLat> path = new ArrayList<>();

        PriorityQueue<Pair<LngLat, Double>> frontier = new PriorityQueue<>(Comparator.comparing(Pair::getValue1));
        frontier.add(new Pair<>(this.location, 0.0));

        Map<LngLat, LngLat> cameFrom = new HashMap<>();
        cameFrom.put(this.location, null);

        Map<LngLat, Double> costSoFar = new HashMap<>();
        costSoFar.put(this.location, 0.0);


        while(!frontier.isEmpty()){
            Pair<LngLat, Double> current = frontier.remove();
            System.out.println(current.getValue0().distanceTo(restaurant.location));
            if(current.getValue0().closeTo(restaurant.location)){
                finished = current.getValue0();
                path.add(finished);
                System.out.println("ARRIVED!");
                break;
            }

            for(Compass c : Arrays.stream(Compass.values()).toList().subList(0, Compass.values().length-1)){
                boolean inNoFlyZone = false;
                LngLat nextLoc = current.getValue0().nextPosition(c);

                for(NoFlyZone zone : noFlyZones){
                    if(nextLoc.inArea(zone.coordinates)){
                        inNoFlyZone = true;
                        break;
                    }
                }
                if(inNoFlyZone){continue;}


                if(inCentralArea != nextLoc.inArea(centralArea.centralPoints)){
                    inCentralArea = !inCentralArea;
                    centralAreaIntersections +=1;
                }

                if(centralAreaIntersections > 1){
                    centralAreaIntersections -=1;
                    continue;
                }

                double nextLocCost = current.getValue0().distanceTo(nextLoc);
                double totalNewCost = costSoFar.get(current.getValue0()) + nextLocCost;
                if(!costSoFar.containsKey(nextLoc) || totalNewCost < costSoFar.get(nextLoc)){
                    costSoFar.put(nextLoc, totalNewCost);
                    double priority = totalNewCost + nextLoc.distanceTo(restaurant.location);
                    frontier.add(new Pair<>(nextLoc, priority));
                    cameFrom.put(nextLoc, current.getValue0());
                }
            }
        }

        LngLat next = cameFrom.get(finished);
        while(next != this.location){
            path.add(cameFrom.get(next));
            next = cameFrom.get(next);
        }
        Collections.reverse(path);
        pathsToRestaurants.put(restaurant, path);
        return path;
    }

    public ArrayList<Integer> calculateCosts(){
        ArrayList<Integer> moves = new ArrayList<>();
        for(Order order : orders){
            if(pathsToRestaurants.containsKey(order.restaurant)){
                moves.add((pathsToRestaurants.get(order.restaurant).size() + 1)*2);
            }else {
                int count = (AStar(order.restaurant).size() + 1) * 2;
                moves.add(count);
            }
        }
        return moves;
    }

    public ArrayList<ArrayList<LngLat>> getAllPaths(){
        ArrayList<ArrayList<LngLat>> paths = new ArrayList<>();
        for(Order order : orders){
            if(pathsToRestaurants.containsKey(order.restaurant)){
                ArrayList<LngLat> currentPath = pathsToRestaurants.get(order.restaurant);
                paths.add(currentPath);
            }
        }
        return paths;
    }

    public String generatePathJSON(ArrayList<LngLat> path) throws IOException {
        JsonFactory factory = new JsonFactory();
        StringWriter jsonObjectWriter = new StringWriter();
        JsonGenerator generator = factory.createGenerator(jsonObjectWriter);

        generator.useDefaultPrettyPrinter();
        generator.writeStartObject();
        generator.writeFieldName("type");
        generator.writeString("Feature");
        generator.writeFieldName("properties");
        generator.writeStartObject();
        generator.writeEndObject();
        generator.writeFieldName("geometry");
        generator.writeStartObject();
        generator.writeFieldName("type");
        generator.writeString("LineString");
        generator.writeFieldName("coordinates");
        generator.writeStartArray();
        for(LngLat i : path){
            generator.writeArray(i.getCoordinates(), 0, 2);
        }
        generator.writeEndArray();
        generator.writeEndObject();
        generator.writeEndObject();
        generator.close();

        return jsonObjectWriter.toString();
    }


}
