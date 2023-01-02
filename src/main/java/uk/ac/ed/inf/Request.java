package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Represents a request created to connect to the server
 */
public class Request {
    private final URL fullAddress;

    /**
     * A public constructor to initialise the URL to which connection to be established
     * @param baseAddress of the rest server
     * @param endPoint from which data to be accessed
     */
    public Request(String baseAddress, String endPoint){
        URL finalURL = null;
        try {
            finalURL = new URL(baseAddress + endPoint);
        }catch (MalformedURLException e){
            System.err.println("URL is invalid: " + baseAddress + endPoint);
            System.exit(ErrorCodes.INVALID_URL.code);
        }
        this.fullAddress = finalURL;
    }

    /**
     * Retrieves a json response from the requested server endpoint
     * @return a json string from the endpoint
     */
    public String getJSONResponse() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = null;
        try {
            json = mapper.readTree(fullAddress);
        }catch (IOException e){
            System.err.println("Error getting JSON response!");
            System.exit(ErrorCodes.JSON_PROCESS.code);
        }
        return json.toString();
    }
}
