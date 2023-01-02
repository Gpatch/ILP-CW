package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a restaurant with its name, location and menu,
 * as well as includes a factory to access all restaurants participating.
 */
public class Restaurant {
    private final String name;
    private final LngLat location;
    private final Menu[] menu;

    /**
     * A public constructor for a restaurant used for deserializing from JSON requested form the REST API.
     * Since LngLat does not exist in the JSON as a field, longitude and latitude will be passed and used
     * to create the LngLat object which will represent the location of the restaurant. The rest of the fields
     * are initialised automatically during the deserialization since they exist in the JSON.
     * @param longitude will be passed into a constructor of LngLat object.
     * @param latitude will be passed into a constructor of LngLat object.
     */
    @JsonCreator
    public Restaurant(@JsonProperty("name") String name, @JsonProperty("longitude") double longitude,@JsonProperty("latitude") double latitude,
                      @JsonProperty("menu") Menu[] menu){
        this.name = name;
        location = new LngLat(longitude, latitude);
        this.menu = menu;
    }

    /**
     * Gets all the items in the menu of the restaurant.
     * @return an array of menu objects.
     */
    public Menu[] getMenu() { return menu; }

    /**
     * Request restaurants data from the server
     * @param serverBaseAddress of the rest server from which data is retrieved
     * @return array of restaurant objects
     */
    public static Restaurant[] requestRestaurants(String serverBaseAddress){
        Request request = new Request(serverBaseAddress, Endpoint.RESTAURANTS.endpoint);
        String json = request.getJSONResponse();
        Restaurant[] restaurants = JSONObjectConverter.convertFromJSON(json, Restaurant[].class);
        return restaurants;
    }

    public String getName(){return name;}
    public LngLat getLocation(){return location;}

}
