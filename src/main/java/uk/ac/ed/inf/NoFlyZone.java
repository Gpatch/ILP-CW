package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

public class NoFlyZone {
    public String name;
    public LngLat[] coordinates;

    @JsonCreator
    public NoFlyZone(@JsonProperty("name") String name, @JsonProperty("coordinates") List<Double[]> location){
        this.name = name;
        this.coordinates =  location.stream().map(l -> new LngLat(l[0], l[1])).toArray(LngLat[]::new);
    }
}
