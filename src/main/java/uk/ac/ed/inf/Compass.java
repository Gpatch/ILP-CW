package uk.ac.ed.inf;

/**
 * An enum representing sixteen compass directions with their degrees.
 * Additionally, with a NULL direction which should be used for drone hovering movement.
 * Note, that -1 angle used for the NULL direction is purely a placeholder, serving no real functionality
 * during calculations.
 */
public enum Compass {
    E(0.0),
    ENE(22.5),
    NE(45.0),
    NNE(67.5),
    N(90.0),
    NNW(112.5),
    NW(135.0),
    WNW(157.5),
    W(180.0),
    WSW(202.5),
    SW(224.0),
    SSW(247.5),
    S(270.0),
    SSE(292.5),
    SE(315.0),
    ESE(337.5),
    NULL(-1.0);


    public final double angle;

    /**
     * Initializing an angle for every compass direction.
     * @param angle in degrees that is being initialized.
     */
    Compass(Double angle){this.angle = angle;}
}
