package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;

/**
 * Represents a restaurant with its name, location and menu,
 * as well as includes a factory to access all restaurants participating.
 */
public class Restaurant {
    @JsonProperty("name")
    String name;
    LngLat location;
    @JsonProperty("menu")
    Menu[] menu;

    /**
     * A public constructor for a restaurant used for deserializing from JSON requested form the REST API.
     * Since LngLat does not exist in the JSON as a field, longitude and latitude will be passed and used
     * to create the LngLat object which will represent the location of the restaurant. The rest of the fields
     * are initialised automatically during the deserialization since they exist in the JSON.
     * @param longitude will be passed into a constructor of LngLat object.
     * @param latitude will be passed into a constructor of LngLat object.
     */
    @JsonCreator
    public Restaurant(@JsonProperty("longitude") double longitude,@JsonProperty("latitude") double latitude){
        location = new LngLat(longitude, latitude);
    }

    /**
     * Serves as a factory method. Creates an array of restaurants from the REST API, which could be accessed statically.
     * @param serverBaseAddress is the address of the REST server.
     * @return an array of the restaurant objects.
     */
    public static Restaurant[] getRestaurantsFromRestServer(URL serverBaseAddress){
        Restaurant[] restaurants;
        ObjectMapper mapper = new ObjectMapper().enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        try {
            restaurants = mapper.readValue(serverBaseAddress, Restaurant[].class);
        }
        catch(IOException e) {
            System.err.println("Error while reading restaurants data from the REST server!");
            return new Restaurant[0];
        }
        return restaurants;
    }

    /**
     * Gets all the items in the menu of the restaurant.
     * @return an array of menu objects.
     */
    public Menu[] getMenu() { return menu; }
}
