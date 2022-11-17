package uk.ac.ed.inf;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreditCardValidator {

    private static final int VISA = 4;
    private static final int MASTER = 5;
    private static final int AMERICAN_EXPRESS = 37;
    private static final int DISCOVER = 6;


    public static boolean isCreditCardNumberValid(String cardNumber){
        Long number = Long.parseLong(cardNumber);

        return (getSize(number) >= 13 && getSize(number) <= 16) &&
                (prefixMatches(number)) && ((sumDoubleEven(number) + sumOddPlaces(number)) % 10 == 0);
    }

    private static int getSize(Long number){
        String num = number.toString();
        return num.length();
    }

    private static long getPrefix(Long number, int prefixLen){
        String num = number.toString();
        return Long.parseLong(num.substring(0, prefixLen));
    }

    private static boolean prefixMatches(Long number){
        return (getPrefix(number, Integer.toString(VISA).length()) == VISA) ||
                (getPrefix(number, Integer.toString(MASTER).length()) == MASTER) ||
                (getPrefix(number, Integer.toString(AMERICAN_EXPRESS).length()) == AMERICAN_EXPRESS) ||
                (getPrefix(number, Integer.toString(DISCOVER).length()) == DISCOVER);
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



    public static boolean isCvvValid(String cvv) {
        String regex = "[0-9]{3,4}$";
        Pattern p = Pattern.compile(regex);

        if (cvv == null) {
            return false;
        }

        Matcher m = p.matcher(cvv);
        return m.matches();
    }

    public static boolean isExpiryDateValid(String expiryDate, String orderDate){
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("MM/yy");
        YearMonth ym;
        try {
            ym = YearMonth.parse(expiryDate, parser);
        }catch (DateTimeParseException e){
            System.err.println("Invalid expiry date: " + expiryDate);
            return false;
        }
        LocalDate expiry = ym.atEndOfMonth();
        System.out.println(expiry);

        LocalDate order = LocalDate.parse(orderDate);

        return (expiry.isAfter(order) || expiry.isEqual(order));
    }

    public static boolean isCreditCardInfoValid(String cardNumber, String expiryDate, String cvv, String orderDate){
        return isCreditCardNumberValid(cardNumber) && isExpiryDateValid(expiryDate, orderDate) && isCvvValid(cvv);
    }
}
