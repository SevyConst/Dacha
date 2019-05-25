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
            socketPi.setSoTimeout(timeOutMsec);
        } catch (java.net.SocketException e) {
            strException =
                    "Exception Timeout socketPi.setSoTimeout! (very strange)";

            System.out.println(strException);

            if (isConnected) {
                NotifyUtils.toBoth(strException);
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
            NotifyUtils.toBoth("strException");
            return;

        }

        PrintWriter out = null;
        try {
            out = new PrintWriter(
                    socketPi.getOutputStream(),true);
        } catch (IOException e) {
            strException = "Exception in socketPi.getOutputStream()";
            NotifyUtils.toBoth(strException);
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
            NotifyUtils.toBoth(strException);
            return;
        } catch (IOException e) {
            strException =
                    "Exception in in.readLine() " +
                            "(first attempt in the thread)";
            NotifyUtils.toBoth(strException);
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
        NotifyUtils.toBoth("Соединение разорвано");
        NotifyUtils.toBoth("Некоторая информация об разрыве соединения:");
        NotifyUtils.toBoth("null from pi has been received");

        try {

            socketPi.close();
            System.out.println("close socket");
        } catch (Exception e) {
            strException = "socketPi.close()";
            NotifyUtils.toBoth(strException);
        }
    }
}
