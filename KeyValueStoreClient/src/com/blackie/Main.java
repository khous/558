package com.blackie;

import java.io.*;
import java.net.Socket;
import java.util.Date;

public class Main {

    public static void main(String[] args) throws IOException {
        String serverAddress = args[0];
        String port = args[1];
        String message = args.length >= 3 ? args[2] : "";

        //Test code for stress test
//        for (int i = 0; i < 100000; i++) {
//            sendMessage(serverAddress, port, i + ":bitches");
//        }
//        for (int i = 0; i < 100000; i++) {
//            sendMessage(serverAddress, port, i + "");
//        }

        sendMessage(serverAddress, port, message);
    }

    /**
     * Send a request to store or retrieve a key
     * @param serverAddress The text address of the server to send the request to. This can be an IP or a domain name
     * @param port The port to send the request to
     * @param message The message to send in the format of key:value to save a value or key to retrieve a value
     * @throws IOException Ain't got time for try catch
     */
    public static void sendMessage (String serverAddress, String port, String message) throws IOException {
        Socket socky = new Socket(serverAddress, Integer.parseInt(port));

        try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socky.getInputStream()));
                PrintWriter out = new PrintWriter(socky.getOutputStream(), true);
        ) {
            String line;
            StringBuilder sb = new StringBuilder();

            line = in.readLine();
//                if (line == null || line.length() == 0) break;
            System.out.println("Requesting\t\t" + message + "\t\tat " + (new Date()).toString());
            out.println(message);
            String response = in.readLine();
            System.out.println("Received\t\t" + message + "\t\tat " + (new Date()).toString());
        }

    }
}
