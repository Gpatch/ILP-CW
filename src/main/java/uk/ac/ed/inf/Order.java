package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.*;

/**
 * Represents an order made by a customer. Currently, only stores the restaurant from which order has been made,
 * given the order is valid, and it's status.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {
    private String orderNo;
    private String orderDate;
    private OrderOutcome outcome;
    private int priceTotalInPence;
    private String[] orderItems;
    private String creditCardNumber;
    private String creditCardExpiry;
    private String cvv;
    private Restaurant restaurant;

    /**
     * A public constructor for the Order class.
     */
    @JsonCreator
    public Order(){
    }

    /**
     * Calculates the delivery cost, given that the pizza combination and pizza count in the order are valid.
     * Each delivery also includes an additional charge of 1£.
     * @param pizzas and array of strings representing pizza names in the order.
     * @return the total cost of the delivery. If the pizza combination and count are valid, then returns sum of their prices with
     * and additional delivery charge of 1£. Otherwise, if either or both pizza combination and count are invalid, returns 0.
     */
    public int getDeliveryCost(String[] pizzas) {
        int cost = 0;
        final int DELIVERY_COST = 100;

        List<Menu> orderedPizzas = Arrays.stream(this.restaurant.getMenu()).filter(item -> Arrays.asList(pizzas).contains(item.getName())).toList();
        cost += orderedPizzas.stream().mapToInt(Menu::getPriceInPence).sum();
        cost += DELIVERY_COST;

        return cost;
    }

    /**
     * Initialises the orders which are retrieved as a json string from the rest server
     * @param serverBaseAddress of the rest server from which data is retrieved
     * @param date which is used to access the correct orders
     * @param restaurants participating
     * @return array of orders initialised with correct outcome statuses
     */
    public static Order[] initOrders(String serverBaseAddress, String date, Restaurant[] restaurants){
        String ordersJSON = new Request(serverBaseAddress, Endpoint.ORDERS.endpoint+"/"+date).getJSONResponse();
        Order[] requestedOrders = JSONObjectConverter.convertFromJSON(ordersJSON, Order[].class);

        for(Order o : requestedOrders){
            OrderOutcome outcome = OrderValidator.orderStatusAfterInit(restaurants, o, date);
            o.setOrderOutcome(outcome);
        }
        return requestedOrders;
    }


    public void setOrderOutcome(OrderOutcome outcome){
        this.outcome = outcome;
    }
    public void setRestaurant(Restaurant restaurant){
        this.restaurant = restaurant;
    }

    public String getOrderDate(){return orderDate;}
    public String[] getOrderItems(){return orderItems;}
    public int getPriceTotalInPence(){return priceTotalInPence;}
    public String getOrderNo(){return orderNo;}
    public OrderOutcome getOutcome(){return outcome;}
    public String getCreditCardNumber(){return creditCardNumber;}
    public String getCreditCardExpiry(){return creditCardExpiry;}
    public String getCvv(){return cvv;}
    public Restaurant getRestaurant(){return restaurant;}
}
