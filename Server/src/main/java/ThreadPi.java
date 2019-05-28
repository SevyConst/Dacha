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

    String strException;


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


        try (BufferedReader in = new BufferedReader(new
                InputStreamReader(socketPi.getInputStream()));
             PrintWriter out = new PrintWriter(
                     socketPi.getOutputStream(), true)) {


            //-----------------------------------------------------------
            // Receive and send
            String input = null;
                input = in.readLine();
            while (ForProperties.beginningOfMessage.equals(input)) {
                if (!isConnected) {
                    NotifyUtils.toBoth("Соединение восстановлено");
                    isConnected = true;
                }

                CheckDate.dateLastConnect = new Date();
                if (!CheckDate.firstConnectHappened) {
                    CheckDate.firstConnectHappened = true;
                }

                // TODO: read about multithreading and the one resource
                System.out.println(input);

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
