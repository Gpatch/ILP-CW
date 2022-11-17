package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;

public class Request {
    public URL address;
    public String endpoint;

    public Request(String baseAddress, String endPoint){
        URL finalURL = null;

        if (!baseAddress.endsWith("/")) {
            baseAddress += "/";
        }
        try {
            finalURL = new URL(baseAddress + endPoint);
        }catch (MalformedURLException e){
            System.err.println("URL is invalid: " + baseAddress + endPoint);
            System.exit(2);
        }
        this.address = finalURL;
        this.endpoint = endPoint;
    }


    public Order[] getOrders(){
        String ordersAddress = address.toString();
        Order[] orders;
        String date;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        ObjectMapper mapper = new ObjectMapper();

        try{
            date = endpoint.substring(7, endpoint.length());
        }catch (StringIndexOutOfBoundsException e){
            System.err.println("Invalid endpoint format!");
            return new Order[0];
        }

        if(!date.equals("")) {
            try {
                Date d = formatter.parse(date);
            } catch (ParseException e) {
                System.err.println("Invalid Date format: " + date);
                return new Order[0];
            }
        }

        try{
            orders = mapper.readValue(address, Order[].class);
        }catch (IOException e){
            System.err.println("Error while reading orders data from the REST server!");
            return new Order[0];
        }
        return orders;
    }

    /**
     * Serves as a factory method. Creates an array of restaurants from the REST API, which could be accessed statically.
     * @return an array of the restaurant objects.
     */
    public Restaurant[] getRestaurants(){
        Restaurant[] restaurants;
        ObjectMapper mapper = new ObjectMapper();
        try {
            restaurants = mapper.readValue(address, Restaurant[].class);
        }
        catch(IOException e) {
            System.err.println("Error while reading restaurants data from the REST server!");
            return new Restaurant[0];
        }
        return restaurants;
    }

    public LngLat[] getCentralArea(){
        LngLat[] centralArea;
        ObjectMapper mapper = new ObjectMapper();

        try {
            centralArea = mapper.readValue(address, LngLat[].class);
        }
        catch(IOException e){
            System.err.println("Error while trying to central points data from the REST server!");
            centralArea = new LngLat[0];
        }
        return centralArea;
    }

    public NoFlyZone[] getNoFlyZones(){
        NoFlyZone[] zones;
        ObjectMapper mapper = new ObjectMapper();

        try {
            zones = mapper.readValue(address, NoFlyZone[].class);
        }catch (IOException e){
            System.err.println("Error while trying to central points data from the REST server!");
            zones = new NoFlyZone[0];
        }
        return zones;
    }
}
