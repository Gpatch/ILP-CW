package uk.ac.ed.inf;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OrderValidator {
    public static OrderOutcome orderStatusAfterInit(Restaurant[] restaurants, Order order){
        if(!isValidOrderNumberFormat(order)){
            return OrderOutcome.InvalidOrderNumberFormat;
        }
        if(!CreditCardValidator.isCreditCardNumberValid(order.creditCardNumber)){
            return OrderOutcome.InvalidCardNumber;
        }
        if(!CreditCardValidator.isExpiryDateValid(order.creditCardExpiry, order.orderDate)){
            return OrderOutcome.InvalidExpiryDate;
        }
        if(!CreditCardValidator.isCvvValid(order.cvv)){
            return OrderOutcome.InvalidCvv;
        }
        if(!isValidPizzaCount(order.orderItems)){
            return OrderOutcome.InvalidPizzaCount;
        }
        if(!arePizzasDefined(restaurants, order.orderItems)){
            return OrderOutcome.InvalidPizzaNotDefined;
        }
        if(!isValidPizzaCombination(restaurants, order)){
            return OrderOutcome.InvalidPizzaCombinationMultipleSuppliers;
        }
        if(!isPriceValid(restaurants, order)){
            return OrderOutcome.InvalidTotal;
        }
        return OrderOutcome.ValidButNotDelivered;
    }

    private static boolean isValidOrderNumberFormat(Order order){
        if(order.orderNo == null){
            return false;
        }
        String regex = "[a-fA-F0-9]{8}$";
        Pattern p = Pattern.compile(regex);

        Matcher m = p.matcher(order.orderNo);
        return m.matches();
    }

    private static boolean isPriceValid(Restaurant[] restaurants, Order order) {
        return order.priceTotalInPence == order.getDeliveryCost(restaurants, order.orderItems);
    }

    /**
     * Checks for a valid combination of pizzas. First checks if pizza names exist. Then checks if all the pizzas in the
     * list are from the same supplier. If both conditions are met, then pizzas are of valid combination.
     * @see Order#getDeliveryCost(Restaurant[], String[])
     * @param restaurants an array of Restaurant objects participating.
     * @return true if pizza combination is valid, false otherwise.
     */
    private static boolean isValidPizzaCombination(Restaurant [] restaurants, Order order) {
        Map<String, String> pizzaRestaurantMap = orderPizzasToRestaurantMap(restaurants, order.orderItems);
        boolean pizzasFromSameSupplier = pizzaRestaurantMap.values().stream().distinct().count() == 1;
        String tempRestaurant = pizzaRestaurantMap.get(order.orderItems[0]);
        for(Restaurant r : restaurants){
            if(Objects.equals(r.name, tempRestaurant)){
                order.restaurant = r;
                break;
            }
        }
        return pizzasFromSameSupplier;
    }

    private static boolean arePizzasDefined(Restaurant[] restaurants, String[] pizzas){
        Map<String, String> pizzaRestaurantMap = orderPizzasToRestaurantMap(restaurants, pizzas);
        boolean pizzasValid = Arrays.stream(pizzas).allMatch(p -> pizzaRestaurantMap.get(p) != null);
        return pizzasValid;
    }

    private static Map<String, String> orderPizzasToRestaurantMap(Restaurant[] restaurants, String[] pizzas) {
        Map<String, String> pizzaRestaurantMap = new HashMap<>();
        for(Restaurant restaurant : restaurants){
            List<String> restaurantPizzaList = Arrays.stream(restaurant.getMenu()).map(menuItem -> menuItem.name).collect(Collectors.toList());
            for(String pizza : pizzas){
                if(restaurantPizzaList.contains(pizza)){pizzaRestaurantMap.put(pizza, restaurant.name);}
            }
        }
        return pizzaRestaurantMap;
    }

    /**
     * Check is the pizza count is valid. Being valid means count must be greater than 0, and less than or equal to 4.
     * @see Order#getDeliveryCost(Restaurant[], String[])
     * @param pizzas a list of strings representing pizza names.
     * @return true if number of pizzas is valid, false otherwise
     */
    private static boolean isValidPizzaCount(String[] pizzas) {
        return pizzas.length > 0 && pizzas.length <= 4;
    }
}
