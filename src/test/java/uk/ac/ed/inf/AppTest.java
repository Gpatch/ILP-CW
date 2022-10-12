package uk.ac.ed.inf;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    LngLat pointInside = new LngLat(-3.189473, 55.944233);
    LngLat pointOutside = new LngLat(-3.192474, 55.944233);
    LngLat pointOnLine = new LngLat(-3.184319, 55.944617);

    @Test
    public void insideTheCentralArea() {
        assertTrue(pointInside.inCentralArea());
    }

    @Test
    public void outsideTheCentralArea(){
        assertFalse(pointOutside.inCentralArea());
    }

    @Test
    public void onEdge(){
        assertTrue(pointOnLine.inCentralArea());
    }

}
