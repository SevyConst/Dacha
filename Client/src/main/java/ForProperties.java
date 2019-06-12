
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ForProperties {

    String ip;
    int port;
    String beginningOfTheMessage;

    boolean loadProperties() {



        // Properties file path.
        //---------------------------------------------------------------------

        // for ide
        String filePath = "Client/src/main/resources/config.properties";

        // my way
        //String filePath = "/home/pi/DachaClient/config.properties";

        Properties prop = new Properties();

        //---------------------------------------------------------------------



        try(InputStream inputStream
                    = new FileInputStream(filePath))  {

            // Loading the properties.
            prop.load(inputStream);


            // Getting properties
            ip = prop.getProperty("IP");
            port = Integer.parseInt(prop.getProperty("Port"));
            beginningOfTheMessage = prop.getProperty("BeginningOfMessage");

            System.out.println("ip = " + ip);
            System.out.println("Port = " + port);

        } catch(IOException ex){
            System.out.println("Problem occurs when reading file with properties!");
            ex.printStackTrace();
            return false;
        }

        return true;
    }
}
