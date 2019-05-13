
public class Main {

    public static void main(String[] args) {
        ForProperties config = new ForProperties();
        if (!config.loadProperties()) {
            return;
        }
        System.out.println("file with config has been read successful!");
        System.out.println("------------------------------------------");
        System.out.println("start connecting!");
        Client client = new Client(config.ip, config.port);
        System.out.println("Done!");
    }
}
