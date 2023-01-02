package uk.ac.ed.inf;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * Main class of the program. Serves an an entry point where the passed arguments are processed.
 */
public class App {
    public static void main(String[] args) {
        String baseAddress = "";
        String date = "";

        if(args.length < 2 || args.length > 3){
            System.err.println("Incorrect number of arguments supplied!");
            System.exit(ErrorCodes.INVALID_ARGS.code);
        }

        //Checking if passed date is of yyyy-MM-dd format.
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
            formatter.parse(args[0]);
            date = args[0];
        }catch (DateTimeParseException e){
            System.err.println("First argument is not a valid date!");
            System.exit(ErrorCodes.INVALID_ARGS.code);
        }

        try{
            new URL(args[1]);
            baseAddress = args[1];
            if(!baseAddress.endsWith("/")){
                baseAddress += "/";
            }
        }catch (MalformedURLException e){
            System.err.println("Second argument is not a valid URL!");
            System.exit(ErrorCodes.INVALID_ARGS.code);
        }

        Download.download(baseAddress, Endpoint.CENTRAL_AREA.endpoint, "json");
        Download.download(baseAddress, Endpoint.NO_FLY_ZONES.endpoint, "json");
        executeDrone(baseAddress, date);
    }

    /**
     * Creates a drone and executes a process of finding all deliverable orders for the specified date,
     * generates paths to deliver those orders and outputs three files, flightpath-date.json, deliveries-date.json,
     * and drone-date.json.
     *
     * @param baseAddress of the rest server from which data is retrieved
     * @param date for which orders are to be delivered
     */
    public static void executeDrone(String baseAddress, String date){
        try {
            Files.createDirectories(Paths.get("resultfiles"));
        }catch (IOException e){
            System.err.println("Error while creating resultfiles folder!");
            System.exit(ErrorCodes.READ_WRITE_FILE.code);
        }
        Drone drone = new Drone(baseAddress, date);
        drone.deliverOrders();

        JSONGenerator.generateDeliveriesJSON(drone.getOrders(), date);
        JSONGenerator.generateGeoJSON(drone.getTotalPath(), date);

        countValid(drone.getOrders());
        countDelivered(drone.getOrders());
        System.out.println(2000-drone.getBattery());

    }

    //Count the number of delivered orders
    private static void countDelivered(Order[] orders){
        int count = 0;
        for(Order o : orders){
            if(o.getOutcome() == OrderOutcome.Delivered){
                count++;
            }
        }
        System.out.println("Delivered orders: " + count);
    }

    //Count the number of valid orders
    private static void countValid(Order[] orders){
        int count = 0;
        for(Order o : orders){
            if(o.getOutcome() == OrderOutcome.Delivered || o.getOutcome() == OrderOutcome.ValidButNotDelivered){
                count++;
            }
        }
        System.out.println("Delivered orders: " + count);
    }
}
