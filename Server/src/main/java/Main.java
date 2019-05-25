public class Main {
    public static void main(String[] args) {

        ForProperties config = new ForProperties();
        if (!config.loadProperties()) {
            return;
        }
        System.out.println("file with config has been read successful!");
        System.out.println("------------------------------------------");

        if(!ForChatIDs.readPreviousChatIds()) {
            return;
        }

        // create bot
        if (!ForBot.startBot()) {
            System.out.println("Can't start bot");
            return;
        }
        System.out.println("Bot Started!");

        // creating thread for watching ping from socket-client
        CheckDate checkDate = new CheckDate("Second check");

        MultiServerPi multiServerPi = new MultiServerPi();
    }
}