
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

    private String receivedStr;

    Client(String ip, int port, String beginningOfTheMessage) {
        System.out.println("Ip: " + ip);
        System.out.println("Port: " + port);

        // TODO stop from command line
        while (true) {
            try (Socket socketCloud = new Socket(ip, port);
                 BufferedReader in = new BufferedReader(new
                         InputStreamReader(socketCloud.getInputStream()));
                 PrintWriter out = new
                         PrintWriter(socketCloud.getOutputStream(), true)) {

                socketCloud.setSoTimeout(2000);

                while (true) {
                    out.println(beginningOfTheMessage);
                    receivedStr = in.readLine();
                    if (receivedStr == null) {
                        throw new IOException();
                    }

                    //System.out.println("received string: " + receivedStr);
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
            } catch (IOException e2) {

            }
        } // close "external" loop
    }
}

