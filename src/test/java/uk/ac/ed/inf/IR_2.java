package uk.ac.ed.inf;

import org.junit.Test;

public class IR_2 {
    String serverBaseAddress = "https://ilp-rest.azurewebsites.net/";
    Restaurant[] restaurants = Restaurant.requestRestaurants(serverBaseAddress);

    @Test
    public void paymentValidFirstDate(){
        String date = "2023-01-01";
        Order[] orders = Order.initOrders(serverBaseAddress, date, Restaurant.requestRestaurants(serverBaseAddress));
        for(Order order : orders){
            if(!CreditCardValidator.isCreditCardNumberValid(order.getCreditCardNumber())){
                System.out.println(order.getOrderNo() + ": " + OrderOutcome.InvalidCardNumber);
            }
            if(!CreditCardValidator.isExpiryDateValid(order.getCreditCardExpiry(), order.getOrderDate())){
                System.out.println(order.getOrderNo() + ": " + OrderOutcome.InvalidCardNumber);
            }
            if(!CreditCardValidator.isCvvValid(order.getCvv())){
                System.out.println(order.getOrderNo() + ": " + OrderOutcome.InvalidCardNumber);
            }
        }
    }

    @Test
    public void paymentValidSecondDate(){
        String date = "2023-01-02";
        Order[] orders = Order.initOrders(serverBaseAddress, date, Restaurant.requestRestaurants(serverBaseAddress));
        for(Order order : orders){
            if(!CreditCardValidator.isCreditCardNumberValid(order.getCreditCardNumber())){
                System.out.println(order.getOrderNo() + ": " + OrderOutcome.InvalidCardNumber);
            }
            if(!CreditCardValidator.isExpiryDateValid(order.getCreditCardExpiry(), order.getOrderDate())){
                System.out.println(order.getOrderNo() + ": " + OrderOutcome.InvalidCardNumber);
            }
            if(!CreditCardValidator.isCvvValid(order.getCvv())){
                System.out.println(order.getOrderNo() + ": " + OrderOutcome.InvalidCardNumber);
            }
        }
    }

    @Test
    public void paymentValidThirdDate(){
        String date = "2023-01-03";
        Order[] orders = Order.initOrders(serverBaseAddress, date, Restaurant.requestRestaurants(serverBaseAddress));
        for(Order order : orders){
            if(!CreditCardValidator.isCreditCardNumberValid(order.getCreditCardNumber())){
                System.out.println(order.getOrderNo() + ": " + OrderOutcome.InvalidCardNumber);
            }
            if(!CreditCardValidator.isExpiryDateValid(order.getCreditCardExpiry(), order.getOrderDate())){
                System.out.println(order.getOrderNo() + ": " + OrderOutcome.InvalidCardNumber);
            }
            if(!CreditCardValidator.isCvvValid(order.getCvv())){
                System.out.println(order.getOrderNo() + ": " + OrderOutcome.InvalidCardNumber);
            }
        }
    }
    @Test
    public void paymentValidFourthDate(){
        String date = "2023-01-04";
        Order[] orders = Order.initOrders(serverBaseAddress, date, Restaurant.requestRestaurants(serverBaseAddress));
        for(Order order : orders){
            if(!CreditCardValidator.isCreditCardNumberValid(order.getCreditCardNumber())){
                System.out.println(order.getOrderNo() + ": " + OrderOutcome.InvalidCardNumber);
            }
            if(!CreditCardValidator.isExpiryDateValid(order.getCreditCardExpiry(), order.getOrderDate())){
                System.out.println(order.getOrderNo() + ": " + OrderOutcome.InvalidCardNumber);
            }
            if(!CreditCardValidator.isCvvValid(order.getCvv())){
                System.out.println(order.getOrderNo() + ": " + OrderOutcome.InvalidCardNumber);
            }
        }
    }
    @Test
    public void paymentValidFifthDate(){
        String date = "2023-01-05";
        Order[] orders = Order.initOrders(serverBaseAddress, date, Restaurant.requestRestaurants(serverBaseAddress));
        for(Order order : orders){
            if(!CreditCardValidator.isCreditCardNumberValid(order.getCreditCardNumber())){
                System.out.println(order.getOrderNo() + ": " + OrderOutcome.InvalidCardNumber);
            }
            if(!CreditCardValidator.isExpiryDateValid(order.getCreditCardExpiry(), order.getOrderDate())){
                System.out.println(order.getOrderNo() + ": " + OrderOutcome.InvalidCardNumber);
            }
            if(!CreditCardValidator.isCvvValid(order.getCvv())){
                System.out.println(order.getOrderNo() + ": " + OrderOutcome.InvalidCardNumber);
            }
        }
    }



    @Test
    public void orderValidFirstDate(){
        String date = "2023-01-01";
        Order[] orders = Order.initOrders(serverBaseAddress, date, Restaurant.requestRestaurants(serverBaseAddress));
        for(Order order : orders){
            System.out.print(order.getOrderNo() + ": ");
            OrderValidator.orderInfo(restaurants, order, date);
        }
    }
    @Test
    public void orderValidSecondDate(){
        String date = "2023-01-02";
        Order[] orders = Order.initOrders(serverBaseAddress, date, Restaurant.requestRestaurants(serverBaseAddress));
        for(Order order : orders){
            System.out.print(order.getOrderNo() + ": ");
            OrderValidator.orderInfo(restaurants, order, date);
        }
    }
    @Test
    public void orderValidThirdDate(){
        String date = "2023-01-03";
        Order[] orders = Order.initOrders(serverBaseAddress, date, Restaurant.requestRestaurants(serverBaseAddress));
        for(Order order : orders){
            System.out.print(order.getOrderNo() + ": ");
            OrderValidator.orderInfo(restaurants, order, date);
        }
    }
    @Test
    public void orderValidFourthDate(){
        String date = "2023-01-04";
        Order[] orders = Order.initOrders(serverBaseAddress, date, Restaurant.requestRestaurants(serverBaseAddress));
        for(Order order : orders){
            System.out.print(order.getOrderNo() + ": ");
            OrderValidator.orderInfo(restaurants, order, date);
        }
    }

    @Test
    public void orderValidFifthDate(){
        String date = "2023-01-05";
        Order[] orders = Order.initOrders(serverBaseAddress, date, Restaurant.requestRestaurants(serverBaseAddress));
        for(Order order : orders){
            System.out.print(order.getOrderNo() + ": ");
            OrderValidator.orderInfo(restaurants, order, date);
        }
    }
}
