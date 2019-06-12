import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;

class ThreadPi implements Runnable{

    Thread thread;

    private Socket socketPi;
    private int timeOutMsec;

    private boolean isConnected;

    public ThreadPi(final Socket socketPi,
                    final int timeoutMsec) {
        this.socketPi = socketPi;
        this.timeOutMsec = timeoutMsec;

        thread = new Thread(this);
        thread.start();
    }


    @Override
    public void run() {

        try {
            socketPi.setSoTimeout(timeOutMsec);
        } catch (SocketException e) {
            try {
                socketPi.close();
            } catch (IOException e2) {
                e.printStackTrace();
            } finally {
                return;
            }
        }

        // the main activity
        recieveAndSend();

    }


    // - Receive message from raspberry pi (from Dacha);
    // - Make sure that this is the message that we are waiting for;
    // - Send answer to pi;
    private void recieveAndSend() {
        try (BufferedReader in = new BufferedReader(new
                InputStreamReader(socketPi.getInputStream()));
             PrintWriter out = new PrintWriter(
                     socketPi.getOutputStream(), true)) {

            // Receive message
            String input = null;
            input = in.readLine();

            // Make sure that this is the message that we are waiting for
            //
            // the variable was named beginningOfMessage (not a message)
            // because I will add some information to message later (TODO)
            // Also some information about current state of pi as
            // temperature, free memory and etc.
            //
            // variable beginningOfMessage is
            // the password for connection with Dacha
            while (ForProperties.beginningOfMessage.equals(input)) {
                if (!isConnected) {
                    isConnected = true;
                }

                CheckDate.dateLastConnect = new Date();
                if (!CheckDate.firstConnectHappened) {
                    CheckDate.firstConnectHappened = true;
                }

                // TODO: read about multithreading and the one resource
                // (System.out is the one resource)
                System.out.println(input);

                // send answer to pi
                if (MultiServerPi.turboMode) {
                    out.println("turboModeOn");
                } else {
                    out.println("turboModeOff");
                }

                input = in.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socketPi.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
