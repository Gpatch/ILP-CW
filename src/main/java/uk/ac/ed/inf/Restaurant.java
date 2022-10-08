package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Restaurant {
    @JsonProperty("name")
    String name;
    LngLat location;
    @JsonProperty("menu")
    Menu[] menu;

    public Restaurant(@JsonProperty("longitude") double longitude,@JsonProperty("latitude") double latitude){
        location = new LngLat(longitude, latitude);
    }

    public Restaurant(){}

    public static Restaurant[] getRestaurantsFromRestServer(URL serverBaseAddress) throws IOException {
        Restaurant[] restaurants;
        ObjectMapper mapper = new ObjectMapper().enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        restaurants = mapper.readValue(serverBaseAddress, Restaurant[].class);
        return restaurants;
    }

    public Menu[] getMenu(){
        return menu;
    }
}
