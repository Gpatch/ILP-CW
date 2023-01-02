package uk.ac.ed.inf;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Represents the central area from the given points, which is a singleton class.
 */
public class CentralArea {
    private LngLat[] centralPoints;
    private static final CentralArea instance = new CentralArea();

    /**
     * Initialises the points for the central area which are retrieved from the centralArea.json file
     */
    private void initPoints(){
        String json = "";
        try {
            json = new String(Files.readAllBytes(Paths.get("staticfiles/centralArea.json")));
        }catch (IOException e){
            System.err.println("Error while reading from downloaded centralArea.json!");
            System.exit(ErrorCodes.READ_WRITE_FILE.code);
        }
        this.centralPoints = JSONObjectConverter.convertFromJSON(json, LngLat[].class);
    }

    /**
     * Private constructor of the class, so that only single instance of the class can be created.
     */
    private CentralArea(){
        initPoints();
        System.out.println("Singleton class CentralArea has been instantiated!");
    }

    public static CentralArea getInstance() {
        return instance;
    }
    public LngLat[] getCentralPoints(){return centralPoints;}
}