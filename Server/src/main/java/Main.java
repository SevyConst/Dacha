public class Main {
    public static void main(String[] args) {

        ForProperties config = new ForProperties();
        if (!config.loadProperties()) {
            return;
        }
        System.out.println("file with config has been read successful!");
        System.out.println("------------------------------------------");

        // create bot
        ForBot forBot = new ForBot();

        // creating thread for watching ping from socket-client
        CheckDate checkDate = new CheckDate("Second check", forBot.bot);

        MultiServerPi multiServerPi = new MultiServerPi(forBot.bot);
    }
}