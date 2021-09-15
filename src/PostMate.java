import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class PostMate {
    public static void download(String url) throws IOException {
        InputStream inputStream = new URL(url).openStream();
        Files.copy(inputStream, Paths.get(new JFileChooser().getFileSystemView().getDefaultDirectory().toString()+"query.xml"), StandardCopyOption.REPLACE_EXISTING);
    }
    public static void showInformation()
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try{
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new JFileChooser().getFileSystemView().getDefaultDirectory().toString()+"query.xml");
            NodeList notifications = doc.getElementsByTagName("TrackingData");
            if(notifications.getLength()==0)
            {
                System.out.println("Не се пронајдени податоци!");
            }
            else
            {
                for (int i = 0; i < notifications.getLength(); i++) {
                    Node p = notifications.item(i);
                    if(p.getNodeType()==Node.ELEMENT_NODE)
                    {
                        Element data = (Element) p;
                        NodeList podlista = data.getChildNodes();
                        for (int j = 0; j < podlista.getLength(); j++) {
                            Node n = podlista.item(j);
                            if(n.getNodeType()==Node.ELEMENT_NODE)
                            {
                                Element notice = (Element) n;
                                if(notice.getTagName().equals("ID") || notice.getTagName().equals("Begining") || notice.getTagName().equals("End")) continue;
                                if(notice.getTagName().equals("Date"))
                                {
                                    System.out.println("Датум на промена: " + notice.getTextContent());
                                }
                                if(notice.getTagName().equals("Notice"))
                                {
                                    switch (notice.getTextContent()) {
                                        case "Pristignata vo Naizmeni!na po{ta(Vlez)" -> {
                                            System.out.println("Пристигната во наизменична пошта (влез).");
                                            System.out.println("------------------------------------------");
                                        }
                                        case "Pristignata vo Naizmeni!na po{ta(Izlez)" -> {
                                            System.out.println("Пристигната во наизменична пошта (излез).");
                                            System.out.println("------------------------------------------");
                                        }
                                        case "Vo Posta" -> {
                                            System.out.println("Пратката е во пошта!");
                                            System.out.println("------------------------------------------");
                                        }
                                        case "Izvesten-Za isporaka na {alter" -> {
                                            System.out.println("Известен за испорака на шалтер!");
                                            System.out.println("------------------------------------------");
                                        }
                                        case "Ispora~ana" -> {
                                            System.out.println("Пратката е испорачана.");
                                            System.out.println("------------------------------------------");
                                        }
                                        case "Pratkata se prima od Ispra}a~" -> {
                                            System.out.println("Пратката се прима од испраќачот.");
                                            System.out.println("------------------------------------------");
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            }

        }catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String url = br.readLine();
        String urlString = "https://www.posta.com.mk/tnt/api/query?id=" + url;
        download(urlString);
        showInformation();
    }
}


