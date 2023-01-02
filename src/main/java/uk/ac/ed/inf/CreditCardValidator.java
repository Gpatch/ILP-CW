package uk.ac.ed.inf;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A static class which serves a purpose of validating a credit card given its number, cvv and expiry date.
 * Validator can process only VISA and MASTERCARD cards.
 */
public class CreditCardValidator {
    private static final int VISA = 4;
    private static final int[] MASTER = {2, 5};

    /**
     * Validates credit cards number using Luhn check algorithm, checking that length is strictly 16 digits,
     * and that correct prefix for VISA/MASTERCARD is used.
     * For reference: https://en.wikipedia.org/wiki/Luhn_algorithm
     * @param cardNumber of the credit card to be validated
     * @return true if the number of digits is 16, correct prefix is used and satisfies the Luhn check,
     * otherwise false
     */
    public static boolean isCreditCardNumberValid(String cardNumber) {
        Long number = Long.parseLong(cardNumber);
        return getSize(number) == 16 && (prefixMatches(number))
              &&  ((sumDoubleEven(number) + sumOddPlaces(number)) % 10 == 0);
    }

    private static int getSize(Long number){
        String num = number.toString();
        return num.length();
    }

    private static long getPrefix(Long number){
        String num = number.toString();
        return Long.parseLong(String.valueOf(num.charAt(0)));
    }

    private static boolean prefixMatches(Long number){
        return (getPrefix(number) == VISA) ||
                (getPrefix(number) == MASTER[0] ||
                getPrefix(number) == MASTER[1]);
    }

    private static int getTwoDigitsSum(int number){
        if(number < 9){
            return number;
        }else{
            return (number / 10) + (number % 10);
        }
    }

    private static int sumDoubleEven(Long cardNumber){
        String number = cardNumber.toString();
        int sum = 0;
        for(int i = getSize(cardNumber) - 2; i >= 0; i -= 2){
            sum += getTwoDigitsSum(Integer.parseInt(number.charAt(i) + "")*2);
        }
        return sum;
    }

    private static int sumOddPlaces(Long cardNumber) {
        String number = cardNumber.toString();
        int sum = 0;
        for(int i = getSize(cardNumber) - 1; i >= 0; i-= 2){
            sum += Integer.parseInt(number.charAt(i) + "");
        }
        return sum;
    }


    /**
     * Checking if the cvv is valid, by using regex to check if the cvv has only 3 digits
     * @param cvv to be processed by the regex
     * @return true if cvv satisfies the regex, otherwise false
     */
    public static boolean isCvvValid(String cvv) {
        String regex = "[0-9]{3}$";
        Pattern p = Pattern.compile(regex);

        if (cvv == null) {
            return false;
        }

        Matcher m = p.matcher(cvv);
        return m.matches();
    }

    /**
     * Checking if expiry date is valid by parsing the date through the formatter of MM/yy format,
     * as well as checking if the expiry date is not before the order date
     * @param expiryDate to be parsed
     * @param orderDate to be checked against the expiry date
     * @return true if the expiry date is of valid format and does not end before the order date,
     * otherwise false
     */
    public static boolean isExpiryDateValid(String expiryDate, String orderDate){
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("MM/yy");
        YearMonth ym;
        try {
            ym = YearMonth.parse(expiryDate, parser);
        }catch (DateTimeParseException e){
            return false;
        }
        LocalDate expiry = ym.atEndOfMonth();
        LocalDate order = LocalDate.parse(orderDate);

        return (expiry.isAfter(order) || expiry.isEqual(order));
    }
}
