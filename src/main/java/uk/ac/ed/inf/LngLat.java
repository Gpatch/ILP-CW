package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents a point with two coordinates being: longitude and latitude.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LngLat {
    public double lng;
    public double lat;

    /**
     * A public constructor for a point used for deserializing from JSON requested form the REST API.
     * @param lng represents the longitude value of the point.
     * @param lat represents the latitude value of the point.
     */
    @JsonCreator
    public LngLat(@JsonProperty("longitude") double lng, @JsonProperty("latitude") double lat){
        this.lng = lng;
        this.lat = lat;
    }

    /**
     * Checks if the point is within the specified central area, including being on the border of the coordinates.
     * An "Even-Odd Rule" algorithm has been used to check if the point is inside a polygon.
     * For reference https://en.wikipedia.org/wiki/Even–odd_rule .
     * @return true if is inside the polygon or on the border, false if outside.
     */
    public boolean inCentralArea(){
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

    /**
     * Calculates the distance between two points.
     * @param point to which distance is calculated.
     * @return the Pythagorean distance between the two points.
     */
    public double distanceTo(LngLat point){
        return Math.sqrt(Math.pow((this.lng - point.lng), 2) + Math.pow((this.lat - point.lat), 2));
    }

    /**
     * Checks if the two points are closed to each other.
     * Being close is defined by points having a distance to each other strictly less than 0.00015 units.
     * @param point to which the proximity is checked.
     * @return true if the points are close, false otherwise.
     */
    public boolean closeTo(LngLat point){ return distanceTo(point) < 0.00015; }

    /**
     * Calculates the next position of the point after one move.
     * Given direction in terms of the angle and the default distance of one move being 0.00015 units,
     * next position is being calculated using trigonometry formulae,
     * new_x = x + (d * cos(alpha)),
     * new_y = y + (d * sin(alpha))
     * Where x is longitude, y is latitude, d is the distance to travel and alpha is the direction in radians.
     * @param direction in which to travel passed in degrees.
     * @return the LngLat object which represents the new point at the next position after one move.
     */
    public LngLat nextPosition(Compass direction){
        LngLat result;
        double d = 0.00015;

        if (direction == Compass.NULL){
            return this;
        }else{

            double resLng = this.lng + (d * Math.cos(Math.toRadians(direction.angle)));
            double resLat = this.lat + (d * Math.sin(Math.toRadians(direction.angle)));
            result = new LngLat(resLng, resLat);
        }
        return result;
    }
}
