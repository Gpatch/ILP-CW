package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Menu {
    @JsonProperty("name")
    String name;
    @JsonProperty("priceInPence")
    int priceInPence;

    public Menu(String name, int price){
        this.name = name;
        this.priceInPence = price;
    }

    public Menu(){}
}
