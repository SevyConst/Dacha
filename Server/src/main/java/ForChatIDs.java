import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/*
 *
 * TODO: substitute XML with database
 *  for simultaneous handling some users of telegram bot
 *
 */

public class ForChatIDs {

    static List<Long> chatIdList = new ArrayList<>();

    static Document doc;
    static DocumentBuilder docBuilder;

    private static final String FILE_NAME = "ChatIds.xml";
    private static final String ROOT_ELEMENT = "ChatIDs";
    private static final String CHAT_ID_TAG = "ChatId";

    static boolean readPreviousChatIds() {
        try {
            File fXmlFile = new File(FILE_NAME);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            docBuilder = dbFactory.newDocumentBuilder();
            doc = docBuilder.parse(fXmlFile);

            NodeList nodeList = doc.getElementsByTagName(CHAT_ID_TAG);

            doc.getDocumentElement().normalize();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) node;
                    String chatIdStr = eElement.getTextContent();
                    chatIdList.add(Long.parseLong(chatIdStr));
                }
            }

        } catch (FileNotFoundException fileNotFoundException){
            return createFile();
        } catch (Exception e) {
            System.out.println("Some strange exception while reading file" +
                    " (not FileNotFoundException)");
            return false;
        }
        return true;
    }

    private static boolean createFile() {
        try {

            // root elements
            doc = docBuilder.newDocument();
            Element rootElement = doc.createElement(ROOT_ELEMENT);
            doc.appendChild(rootElement);


            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(FILE_NAME));

            transformer.transform(source, result);

            System.out.println("File saved!");

        } catch (TransformerException tfe) {
            tfe.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean addChatId(Long chatId) {
        try {

            Node company = doc.getFirstChild();

            Element newChatId = doc.createElement(CHAT_ID_TAG);
            newChatId.appendChild(doc.createTextNode(Long.toString(chatId)));
            company.appendChild(newChatId);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(FILE_NAME));

            transformer.transform(source, result);


        } catch (TransformerException tfe) {
            tfe.printStackTrace();
            return false;
        }

        chatIdList.add(chatId);
        return true;
    }

    // TODO: write!
//    public static boolean removeChatId(Long chatId) {
//        return true;
//    }
}

