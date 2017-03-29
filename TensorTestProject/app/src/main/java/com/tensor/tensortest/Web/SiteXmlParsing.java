package com.tensor.tensortest.Web;

import android.os.AsyncTask;
import android.util.Log;

import com.tensor.tensortest.App.App;
import com.tensor.tensortest.Utils.Settings;
import com.tensor.tensortest.beans.News;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
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

    public List<String> getNewsLinks(String urlSite) {
        List<String> stringList = new ArrayList<>();
        try {
            URL url = new URL(urlSite);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            org.w3c.dom.Document doc = db.parse(new InputSource(url.openStream()));
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("item");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    stringList.add(getNode("link", element));
                    //new Task().execute(getNode("link", element));
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

    public void getNews(String url) {
        new Task().execute(url);
    }

    class Task extends AsyncTask<String, Void, News> {

        @Override
        protected News doInBackground(String... strings) {
            News currentNews = null;
            try {
                Document doc = Jsoup.connect(strings[0]).get();
                Elements select = doc.select(".title2");
                currentNews = new News(select.text());
                Log.d(Settings.TAG, "Все хорошо");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return currentNews;
        }

        @Override
        protected void onPostExecute(News news) {
            super.onPostExecute(news);
            App.getNews().add(news);
        }
    }



    private String getNode(String tag, Element element) {
        NodeList linkNodeList = element.getElementsByTagName(tag).item(0)
                .getChildNodes();
        Node nodeLink = linkNodeList.item(0);
        return nodeLink.getNodeValue();
    }
}
