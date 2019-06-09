import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class NotifyUtils {
    // for inability of creating instance of NotifyUtils class
    private NotifyUtils() {
    }

    public static void toBoth(String out) {
        System.out.println(out);
        if (ForBot.bot != null) {
            try {
                ForBot.bot.sendToAll(out);
            } catch (Exception e) {
                System.out.println("exception while trying send message to bot");
            }
        } else {
            System.out.println("bot ref is null in NotifyUtils.toBoth");
        }
    }

    private static final String patternDate = "HH:mm:ss dd/MM";
    public static String getStringDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(patternDate);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+3"));
        return dateFormat.format(date);
    }
}
