package com.inria.testclient.simpleclientdbus;

import com.inria.testserver.simpledbusserver.ISimpleBus;
import com.inria.testserver.simpledbusserver.SimpleBusServer;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.exceptions.DBusException;

/**
 *
 * @author cgourdin
 */
public class SimpleClient {

    public static void main(String[] args) {

        DBusConnection client = null;

        try {
            client = DBusConnection.getConnection(DBusConnection.SESSION);

            // Get the remote object
            ISimpleBus server = client.getRemoteObject("com.inria.simplebus", "/Main", ISimpleBus.class);

            if (server == null) {
                System.out.println("null object returned");
                System.exit(0);
            }
            String buffer = "firstTest";
            // Call methods on it
            String result = server.testStringBuffer(buffer);

            System.out.println("Result buffer: " + result);
            String filename = "";
            String absPath = new File(".").getAbsolutePath();
            try {
                filename = absPath + "/schema.xml";
                // All ok, we make with a file > 16 kB.
                InputStream in = new FileInputStream(filename);
                buffer = setAndGetSchema(in);
                // Send buffer to server:
                result = server.testStringBuffer(buffer);
                System.out.println(result);
                System.out.println("---------------------------------");
                filename = absPath + "/docker-schema.xml";
                in = new FileInputStream(filename);
                buffer = setAndGetSchema(in);
                // Send buffer.
                result = server.testStringBuffer(buffer);
                System.out.println(result);
                System.out.println("---------------------------------");
                System.out.println("---------------------------------");
                // Get propertie schema from server.
                buffer = server.Get("", "schema");
                System.out.println("Get() property called, ");
                System.out.println("buffer :-->  " + buffer);
                
                
            } catch (FileNotFoundException ex) {
                System.out.println("File: " + filename + " not found !!! " + ex.getMessage());
            }
            //dataTransfer.sendDataIn("", "", 0, new byte[2]);
        } catch (DBusException e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                System.out.println("disconnecting client");
                client.disconnect();
            }
        }

    }

    public static String setAndGetSchema(InputStream in) {
        if (in == null) {
            return "no inputstream";
        }
        ByteArrayOutputStream os = null;
        String fileContent;
        try {
            os = new ByteArrayOutputStream();
            fileContent = Utils.copyStream(in, os);
            // logger.info("Schema returned: " + this.schema);

            // this.schema = os.toString("UTF-8");
        } catch (IOException e) {
            Utils.closeQuietly(in);
            Utils.closeQuietly(os);
            fileContent = null;
        }
        return fileContent;
    }

}
