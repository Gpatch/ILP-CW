package uk.ac.ed.inf;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents an order made by a customer. Currently, only stores the restaurant from which order has been made,
 * given the order is valid, and it's status.
 */
public class Order {
    private OrderOutcome orderStatus;
    private Restaurant restaurant;

    /**
     * A public constructor for the Order class.
     */
    public Order(){}

    /**
     * Calculates the delivery cost, given that the pizza combination and pizza count in the order are valid.
     * Each delivery also includes an additional charge of 1£.
     * @param restaurants an array of Restaurant objects participating.
     * @param pizzas and array of strings representing pizza names in the order.
     * @return the total cost of the delivery. If the pizza combination and count are valid, then returns sum of their prices with
     * and additional delivery charge of 1£. Otherwise, if either or both pizza combination and count are invalid, returns 0.
     */
    public int getDeliveryCost(Restaurant[] restaurants, String[] pizzas){
        int cost = 0;
        final int DELIVERY_COST = 100;

        try{
            checkValidPizzaCount(pizzas);
            checkValidPizzaCombination(restaurants, pizzas);
        }catch (InvalidPizzaCombinationException e){
            System.err.println("Error: " + orderStatus);
            return 0;
        }

        List<Menu> orderedPizzas = Arrays.stream(this.restaurant.getMenu()).filter(item -> Arrays.asList(pizzas).contains(item.name)).toList();
        cost += orderedPizzas.stream().mapToInt(item -> item.priceInPence).sum();
        cost += DELIVERY_COST;

        return cost;
    }

    /**
     * Check is the pizza count is valid. Being valid means count must be greater than 0, and less than or equal to 4.
     * NOTE: any method using this function must handle the InvalidPizzaCombinationException exception appropriately,
     * so that no runtime problems occur. Like shown in the getDeliveryCost function.
     * @see Order#getDeliveryCost(Restaurant[], String[])
     * @param pizzas a list of strings representing pizza names.
     * @throws InvalidPizzaCombinationException if the count is invalid.
     */
    private void checkValidPizzaCount(String[] pizzas) throws InvalidPizzaCombinationException {
        boolean validCount =  pizzas.length > 0 && pizzas.length <= 4;
        if(!validCount){
            orderStatus = OrderOutcome.InvalidPizzaCount;
            throw new InvalidPizzaCombinationException();
        }
    }

    /**
     * Checks for a valid combination of pizzas. First checks if pizza names exist. Then checks if all the pizzas in the
     * list are from the same supplier. If both conditions are met, then pizzas are of valid combination.
     * NOTE: any method using this function must handle the InvalidPizzaCombinationException exception appropriately,
     * so that no runtime problems occur. Like shown in the getDeliveryCost function.
     * @see Order#getDeliveryCost(Restaurant[], String[])
     * @param restaurants an array of Restaurant objects participating.
     * @param pizzas a list of strings representing pizza names.
     * @throws InvalidPizzaCombinationException if either pizzas don't exist or they are not from the same supplier.
     */
    private void checkValidPizzaCombination(Restaurant [] restaurants, String[] pizzas) throws InvalidPizzaCombinationException {
        Map<String, String> pizzaRestaurantMap = new HashMap<>();
        for(Restaurant restaurant : restaurants){
            List<String> restaurantPizzaList = Arrays.stream(restaurant.getMenu()).map(menuItem -> menuItem.name).collect(Collectors.toList());
            for(String pizza : pizzas){
                if(restaurantPizzaList.contains(pizza)){pizzaRestaurantMap.put(pizza, restaurant.name);}
            }
        }
        boolean pizzasValid = Arrays.stream(pizzas).allMatch(p -> pizzaRestaurantMap.get(p) != null);
        boolean pizzasFromSameSupplier = pizzaRestaurantMap.values().stream().distinct().count() == 1;

        if(!pizzasValid){orderStatus = OrderOutcome.InvalidPizzaNotDefined;}
        else if(!pizzasFromSameSupplier){
            orderStatus = OrderOutcome.InvalidPizzaCombinationMultipleSuppliers;
        }else{
            String tempRestaurant = pizzaRestaurantMap.get(pizzas[0]);
            for(Restaurant r : restaurants){
                if(Objects.equals(r.name, tempRestaurant)){
                    this.restaurant = r;
                    break;
                }
            }
        }
        boolean valid = pizzasValid && pizzasFromSameSupplier;
        if (!valid){ throw new InvalidPizzaCombinationException(); }
    }
}
