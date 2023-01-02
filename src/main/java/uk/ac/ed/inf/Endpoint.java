package uk.ac.ed.inf;

/**
 * Enum used to store the endpoints which are accessed frequently in the system
 */
public enum Endpoint {
    RESTAURANTS("restaurants"),
    CENTRAL_AREA("centralArea"),
    ORDERS("orders"),
    NO_FLY_ZONES("noflyzones");

    public final String endpoint;

    Endpoint(String endpoint) {
        this.endpoint = endpoint;
    }
}
