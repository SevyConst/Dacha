public class NotifyUtils {
    public static void toBoth(String out, MyTelegramBot bot) {
        System.out.println(out);
        if (bot != null) {
            try {
                bot.sendToAll(out);
            } catch (Exception e) {
                System.out.println("exception while trying send message to bot");
            }
        } else {
            System.out.println("bot ref is null in NotifyUtils.toBoth");
        }
    }
}
