package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represent an item on the restaurant's menu.
 */
public class Menu {
    private final String name;
    private final int priceInPence;

    /**
     * A public constructor for a menu item used for deserializing from JSON requested form the REST API.
     * @param name is then name of the menu item.
     * @param price is the price in pence of the menu item.
     */
    @JsonCreator
    public Menu(@JsonProperty("name") String name, @JsonProperty("priceInPence") int price){
        this.name = name;
        this.priceInPence = price;
    }

    public String getName(){return name;}
    public int getPriceInPence(){return priceInPence;}
}
