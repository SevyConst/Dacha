package main.java.com.company;

public class Main {

    public static void main(String[] args) {
        ForProperties config = new ForProperties();
        if (!config.loadProperties()) {
            return;
        }

        Client client = new Client(config.ip, config.port);
        System.out.println("Done!");
    }
}
