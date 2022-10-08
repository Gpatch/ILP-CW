package uk.ac.ed.inf;

import com.sun.jdi.ObjectReference;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException, InvalidPizzaCombinationException {
        CentralArea CA = CentralArea.getInstance();
        LngLat point = new LngLat(-3.192473, 55.942617);

        for (int i = 0; i <= 3; i ++){
            System.out.println(CA.centralPoints.get(i).lng + ", " +  CA.centralPoints.get(i).lat);
        }
        System.out.println("Is drone in central area: " + point.inCentralArea());

        point = point.nextPosition(Compass.SSW);

        System.out.println("New drone position is: " + point.lng + ", " + point.lat);

        Restaurant[] participants = Restaurant.getRestaurantsFromRestServer(new URL("https://ilp-rest.azurewebsites.net/restaurants"));

        Order o = new Order();

        String[] pizzas = {"Margarita"};

        System.out.println(o.getDeliveryCost(participants, pizzas));
    }
}
