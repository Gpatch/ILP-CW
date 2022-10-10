package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;


public class CentralArea {
    List<LngLat> centralPoints;
    private static CentralArea instance;

    static {
        try {
            instance = new CentralArea();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void getFromURL() throws IOException {
        URL finalURL = null;
        String baseURL = "https://ilp-rest.azurewebsites.net/";
        String filenameToLoad = "centralArea";

        if (!baseURL.endsWith("/")) {
            baseURL += "/";
        }

        try {
            finalURL = new URL(baseURL + filenameToLoad);
        }catch (MalformedURLException e){
            System.err.println("URL is invalid: " + baseURL + filenameToLoad);
            System.exit(2);
        }

        ObjectMapper mapper = new ObjectMapper().enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        centralPoints = Arrays.asList(mapper.readValue(finalURL, LngLat[].class));
    }


    private CentralArea() throws IOException {
        this.getFromURL();
        System.out.println("Singleton class CentralArea has been instantiated!");
    }

    public static CentralArea getInstance() {return instance;}
}
