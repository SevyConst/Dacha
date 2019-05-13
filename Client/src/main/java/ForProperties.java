
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ForProperties {

    String ip;
    int port;

    boolean loadProperties() {



        // Properties file path.
        //---------------------------------------------------------------------

        // for ide
        String filePath = "Client/src/main/resources/config.properties";

        // for jar
        //String filePath = "../src/main/resources/config.properties";

        Properties prop=new Properties();

        //---------------------------------------------------------------------



        try(InputStream inputStream
                    = new FileInputStream(filePath))  {

            // Loading the properties.
            prop.load(inputStream);


            // Getting properties
            ip = prop.getProperty("ip");
            port = Integer.parseInt(prop.getProperty("Port"));


            System.out.println("ip = " + ip);
            System.out.println("Port = " + port);

        } catch(IOException ex){
            System.out.println("Problem occurs when reading file !");
            ex.printStackTrace();
            return false;
        }

        return true;
    }
}
