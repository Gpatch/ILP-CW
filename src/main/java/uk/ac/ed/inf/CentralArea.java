package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Represents the central area from the given points, which is a singleton class.
 */
public class CentralArea {
    LngLat[] centralPoints;
    private static final CentralArea instance = new CentralArea();

    /**
     * Connects the REST server with "centralArea" endpoint, retrieves and deserializes the central points from JSON.
     */
    private void getFromURL() {
        URL finalURL = null;
        String baseURL = "https://ilp-rest.azurewebsites.net/";
        String endPoint = "centralArea";

        if (!baseURL.endsWith("/")) {
            baseURL += "/";
        }

        try {
            finalURL = new URL(baseURL + endPoint);
        }catch (MalformedURLException e){
            System.err.println("URL is invalid: " + baseURL + endPoint);
            System.exit(2);
        }

        ObjectMapper mapper = new ObjectMapper().enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        try {
            centralPoints = mapper.readValue(finalURL, LngLat[].class);
        }
        catch(IOException e){
            System.err.println("Error while trying to central points data from the REST server!");
            centralPoints = new LngLat[0];
        }
    }

    /**
     * Private constructor of the class, so that only single instance of the class can be created.
     */
    private CentralArea(){
        this.getFromURL();
        System.out.println("Singleton class CentralArea has been instantiated!");
    }

    /**
     * @return the instance of the singleton class.
     */
    public static CentralArea getInstance() {return instance;}
}
