package com.tensor.tensortest.Web;

import android.util.Log;

import com.tensor.tensortest.MainActivity;
import com.tensor.tensortest.R;
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
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by develop on 23.03.2017.
 */

public class SiteParsing {

    public SiteParsing() {}


    /**
     * Получение полного описания новости
     * @param element - div с новостью
     * @return - возвращаем строку со всеми обзацами новости
     */
    public String getDescription(org.jsoup.nodes.Element element) {
        StringBuilder strBuilder = new StringBuilder();
        Elements select = element.select(".news-block-justify noindex");
        int size = select.get(0).children().size();
        for(int i = 0; i < size; i++) {
            org.jsoup.nodes.Element elem = select.get(0).child(i);
            if(elem.tagName().equals("p")) {
                if(!elem.text().trim().equals("")) {
                    strBuilder
                            .append("     ")
                            .append(elem.text().trim()).append("\n\n");
                }
            } else if(elem.tagName().equals("ul")) {
                for(int k = 0; k < elem.children().size(); k++) {
                    if(!elem.child(k).text().trim().equals("")) {
                        strBuilder
                                .append("     ")
                                .append(MainActivity.getRes().getString(R.string.point))
                                .append("  ")
                                .append(elem.child(k).text().trim())
                                .append("\n");
                    }
                }
                strBuilder.append("\n");
            }
        }
        String result = strBuilder.toString();
        return result;
    }

    /**
     * Получаем список div элементов класса "news-record record_feed_list" с заданной страницы
     */
    public List<org.jsoup.nodes.Element> getNewsDivElements(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(Settings.TAG, "Не удалось открыть страницу");
            return null;
        }

        Elements elements = doc.select(".news-record");
        List<org.jsoup.nodes.Element> result = new ArrayList<>();
        for(org.jsoup.nodes.Element current: elements) {
            result.add(current);
        }
        return result;
    }

    public News parsingDiv(org.jsoup.nodes.Element element) {
        News news = new News();
        //Получение времени добавления новости
        String time = element.select(".news-block-left > span").text();
        news.setPubDate(time);

        //Получение уникального имени
        String name = element.select(".news-record a").attr("name");
        news.setName(name);

        //Получение заголовка
        String title = element.select(".title2").text();
        news.setTitle(title);

        //Получение короткого описания новости
        String shortDescription = element.select(".fotorama_frame_description").text();
        news.setShortDescription(shortDescription);

        //Получение полного описания нвоости
        news.setDescription(getDescription(element));

        //Получение изображения нвоости
        String src = element.select(".img-responsive").attr("src");
        Thread myThready = new Thread(() -> {
            try {
                news.setImage(Settings.bytesFromUrl(src));
                news.setReady(true);
                App.getDataSource().addNews(news);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        myThready.start();

        return news;
    }
}
