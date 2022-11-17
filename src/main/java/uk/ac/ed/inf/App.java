package uk.ac.ed.inf;

import java.io.IOException;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) throws IOException {
        String baseAddress = "";
        String date = "";
        try{
            baseAddress = args[0];
        }catch (ArrayIndexOutOfBoundsException e){
            System.err.println("URL not specified");
            System.exit(2);
        }

        try {
            date = args[1];
        }catch (ArrayIndexOutOfBoundsException e){
            System.err.println("Date not specified! Retrieving all orders.");
        }
        Drone drone = new Drone(baseAddress, date);
        //drone.AStar(new LngLat(-3.18942006469274, 55.94586860199561));
        ArrayList<Integer> moves = drone.calculateCosts();
      //  System.out.println(moves.stream().mapToInt(Integer::intValue).sum());
        System.out.println(moves);
        System.out.println(drone.getAllPaths());
        System.out.println(drone.generatePathJSON(drone.pathsToRestaurants.get(drone.restaurants[3])));
        // System.out.println(Arrays.toString(CentralArea.getInstance().centralPoints));
       // System.out.println(drone.AStar(drone.restaurants[1]));

        //System.out.println(drone.location.distanceTo((new LngLat(-3.202541470527649, 55.943284737579376))));
    }
}
