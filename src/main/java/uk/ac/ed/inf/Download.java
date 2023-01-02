package uk.ac.ed.inf;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * A static class which serves a purposed of download static data files form the rest server and store locally
 */
public class Download {
    /**
     * Creates staticfiles folder in the root directory, and downloads the specified file from the rest server
     * to the folder
     * @param baseServerAddress of the rest server from which data is retrieved
     * @param file the file to be downloaded as well as the endpoint of the server
     * @param format the file extension to be used
     */
    public static void download(String baseServerAddress, String file, String format){
        URL finalUrl = null;
        try {
            finalUrl = new URL(baseServerAddress + file);
        } catch (MalformedURLException e ) {
            System.err.println("URL is invalid: " + baseServerAddress + file);
            System.exit(ErrorCodes.INVALID_URL.code);
        }

        try {
            Files.createDirectories(Paths.get("staticfiles"));
            BufferedInputStream in = new BufferedInputStream(finalUrl.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream("staticfiles/" + file + "." + format ,false);
            byte[] dataBuffer = new byte[4096];
            int bytesRead ;
            while ((bytesRead = in.read(dataBuffer , 0, 1024)) != -1) {
                fileOutputStream . write ( dataBuffer , 0 , bytesRead ) ;
            }
            System.out.println("Successfully created " + file + "." + format);
        }catch ( IOException e ) {
            System.err.println("Error while writing to " + file + "." + format);
            System.exit(ErrorCodes.READ_WRITE_FILE.code);
        }

    }
}
