import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MultiServerPi {

    // Timeout for Pi connection in milliseconds
    // TODO: change by option in telegram?
    private int timeOutMsec = 15 * 1000;

    static volatile boolean turboMode = false;

    public MultiServerPi() {
        while (ForConsole.TryingConnectPi) {
            try (ServerSocket serverSocketPi =
                         new ServerSocket(ForProperties.port)) {

                serverSocketPi.setSoTimeout(timeOutMsec);

                while (ForConsole.TryingConnectPi) {
                    try {
                        Socket socketPi = serverSocketPi.accept();
                        if (socketPi != null) {
                            new ThreadPi(socketPi,
                                    timeOutMsec);
                        }
                    } catch (SocketTimeoutException e) {}
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
