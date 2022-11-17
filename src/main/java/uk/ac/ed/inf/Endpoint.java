package uk.ac.ed.inf;

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
