import java.io.Console;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class MultiServerPi {

    // Timeout for Pi connection in milliseconds
    // TODO: change by option in telegram?
    private int timeOutMsec = 15 * 1000;

    static volatile boolean turboMode = false;

    public MultiServerPi() {

        ServerSocket serverSocketPi;
        try {
            serverSocketPi = new ServerSocket(ForProperties.port);
        } catch (IOException e) {
            System.out.println("Couldn't listen to port" + ForProperties.port);
            return;
        }

        Socket socketPi = null;

        while(ForConsole.TryingConnectPi) {
            try {
                socketPi = serverSocketPi.accept();
                if (socketPi != null) {
                    new ThreadPi(socketPi,
                            timeOutMsec);
                }

            } catch (SocketTimeoutException e) {
                System.out.println(
                        "SocketTimeoutException while serverSocketPi.accept()");
            } catch (IOException e) {
                System.out.println(
                        "exception in serverSocketPi.accept()");
            }
        }
    }

}
