
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
            prop.load(inputStream);




            // Get properties about telegram bot
            //-----------------------------------------------------------------

            // The necessary parameters for any telegram bot
            botToken = prop.getProperty("BotToken");
            botUsername = prop.getProperty("BotUsername");
            System.out.println("bot's username: " + botUsername);

            // Parameters for my telegram bot
            botPassword = prop.getProperty("BotPassword");



            // Get properties about connection with Dacha (with raspberry pi)
            //-----------------------------------------------------------------

            port = Integer.parseInt(prop.getProperty("Port"));
            System.out.println("Port: " + port);

            // password for connection with Dacha
            beginningOfMessage = prop.getProperty("BeginningOfMessage");



        } catch(IOException ex){
            System.out.println("Problem occurs when reading file with properties!");
            ex.printStackTrace();
            return false;
        }

        return true;
    }
}
