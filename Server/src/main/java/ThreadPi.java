import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

class ThreadPi implements Runnable{

    Thread thrd;

    private Socket socketPi;
    private MyTelegramBot bot;
    private int timeOutMsec;

    private boolean isConnected;

    String strException;


    public ThreadPi(final Socket socketPi,
                    final MyTelegramBot bot,
                    final int timeoutMsec) {
        this.socketPi = socketPi;
        this.bot = bot;
        this.timeOutMsec = timeoutMsec;

        thrd = new Thread(this);
        thrd.start();
    }


    @Override
    public void run() {
        try {
            socketPi.setSoTimeout(timeOutMsec);
        } catch (java.net.SocketException e) {
            strException =
                    "Exception Timeout socketPi.setSoTimeout! (very strange)";

            System.out.println(strException);

            if (isConnected) {
                NotifyUtils.toBoth(strException, bot);
            }

            return;
        }
        System.out.println("Client connected");

        // TODO: add some of these objects
        // to the struct in the ArrayList for threads?
        BufferedReader in = null;
        try {
            in = new BufferedReader(new
                    InputStreamReader(socketPi.getInputStream()));
        } catch (IOException e) {
            strException = "Exception in socketPi.getInputStream()";
            NotifyUtils.toBoth("strException", bot);
            return;

        }

        PrintWriter out = null;
        try {
            out = new PrintWriter(
                    socketPi.getOutputStream(),true);
        } catch (IOException e) {
            strException = "Exception in socketPi.getOutputStream()";
            NotifyUtils.toBoth(strException, bot);
            return;
        }



        //-----------------------------------------------------------
        // Receive and send
        String input = null;
        try {
            input = in.readLine();
        } catch (java.net.SocketTimeoutException e) {
            strException =
                    "Exception in in.readLine() " +
                            "Timeout!" +
                            " (first attempt in the thread)";
            NotifyUtils.toBoth(strException, bot);
            return;
        } catch (IOException e) {
            strException =
                    "Exception in in.readLine() " +
                            "(first attempt in the thread)";
            NotifyUtils.toBoth(strException, bot);
            return;
        }
        while (input != null) {
            if (!isConnected) {
                NotifyUtils.toBoth("Соединение восстановлено", bot);
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
                NotifyUtils.toBoth(strException, bot);
                return;
            } catch (IOException e) {
                strException =
                        "Exception in in.readLine()";
                NotifyUtils.toBoth("strException", bot);
                return;
            }
        }
        NotifyUtils.toBoth("null from pi has been received", bot);

        try {

            socketPi.close();
            System.out.println("close socket");
        } catch (Exception e) {
            strException = "socketPi.close()";
            NotifyUtils.toBoth(strException, bot);
        }
    }
}
