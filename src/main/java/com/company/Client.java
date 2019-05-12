package main.java.com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

class Client {

    private final int PERIOD_PING_DEFAULT_SEC = 10;
    private final int PERIOD_PING_TURBO_SEC = 1;

    private int periodPingSec = PERIOD_PING_DEFAULT_SEC;

    String SentStr = "ping";

    private String receivedStr;

    private Socket socketCloud;
    private PrintWriter out;
    private BufferedReader in;

    Client(String ip, int port) {
        System.out.println("Ip: " + ip);
        System.out.println("Port: " + port);

        while (true) {
            try {
                socketCloud = new Socket(ip, port);

                in = new BufferedReader(new
                        InputStreamReader(socketCloud.getInputStream()));

                out = new
                        PrintWriter(socketCloud.getOutputStream(), true);

                while (true) {
                    out.println(SentStr);
                    receivedStr = in.readLine();
                    if (receivedStr == null) {
                        throw new IOException();
                    }

                    System.out.println("received string: " + receivedStr);
                    switch (receivedStr) {
                        case "turboModeOn":
                            periodPingSec = PERIOD_PING_TURBO_SEC;
                            break;
                        case "turboModeOff":
                            periodPingSec = PERIOD_PING_DEFAULT_SEC;
                            break;
                        default:
                    }
                    TimeUnit.SECONDS.sleep(periodPingSec);
                }


                // Attempt of reconnection after handling these exceptions
            } catch (InterruptedException e1) {
                // TODO: output for client
                closeSockets();
            } catch (IOException e2) {
                closeSockets();
            }
        } // close "external" loop
    }

    // Attempt of reconnection after execution of this method - closeSockets()
    private void closeSockets() {
        try {
            in.close();
            out.close();
            socketCloud.close();
            TimeUnit.SECONDS.sleep(periodPingSec);
        } catch (Exception e) {
            System.out.println("Error in closing sockets");
            try {
                TimeUnit.SECONDS.sleep(periodPingSec);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }
}

