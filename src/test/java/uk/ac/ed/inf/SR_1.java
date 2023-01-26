package uk.ac.ed.inf;

import org.junit.Assert;
import org.junit.Test;

public class SR_1 {
    @Test
    public void seventyPercentDeliveredDay1() {
        String baseAddress = "https://ilp-rest.azurewebsites.net/";
        Drone drone = new Drone(baseAddress, "2023-01-01");
        Download.download(baseAddress, Endpoint.CENTRAL_AREA.endpoint, "json");
        Download.download(baseAddress, Endpoint.NO_FLY_ZONES.endpoint, "json");
        drone.deliverOrders();


        int delivered = 0;
        for(Order order : drone.getOrders()){
            if(order.getOutcome() == OrderOutcome.Delivered){
                delivered++;
            }
        }

        int validOrders = 0;

        for(Order order : drone.getOrders()){
            if(order.getOutcome() == OrderOutcome.ValidButNotDelivered || order.getOutcome() == OrderOutcome.Delivered){
                validOrders++;
            }
        }

        boolean condition = delivered >= Math.floor(validOrders * 0.7);
        Assert.assertTrue(condition);
    }

    @Test
    public void seventyPercentDeliveredDay2() {
        String baseAddress = "https://ilp-rest.azurewebsites.net/";
        Drone drone = new Drone(baseAddress, "2023-01-02");
        Download.download(baseAddress, Endpoint.CENTRAL_AREA.endpoint, "json");
        Download.download(baseAddress, Endpoint.NO_FLY_ZONES.endpoint, "json");
        drone.deliverOrders();


        int delivered = 0;
        for(Order order : drone.getOrders()){
            if(order.getOutcome() == OrderOutcome.Delivered){
                delivered++;
            }
        }

        int validOrders = 0;

        for(Order order : drone.getOrders()){
            if(order.getOutcome() == OrderOutcome.ValidButNotDelivered || order.getOutcome() == OrderOutcome.Delivered){
                validOrders++;
            }
        }

        boolean condition = delivered >= Math.floor(validOrders * 0.7);
        Assert.assertTrue(condition);
    }

    @Test
    public void seventyPercentDeliveredDay3() {
        String baseAddress = "https://ilp-rest.azurewebsites.net/";
        Drone drone = new Drone(baseAddress, "2023-01-03");
        Download.download(baseAddress, Endpoint.CENTRAL_AREA.endpoint, "json");
        Download.download(baseAddress, Endpoint.NO_FLY_ZONES.endpoint, "json");
        drone.deliverOrders();


        int delivered = 0;
        for(Order order : drone.getOrders()){
            if(order.getOutcome() == OrderOutcome.Delivered){
                delivered++;
            }
        }

        int validOrders = 0;

        for(Order order : drone.getOrders()){
            if(order.getOutcome() == OrderOutcome.ValidButNotDelivered || order.getOutcome() == OrderOutcome.Delivered){
                validOrders++;
            }
        }

        boolean condition = delivered >= Math.floor(validOrders * 0.7);
        Assert.assertTrue(condition);
    }

    @Test
    public void seventyPercentDeliveredDay4() {
        String baseAddress = "https://ilp-rest.azurewebsites.net/";
        Drone drone = new Drone(baseAddress, "2023-01-04");
        Download.download(baseAddress, Endpoint.CENTRAL_AREA.endpoint, "json");
        Download.download(baseAddress, Endpoint.NO_FLY_ZONES.endpoint, "json");
        drone.deliverOrders();


        int delivered = 0;
        for(Order order : drone.getOrders()){
            if(order.getOutcome() == OrderOutcome.Delivered){
                delivered++;
            }
        }

        int validOrders = 0;

        for(Order order : drone.getOrders()){
            if(order.getOutcome() == OrderOutcome.ValidButNotDelivered || order.getOutcome() == OrderOutcome.Delivered){
                validOrders++;
            }
        }

        boolean condition = delivered >= Math.floor(validOrders * 0.7);
        Assert.assertTrue(condition);
    }

    @Test
    public void seventyPercentDeliveredDay5() {
        String baseAddress = "https://ilp-rest.azurewebsites.net/";
        Drone drone = new Drone(baseAddress, "2023-01-05");
        Download.download(baseAddress, Endpoint.CENTRAL_AREA.endpoint, "json");
        Download.download(baseAddress, Endpoint.NO_FLY_ZONES.endpoint, "json");
        drone.deliverOrders();


        int delivered = 0;
        for(Order order : drone.getOrders()){
            if(order.getOutcome() == OrderOutcome.Delivered){
                delivered++;
            }
        }

        int validOrders = 0;

        for(Order order : drone.getOrders()){
            if(order.getOutcome() == OrderOutcome.ValidButNotDelivered || order.getOutcome() == OrderOutcome.Delivered){
                validOrders++;
            }
        }

        boolean condition = delivered >= Math.floor(validOrders * 0.7);
        Assert.assertTrue(condition);
    }
}
