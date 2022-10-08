package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Menu {
    String name;
    int priceInPence;

    @JsonCreator
    public Menu(@JsonProperty("name") String name, @JsonProperty("priceInPence") int price){
        this.name = name;
        this.priceInPence = price;
    }
}
