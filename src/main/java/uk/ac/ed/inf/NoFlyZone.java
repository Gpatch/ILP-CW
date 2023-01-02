package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Represents noflyzone with a set of coordinates to create a shape and name of the place
 */
public class NoFlyZone {
    private final String name;
    private final LngLat[] coordinates;

    /**
     * A public constructor for a noflyzone used for deserializing from JSON
     * @param name represents name of the place
     * @param location represents the edges of the noflyzone
     */
    @JsonCreator
    public NoFlyZone(@JsonProperty("name") String name, @JsonProperty("coordinates") List<Double[]> location){
        this.name = name;
        this.coordinates =  location.stream().map(l -> new LngLat(l[0], l[1])).toArray(LngLat[]::new);
    }

    /**
     * Retrieves a list of noflyzones from the centralArea.json file
     * @return an array of noflyzones
     */
    public static NoFlyZone[] requestNoFlyZones(){
        String json = "";
        try {
            json = new String(Files.readAllBytes(Paths.get("staticfiles/noflyzones.json")));
        }catch (IOException e){
            System.err.println("Error while reading from downloaded noflyzones.json!");
            System.exit(ErrorCodes.READ_WRITE_FILE.code);
        }
        return JSONObjectConverter.convertFromJSON(json, NoFlyZone[].class);
    }

    public LngLat[] getCoordinates() {return coordinates;}
}
