package com.tensor.tensortest.Web;

import android.util.Log;

import com.tensor.tensortest.Utils.Settings;
import com.tensor.tensortest.app.App;
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

    /**
     * Получение краткого содержания новостей
     * @param urlSite - сайт откуда берем информацию о новостях
     * @return - возвращаем список новостей с неполной информацией
     */
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
                    news.setShortDescription(getNode("description", element));
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

    /**
     * Получаем все отсальные данные о новости перейдя по ссылке link
     * @param news - новость которую нужно дозаполнить
     * @return - возвращаем новоть которую дополнили информацией
     */
    public News getNews(News news) {
        Thread myThready = new Thread(() -> {
            try {
                Document doc = Jsoup.connect(news.getLink()).get();
                Elements element = doc.select(".img-responsive");
                String src = element.attr("src");
                news.setLinkImage(src);
                news.setImage(Settings.drawableFromUrl(src));
                news.setImageTitle(element.attr("title"));
                news.setDescription(getDescription(doc));
                news.setReady(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        myThready.start();

        return news;
    }

    /**
     * Получение полного описания новости
     * @param doc - страница с новостью которую открыли
     * @return - возвращаем строку со всеми обзацами новости
     */
    public String getDescription(Document doc) {
        StringBuilder strBuilder = new StringBuilder();
        Elements select = doc.select(".js-mediator-article p");
        for(int i = 0; i < select.size(); i++) {
            String str = select.get(i).text().trim();
            if(!str.equals("")) {
                if(i != 0)
                    strBuilder.append("\n\n");
                strBuilder.append("     ").append(str);
            }
        }

        return strBuilder.toString();
    }

    /**
     * Получение содержимого по тегу
     * @param tag - название тега
     * @param element - элемент в котором он содержиться
     * @return - возвращаем содержание
     */
    private String getNode(String tag, Element element) {
        NodeList linkNodeList = element.getElementsByTagName(tag).item(0)
                .getChildNodes();
        Node nodeLink = linkNodeList.item(0);
        return nodeLink.getNodeValue();
    }
}
