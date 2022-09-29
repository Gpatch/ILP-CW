package uk.ac.ed.inf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CentralArea {
    private static CentralArea instance;

    static {
        try {
            instance = new CentralArea();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    List<LngLat> centralPoints;

    private void getFromURL(){
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

        try (BufferedInputStream in = new BufferedInputStream(finalURL.openStream());
            FileOutputStream fileOutputSteam = new FileOutputStream(filenameToLoad, false)){

            byte[] dataBuffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1){
                fileOutputSteam.write(dataBuffer, 0, bytesRead);
            }
            System.out.println("File was written: " + filenameToLoad);
        } catch (IOException e){
            System.err.format("Error loading file: %s from %s -> %s", filenameToLoad, finalURL, e);
        }
    }

    private void mapToObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper().enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        File json = new File("centralArea");
        centralPoints = Arrays.asList(mapper.readValue(json, LngLat[].class));
    }

    private CentralArea() throws IOException {
        this.getFromURL();
        this.mapToObject();
        System.out.println("Singleton class CentralArea has been instantiated!");
    }

    public static CentralArea getInstance() {return instance;}
}
