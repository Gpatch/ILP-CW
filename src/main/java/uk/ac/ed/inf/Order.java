package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Represents an order made by a customer. Currently, only stores the restaurant from which order has been made,
 * given the order is valid, and it's status.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {
    public String orderNo;
    public String orderDate;
    public String customer;
    public String creditCardNumber;
    public String creditCardExpiry;
    public String cvv;
    public int priceTotalInPence;
    public String[] orderItems;

    public OrderOutcome outcome;
    public Restaurant restaurant;

    /**
     * A public constructor for the Order class.
     */
    @JsonCreator
    public Order(){
    }

    /**
     * Calculates the delivery cost, given that the pizza combination and pizza count in the order are valid.
     * Each delivery also includes an additional charge of 1£.
     * @param restaurants an array of Restaurant objects participating.
     * @param pizzas and array of strings representing pizza names in the order.
     * @return the total cost of the delivery. If the pizza combination and count are valid, then returns sum of their prices with
     * and additional delivery charge of 1£. Otherwise, if either or both pizza combination and count are invalid, returns 0.
     * @throws InvalidPizzaCombinationException if number of pizzas is invalid, pizza doesn't exist or pizzas are not from the same supplier.
     */
    public int getDeliveryCost(Restaurant[] restaurants, String[] pizzas) {
        int cost = 0;
        final int DELIVERY_COST = 100;

//        if(!checkValidPizzaCount(pizzas) || !checkValidPizzaCombination(restaurants, pizzas)){
//            throw new InvalidPizzaCombinationException(outcome.name());
//        }

        List<Menu> orderedPizzas = Arrays.stream(this.restaurant.getMenu()).filter(item -> Arrays.asList(pizzas).contains(item.name)).toList();
        cost += orderedPizzas.stream().mapToInt(item -> item.priceInPence).sum();
        cost += DELIVERY_COST;

        return cost;
    }

    public static Order[] initOrders(String serverBaseAddress, String date, Restaurant[] restaurants){
        Order[] requestedOrders = new Request(serverBaseAddress, Endpoint.ORDERS.endpoint+"/"+date).getOrders();
        List<Order> listOrders = new ArrayList<>();
        for(Order o : requestedOrders){
            OrderOutcome outcome = OrderValidator.orderStatusAfterInit(restaurants, o);
            o.setOrderOutcome(outcome);
            System.err.println(outcome);
            if(o.outcome == OrderOutcome.ValidButNotDelivered){
                listOrders.add(o);
            }
        }
        return listOrders.toArray(Order[] ::new);
    }

    public void setOrderOutcome(OrderOutcome outcome){
        this.outcome = outcome;
    }
}
