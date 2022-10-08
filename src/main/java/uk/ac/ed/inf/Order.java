package uk.ac.ed.inf;

import java.util.*;
import java.util.stream.Collectors;

public class Order {
    private OrderOutcome orderStatus;
    private Restaurant restaurant;

    public Order(){}

    public int getDeliveryCost(Restaurant[] restaurants, String[] pizzas) throws InvalidPizzaCombinationException {
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

    private boolean checkValidPizzaCount(String[] pizza) throws InvalidPizzaCombinationException {
        boolean validCount =  pizza.length > 0 && pizza.length <= 4;
        if(!validCount){
            orderStatus = OrderOutcome.InvalidPizzaCount;
            throw new InvalidPizzaCombinationException();
        }
        return validCount;
    }


    private boolean checkValidPizzaCombination(Restaurant [] restaurants, String[] pizzas) throws InvalidPizzaCombinationException {
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
        return valid;
    }
}
