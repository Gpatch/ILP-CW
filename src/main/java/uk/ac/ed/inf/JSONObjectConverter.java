package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

/**
 * A static class used to convert JSON to object of some class type
 */
public class JSONObjectConverter {

    /**
     * Converts JSON string to a class of type T
     * @param json string from which to be converted
     * @param type of the class to which to be converted
     * @param <T> type of the specified class
     * @return object converted from json to class of type T
     */
    public static <T> T convertFromJSON(String json, Class<T> type){
        T obj = null;
        ObjectMapper mapper = new ObjectMapper();
        try{
            obj = mapper.readValue(json, type);
        }catch (IOException e){
            System.err.println("Error while converting from json to class!");
            System.exit(ErrorCodes.READ_WRITE_FILE.code);
        }
        return obj;
    }
}
