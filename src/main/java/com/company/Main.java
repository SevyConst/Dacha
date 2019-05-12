package main.java.com.company;

public class Main {

    public static void main(String[] args) {
        ForProperties config = new ForProperties();
        if (!config.loadProperties()) {
            return;
        }
        System.out.println("Done!");
    }
}
