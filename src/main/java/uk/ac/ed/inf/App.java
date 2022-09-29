package uk.ac.ed.inf;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        CentralArea CA = CentralArea.getInstance();

        LngLat point = new LngLat(-3.190001, 55.945001);

        for (int i = 0; i <= 3; i ++){
            System.out.println(CA.centralPoints.get(i).lng + ", " +  CA.centralPoints.get(i).lat);
        }

        System.out.println(point.inCentralArea());
    }
}
