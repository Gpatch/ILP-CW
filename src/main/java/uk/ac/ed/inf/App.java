package uk.ac.ed.inf;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        CentralArea CA = CentralArea.getInstance();
        LngLat point = new LngLat(-3.192473, 55.942617);

        for (int i = 0; i <= 3; i ++){
            System.out.println(CA.centralPoints.get(i).lng + ", " +  CA.centralPoints.get(i).lat);
        }
        System.out.println("Is drone in central area: " + point.inCentralArea());

        point = point.nextPosition(Compass.SSW);

        System.out.println("New drone position is: " + point.lng + ", " + point.lat);
    }
}
