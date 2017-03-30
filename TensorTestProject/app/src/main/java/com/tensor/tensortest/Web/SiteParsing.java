package com.tensor.tensortest.Web;

import com.tensor.tensortest.App.App;
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

public class SiteParsing {

    public SiteParsing() {}

    public List<News> getNewsLinks(String urlSite) {
        List<News> newsList = new ArrayList<>();
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
                    News news = new News();
                    Element element = (Element) node;
                    String datePublicNews = getNode("pubDate", element);
                    /**
                     * Проверка: если дата пуцбликации текущей добавляемой новости совпадает с датой публикации самой последней новости(первая в списке),
                     * то значит все новые новости пройдены, поэтому выходим
                     */
                    if(App.getNews().size() != 0 && datePublicNews.equals(App.getNews().get(0).getPubDate())) {
                        return newsList;
                    }
                    news.setLink(getNode("link", element));
                    news.setTitle(getNode("title", element));
                    news.setShort_description(getNode("description", element));
                    news.setPubDate(getNode("pubDate", element));
                    newsList.add(news);
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
        return newsList;
    }

    public News getNews(News news) {
        try {
            Document doc = Jsoup.connect(news.getLink()).get();
            Elements select = doc.select(".title2");
            news.setDescription(select.text());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return news;
    }


    private String getNode(String tag, Element element) {
        NodeList linkNodeList = element.getElementsByTagName(tag).item(0)
                .getChildNodes();
        Node nodeLink = linkNodeList.item(0);
        return nodeLink.getNodeValue();
    }
}
