package uk.ac.ed.inf;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A static class used to validate the order
 */
public class OrderValidator {
    /**
     * Sets the correct outcome status for the order
     * @param restaurants participating
     * @param order for which outcome status to be set
     * @param date when order needs to be delivered
     * @return the outcome status of the order
     */
    public static OrderOutcome orderStatusAfterInit(Restaurant[] restaurants, Order order, String date){
        if(!isValidDate(order.getOrderDate(), date)){
            return OrderOutcome.InvalidDate;
        }
        if(!isValidOrderNumberFormat(order)){
            return OrderOutcome.InvalidOrderNumberFormat;
        }
        if(!CreditCardValidator.isCreditCardNumberValid(order.getCreditCardNumber())){
            return OrderOutcome.InvalidCardNumber;
        }
        if(!CreditCardValidator.isExpiryDateValid(order.getCreditCardExpiry(), order.getOrderDate())){
            return OrderOutcome.InvalidExpiryDate;
        }
        if(!CreditCardValidator.isCvvValid(order.getCvv())){
            return OrderOutcome.InvalidCvv;
        }
        if(!isValidPizzaCount(order.getOrderItems())){
            return OrderOutcome.InvalidPizzaCount;
        }
        if(!arePizzasDefined(restaurants, order.getOrderItems())){
            return OrderOutcome.InvalidPizzaNotDefined;
        }
        if(!isValidPizzaCombination(restaurants, order)){
            return OrderOutcome.InvalidPizzaCombinationMultipleSuppliers;
        }
        if(!isPriceValid(order)){
            return OrderOutcome.InvalidTotal;
        }
        return OrderOutcome.ValidButNotDelivered;
    }

    private static boolean isValidOrderNumberFormat(Order order){
        if(order.getOrderNo() == null){
            return false;
        }
        String regex = "[a-fA-F0-9]{8}$";
        Pattern p = Pattern.compile(regex);

        Matcher m = p.matcher(order.getOrderNo());
        return m.matches();
    }

    private static boolean isPriceValid(Order order) {
        return order.getPriceTotalInPence() == order.getDeliveryCost(order.getOrderItems());
    }

    /**
     * Checks for a valid combination of pizzas. First checks if pizza names exist. Then checks if all the pizzas in the
     * list are from the same supplier. If both conditions are met, then pizzas are of valid combination.
     * @param restaurants an array of Restaurant objects participating.
     * @return true if pizza combination is valid, false otherwise.
     */
    private static boolean isValidPizzaCombination(Restaurant [] restaurants, Order order) {
        Map<String, String> pizzaRestaurantMap = orderPizzasToRestaurantMap(restaurants, order.getOrderItems());
        boolean pizzasFromSameSupplier = pizzaRestaurantMap.values().stream().distinct().count() == 1;
        String tempRestaurant = pizzaRestaurantMap.get(order.getOrderItems()[0]);
        for(Restaurant r : restaurants){
            if(Objects.equals(r.getName(), tempRestaurant)){
                order.setRestaurant(r);
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
            List<String> restaurantPizzaList = Arrays.stream(restaurant.getMenu()).map(Menu::getName).collect(Collectors.toList());
            for(String pizza : pizzas){
                if(restaurantPizzaList.contains(pizza)){pizzaRestaurantMap.put(pizza, restaurant.getName());}
            }
        }
        return pizzaRestaurantMap;
    }



    //Check is the pizza count is valid. Being valid means count must be greater than 0, and less than or equal to 4.
    private static boolean isValidPizzaCount(String[] pizzas) {
        return pizzas.length > 0 && pizzas.length <= 4;
    }

    private static boolean isValidDate(String orderDate, String actualDate){
        return Objects.equals(orderDate, actualDate);
    }

}
