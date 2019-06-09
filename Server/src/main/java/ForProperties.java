
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ForProperties {

    static int port;
    static String botToken;
    static String botPassword;
    static String beginningOfMessage;
    static String botUsername;

    boolean loadProperties() {



        // Properties file path.
        //---------------------------------------------------------------------

        // for ide
        String filePath = "Server/src/main/resources/config.properties";

        // my way
        //String filePath = "./config.properties";


        Properties prop = new Properties();

        //---------------------------------------------------------------------



        try(InputStream inputStream
                    = new FileInputStream(filePath))  {

            // Loading the properties.
            prop.load(inputStream);


            // Getting properties
            botToken = prop.getProperty("BotToken");
            botPassword = prop.getProperty("BotPassword");

            port = Integer.parseInt(prop.getProperty("Port"));
            System.out.println("Port: " + port);

            beginningOfMessage = prop.getProperty("BeginningOfMessage");

            botUsername = prop.getProperty("BotUsername");
            System.out.println("bot's username: " + botUsername);


        } catch(IOException ex){
            System.out.println("Problem occurs when reading file with properties!");
            ex.printStackTrace();
            return false;
        }

        return true;
    }
}
