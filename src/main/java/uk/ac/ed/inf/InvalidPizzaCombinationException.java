package uk.ac.ed.inf;

/**
 * Represents an Exception which is thrown when pizza combination is not valid.
 * Examples: some pizza names on the list don't exist, pizzas are not from the same supplier or number of pizzas in the order
 * is beyond the set limits.
 */
public class InvalidPizzaCombinationException extends Exception{
    /**
     * A public constructor for the exception using a custom message.
     * @param s message to be displayed when the exception is thrown.
     */
    public InvalidPizzaCombinationException(String s){
        super(s);
    }

    /**
     * A public empty default constructor for the exception.
     */
    public InvalidPizzaCombinationException(){}
}
