package uk.ac.ed.inf;

/**
 * An enum representing sixteen compass directions with their degrees. Additionally with a NULL direction which should be used for drone hovering movement.
 */
public enum Compass {
    E(0.0),
    ENE(22.5),
    NE(45),
    NNE(67.5),
    N(90),
    NNW(112.5),
    NW(135),
    WNW(157.5),
    W(180),
    WSW(202.5),
    SW(224),
    SSW(247.5),
    S(270),
    SSE(292.5),
    SE(315),
    ESE(337.5),
    NULL(-1.0);


    public final double angle;

    /**
     * Initializing an angle for every compass direction.
     * @param angle that is being initialized.
     */
    Compass(double angle){this.angle = angle;}
}
