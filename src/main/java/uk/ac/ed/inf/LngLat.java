package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Represents a point with two coordinates being: longitude and latitude.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LngLat {
    private final double lng;
    private final double lat;

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

    //Checks if the line between two points is horizontal.
    private boolean isLineHorizontal(LngLat x, LngLat y){
        return x.lat == y.lat;
    }


    //Checks if the line between two points is vertical.
    private boolean isLineVertical(LngLat x, LngLat y){
        return x.lng == y.lng;
    }


    private double calculateGradient(LngLat x, LngLat y){
        return (y.lng - x.lng) / (y.lat - x.lat);
    }

    private double calculateIntercept(double m, LngLat x){
        return x.lat - (m * x.lng);
    }

    //Checks if a given point lies on the line. With an acceptable degree of 10^-12 due to floating point rounding errors.
    private boolean onLine(double m, double c, LngLat point){
        double result =  (m * point.lng) + c;
        return (result + Math.pow(10, -12) >= point.lng) || (result - Math.pow(10, -12) <= point.lng);
    }


    //Checks if a point is in the region between other two points.
    private boolean betweenTwoPoints(LngLat x, LngLat y, LngLat point){
        return (point.lng <= Math.max(x.lng, y.lng)) && (point.lng >= Math.min(x.lng, y.lng))
                && (point.lat <= Math.max(x.lat, y.lat)) && (point.lat >= Math.min(x.lat, y.lat));
    }


     //Checks if the points lies on the line segment.
    private boolean onLineSegment(LngLat x, LngLat y, LngLat point){
        double m;
        double c;
        boolean result = false;

        if(isLineHorizontal(x, y) || isLineVertical(x, y)){
            result = betweenTwoPoints(x, y, point);
        }
        else {
            m = calculateGradient(x, y);
            c = calculateIntercept(m, x);
            if (onLine(m, c, point)) {
                result = betweenTwoPoints(x, y, point);
            }
        }
        return result;
    }


    //Check if a point is on one of the edges of the polygon.
    private boolean onPolygonEdge(LngLat point, LngLat[] polygon){
        boolean onEdge = false;

        for(int i = 0, j = 1; i < polygon.length; i++, j++){
            j %= polygon.length;

            if(onLineSegment(polygon[i], polygon[j], point)){
                onEdge = true;
                break;
            }
        }
        return onEdge;
    }

    /**
     * Checks if the point is within the specified central area, including being on the edge.
     * An "Even-Odd Rule" algorithm has been used to check if the point is inside a polygon.
     * For reference https://en.wikipedia.org/wiki/Even–odd_rule .2
     * @return true if is inside the polygon or on the edge, false if outside.
     */
    public boolean inArea(LngLat[] coordinates){
        boolean inside = false;

     if(onPolygonEdge(this, coordinates)){ return true; }

     for(int i = 0, j = coordinates.length - 1; i < coordinates.length; j =  i++){
         if((coordinates[i].lat > this.lat) != (coordinates[j].lat > this.lat) &&
                 (this.lng < (coordinates[j].lng - coordinates[i].lng) * (this.lat - coordinates[i].lat) /
                         (coordinates[j].lat - coordinates[i].lat) + coordinates[i].lng)){
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

    /**
     * Gets longitude and latitude in the form of array
     * @return double array with two elements: longitude and latitude
     */
    public double[] getCoordinates(){
        return new double[]{lng, lat};
    }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(!(o instanceof LngLat)){
            return false;
        }

        return (this.lng == ((LngLat) o).lng) && (this.lat == ((LngLat) o).lat);
    }


    @Override
    public int hashCode() {
        return Objects.hash(lng, lat);
    }

    @Override
    public String toString(){
        return "[" + lng + ", " + lat + "]";
    }

}
