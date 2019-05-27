/*
Class for observing ping from socket-client
 */

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CheckDate implements Runnable {

    private final int DEFAULT_SEC = 10;
    private final int TURBO_SEC = 1;  // period ping in the turbomode
    private int sleepSec = DEFAULT_SEC;

    private double COEF = 2;

    // dateLastConnect must be not null
    public static volatile boolean firstConnectHappened = false;

    public static volatile Date dateLastConnect;

    public static volatile Date dateNextToLastConnect;

    private boolean warningSent = false;

    CheckDate(String name) {
        Thread thread = new Thread(this, name);
        thread.start();
    }

    @Override
    public void run() {
        while(ForConsole.TryingConnectPi) {
            connect();
        }
    }

    private void connect() {

        sleepSec = MultiServerPi.turboMode ?
                TURBO_SEC : DEFAULT_SEC;

        if (firstConnectHappened) {
            Date currentDate = new Date();
            if (currentDate.getTime() < dateLastConnect.getTime()) {
                ForBot.bot.sendToAll("" +
                        "currentDate.getTime() < dateLastConnect.getTime()");
                formatAndSendTwoDates(new Date());

            } else {
                int sleepMilliSec = 1000 * sleepSec;

                double limit = dateLastConnect.getTime()
                        + sleepMilliSec * COEF;

                boolean limitExceeded = limit < currentDate.getTime();

                if (limitExceeded) {
                    if (!warningSent) {

                        String str = "Проверка: соединение разорвано!" +
                                "Ниже некоторая служебная информация";
                        NotifyUtils.toBoth(str);
                        formatAndSendTwoDates(currentDate);

                        str =
                                "CheckDate: The limit has been exceeded";
                        NotifyUtils.toBoth(str);
                        
                        warningSent = true;
                    }
                } else {
                    if (warningSent) {
                        String str =
                                "Проверка: соединение восстановленно!";
                        NotifyUtils.toBoth(str);
                        warningSent = false;
                    }
                }

                dateNextToLastConnect = dateLastConnect;
            }
        }
        try {
            TimeUnit.SECONDS.sleep(sleepSec);
        } catch (InterruptedException e){
            String outStr = "CheckDate: Exception while sleeping";
            NotifyUtils.toBoth(outStr);
            formatAndSendTwoDates(new Date());
        }
    }

    // send date dateLastConnect and currentDate
    private void formatAndSendTwoDates(Date currentDate) {
        DateFormat dateFormat = new SimpleDateFormat(
                "E, MMM dd yyyy HH:mm:ss");

        String dateStr;
        String outStr;

        if (null == dateNextToLastConnect) {
            outStr = "Время предпоследнего соединения отсутствует";
            NotifyUtils.toBoth(outStr);
        }
        else {
            dateStr = dateFormat.format(dateNextToLastConnect);
            outStr = "Время предпоследнего соединения: " + dateStr;
            NotifyUtils.toBoth(outStr);
        }

        dateStr = dateFormat.format(dateLastConnect);
        outStr = "Время последнего соединения: " +
                dateStr;
        NotifyUtils.toBoth(outStr);

        dateStr = dateFormat.format(currentDate);
        outStr = "Текущее время: " + dateStr;
        NotifyUtils.toBoth(outStr);
    }
}
