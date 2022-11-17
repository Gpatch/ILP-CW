package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Represents the central area from the given points, which is a singleton class.
 */
public class CentralArea {
    public LngLat[] centralPoints;
    private static final CentralArea instance = new CentralArea();

    /**
     * Connects the REST server with "centralArea" endpoint, retrieves and deserializes the central points from JSON.
     */
    public void initPoints(String baseAddress, String endpoint){
        Request request = new Request(baseAddress, endpoint);
        centralPoints = request.getCentralArea();
    }

    /**
     * Private constructor of the class, so that only single instance of the class can be created.
     */
    private CentralArea(){
        System.out.println("Singleton class CentralArea has been instantiated!");
    }

    /**
     * @return the instance of the singleton class.
     */
    public static CentralArea getInstance() {
        return instance;
    }
}