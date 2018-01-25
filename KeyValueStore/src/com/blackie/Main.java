package com.blackie;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.openhft.affinity.*;

public class Main {
    /**
     * The port on which to listen
     */
    private static final int PORT = 1337;
    /**
     * The backing datastructure for the key value store
     */
    private static final Map<String, String> map = new HashMap<>();
    private static final int NUM_CORES = 4;

    public static void main(String[] args) throws IOException {

        int port = PORT;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        ServerSocket listener = new ServerSocket(port);

        try {
            int core = 0;
            while (true) {
                try (AffinityLock al = AffinityLock.acquireLock(core)) {
                    // do some work while locked to a CPU.
                    new Server(listener.accept()).start();
                }

                core = (++core) % NUM_CORES;
            }
        } finally {
            listener.close();
        }
    }

    /**
     * The threaded Server
     */
    private static class Server extends Thread {
        private Socket socket;

        Server (Socket socky) {
            socket = socky;
        }

        @Override
        public void run() {
            try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            ) {
                out.println();
                String input = in.readLine();
                System.out.println("Receiving\t\t" + input + "\t\tat " + (new Date()).toString());

                int idxOfColon = input.indexOf(":");
                String output = "key/value saved";
                if (idxOfColon > 0) {
                    String k = input.substring(0, idxOfColon);
                    String v = input.substring(idxOfColon + 1);
                    synchronized (map) {
                        map.put(k, v);
                    }
                } else {
                    synchronized (map) {
                        output = map.getOrDefault(input, "null");
                    }
                }
                System.out.println("Sending\t\t" + output + "\t\tat " + (new Date()).toString());
                out.println(output);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
