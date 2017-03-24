package com.tensor.tensortest.Web;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by develop on 23.03.2017.
 */

public class SiteXmlParsing {

    public SiteXmlParsing() {}

    public List<String> getURLs(String urlSite) {
        List<String> stringList = new ArrayList<>();
        try {
            URL url = new URL(urlSite);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("item");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    stringList.add(getNode("link", element));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        } catch (SAXException e) {
            e.printStackTrace();
            return null;
        }
        return stringList;
    }

    private String getNode(String tag, Element element) {
        NodeList linkNodeList = element.getElementsByTagName(tag).item(0)
                .getChildNodes();
        Node nodeLink = linkNodeList.item(0);
        return nodeLink.getNodeValue();
    }
}
