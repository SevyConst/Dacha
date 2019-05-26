import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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
            try {
                socketPi.setSoTimeout(timeOutMsec);
            } catch (java.net.SocketException e) {
                strException =
                        "Exception Timeout socketPi.setSoTimeout!";

                System.out.println(strException);

                if (isConnected) {
                    NotifyUtils.toBoth(strException);
                }

                return;
            }

            // TODO: add some of these objects
            // to the struct in the ArrayList for threads?

            try (BufferedReader in = new BufferedReader(new
                    InputStreamReader(socketPi.getInputStream()));
                 PrintWriter out = new PrintWriter(
                         socketPi.getOutputStream(), true)) {


                //-----------------------------------------------------------
                // Receive and send
                String input = null;
                try {
                    input = in.readLine();
                } catch (java.net.SocketTimeoutException e) {
                    e.printStackTrace();
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
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

                    try {
                        input = in.readLine();
                    } catch (java.net.SocketTimeoutException e) {
                        strException =
                                "Exception in in.readLine() " +
                                        "Timeout!";
                        NotifyUtils.toBoth(strException);
                        return;
                    } catch (IOException e) {
                        strException =
                                "Exception in in.readLine()";
                        NotifyUtils.toBoth("strException");
                        return;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                socketPi.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
