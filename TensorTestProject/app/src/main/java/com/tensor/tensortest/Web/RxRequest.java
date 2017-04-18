package com.tensor.tensortest.Web;

import com.tensor.tensortest.app.App;
import com.tensor.tensortest.beans.News;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by develop on 30.03.2017.
 */

public class RxRequest {

    /**
     * Создаем общий Observable для получения списка со всей информацией о новости уже по ссылке из Link
     * @return - Observable
     */
    public static Observable<News> getNewsObservable() {
        SiteParsing webParsing = new SiteParsing();
        return queryNewsLinks()
                .flatMap((div) -> {
                        //Collections.reverse(news);
                        return Observable.from(div);
                })
                .map(div -> webParsing.parsingDiv(div))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Создаем Observable для получения листа элементов класса "news-record record_feed_list"
     * @return - Observable
     */
    public static Observable<List<org.jsoup.nodes.Element>> queryNewsLinks() {
        SiteParsing webParsing = new SiteParsing();
        return Observable.create( (Observable.OnSubscribe<List<org.jsoup.nodes.Element>>) subscriber -> {
                        subscriber.onNext(webParsing.getNewsDivElements(WebSetting.SITE_URL + "?p=" + Integer.toString(App.getCurrentPage())));
                        subscriber.onCompleted();
                }).subscribeOn(Schedulers.newThread());
    }
}
