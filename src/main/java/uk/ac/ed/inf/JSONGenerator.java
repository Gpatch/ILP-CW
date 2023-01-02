package uk.ac.ed.inf;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.javatuples.Triplet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * A static class used to generate the necessary json files
 *
 */
public class JSONGenerator {
    /**
     * Generates a deliveries-date.json file. Overwrites if already exists
     * @param orders which info is used in generating the json file
     * @param date used to generate a correct file name
     */
    public static void generateDeliveriesJSON(Order[] orders, String date) {
        String fileName = "deliveries-" + date;
        String path = "resultfiles";
        String deliveriesJSON = createDeliveriesJSON(orders);

        createJSONFile(fileName, path);
        writeToJSONFile(fileName, path, deliveriesJSON);
    }
    /**
     * Generates a flightpath-date.json file. Overwrites if already exists
     * @param date used to generate a correct file name
     * @param json string used to be put inside the file, since drone generates the string
     */
    public static void generateFlightPathJSON(String date, String json) {
        String fileName = "flightpath-" + date;
        String path = "resultfiles";

        createJSONFile(fileName, path);
        writeToJSONFile(fileName, path, json);
    }
    /**
     * Generates a geojson-date.json file. Overwrite if already exists
     * @param pathList which locations are used in generating the json file
     * @param date used to generate a correct file name
     */
    public static void generateGeoJSON(ArrayList<LngLat> pathList, String date) {
        String fileName = "drone-" + date;
        String path = "resultfiles";
        String pathJSON = createGeoJSON(pathList);

        createJSONFile(fileName, path);
        writeToJSONFile(fileName, path, pathJSON);
    }


    private static void createJSONFile(String fileName, String path){
        try {
            File myObj = new File(path + "/" + fileName + ".json");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            }
        } catch (IOException e) {
            System.err.println("An error occurred while creating" + fileName + ".json");
            System.exit(ErrorCodes.READ_WRITE_FILE.code);
        }
    }

    private static void writeToJSONFile(String filename, String path, String jsonString){
        try {
            FileWriter myWriter = new FileWriter(path + "/" + filename + ".json");
            myWriter.write(jsonString);
            myWriter.close();
            System.out.println("Successfully written to the " + filename);
        } catch (IOException e) {
            System.err.println("An error occurred while writing to " + filename + ".json");
            System.exit(ErrorCodes.READ_WRITE_FILE.code);
        }
    }

    private static String createDeliveriesJSON(Order[] orders){
        JsonFactory factory = new JsonFactory();
        StringWriter jsonObjectWriter = new StringWriter();
        JsonGenerator generator;

        try{
            generator = factory.createGenerator(jsonObjectWriter);
        } catch (IOException e) {
            System.err.println("Error while initialising JSON generator!");
            return "";
        }
        generator.useDefaultPrettyPrinter();
        try {
            generator.writeStartArray();
            for (Order order : orders) {
                generator.writeStartObject();
                generator.writeFieldName("orderNo");
                generator.writeString(order.getOrderNo());
                generator.writeFieldName("outcome");
                generator.writeString(String.valueOf(order.getOutcome()));
                generator.writeFieldName("costInPence");
                generator.writeNumber(order.getPriceTotalInPence());
                generator.writeEndObject();
            }
            generator.writeEndArray();
            generator.close();
        }catch (IOException e){
            System.err.println("Error while trying to write into deliveries json file!");
            System.exit(ErrorCodes.READ_WRITE_FILE.code);
        }
        return jsonObjectWriter.toString();
    }

    /**
     * Adds a single flightpath step to the flightpath json string
     * @param generator for the json used
     * @param order for which the path is currently being processed
     * @param loc which is the current location of the drone for delivering current order
     * @param ticksSinceStartOfCalculation time elapsed since the last step
     */
    public static void addFlightpathObjectToJSON(JsonGenerator generator, Order order, Triplet<LngLat, Double, LngLat> loc, long ticksSinceStartOfCalculation){
        try {
            generator.writeStartObject();
            generator.writeFieldName("orderNo");
            generator.writeString(order.getOrderNo());
            generator.writeFieldName("fromLongitude");
            generator.writeNumber(loc.getValue2().getCoordinates()[0]);
            generator.writeFieldName("fromLatitude");
            generator.writeNumber(loc.getValue2().getCoordinates()[1]);
            generator.writeFieldName("angle");
            generator.writeNumber(loc.getValue1());
            generator.writeFieldName("toLongitude");
            generator.writeNumber(loc.getValue0().getCoordinates()[0]);
            generator.writeFieldName("toLatitude");
            generator.writeNumber(loc.getValue0().getCoordinates()[1]);
            generator.writeFieldName("ticksSinceStartOfCalculation");
            generator.writeNumber(ticksSinceStartOfCalculation);
            generator.writeEndObject();
        }catch (IOException e) {
            System.err.println("Error while trying to write into flightpath json file!");
            System.exit(ErrorCodes.READ_WRITE_FILE.code);
        }
    }



    private static String createGeoJSON(ArrayList<LngLat> path)  {
        JsonFactory factory = new JsonFactory();
        StringWriter jsonObjectWriter = new StringWriter();

        try {
            JsonGenerator generator = factory.createGenerator(jsonObjectWriter);

            generator.useDefaultPrettyPrinter();
            generator.writeStartObject();
            generator.writeFieldName("type");
            generator.writeString("FeatureCollection");
            generator.writeFieldName("features");
            generator.writeStartArray();
            generator.writeStartObject();
            generator.writeFieldName("type");
            generator.writeString("Feature");
            generator.writeFieldName("properties");
            generator.writeStartObject();
            generator.writeEndObject();
            generator.writeFieldName("geometry");
            generator.writeStartObject();
            generator.writeFieldName("type");
            generator.writeString("LineString");
            generator.writeFieldName("coordinates");
            generator.writeStartArray();
            for (LngLat i : path) {
                generator.writeArray(i.getCoordinates(), 0, 2);
            }
            generator.writeEndArray();
            generator.writeEndObject();
            generator.writeEndObject();
            generator.writeEndArray();
            generator.writeEndObject();
            generator.close();
        }catch (IOException e){
            System.err.println("Error while trying to write into geojson file!");
            System.exit(ErrorCodes.READ_WRITE_FILE.code);
        }
        return jsonObjectWriter.toString();
    }
}
