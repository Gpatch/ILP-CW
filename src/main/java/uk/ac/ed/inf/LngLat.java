package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LngLat {
    @JsonProperty("longitude")
    double lng;
    @JsonProperty("latitude")
    double lat;

    public LngLat(double lng, double lat){
        this.lng = lng;
        this.lat = lat;
    }

    public LngLat(){}


    boolean inCentralArea(){
     List<LngLat> polygon = CentralArea.getInstance().centralPoints;

     boolean inside = false;

     for(int i = 0, j = polygon.size() - 1; i < polygon.size(); j = i++){
         if((polygon.get(i).lat > this.lat) != (polygon.get(j).lat > this.lat) &&
                 (this.lng < (polygon.get(j).lng - polygon.get(i).lng) * (this.lat - polygon.get(i).lat) /
                         (polygon.get(j).lat - polygon.get(i).lat) + polygon.get(i).lng)){
             inside = !inside;
         }
     }
     return inside;
    }

    double distanceTo(LngLat point){

        return 0.888;
    }

    boolean closeTo(LngLat point){

        return true;
    }
}
