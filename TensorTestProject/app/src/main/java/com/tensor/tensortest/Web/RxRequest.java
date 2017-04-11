package com.tensor.tensortest.Web;

import com.tensor.tensortest.beans.News;

import java.util.Collections;
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
                .flatMap((news) -> {
                        Collections.reverse(news);
                        return Observable.from(news);
                })
                .map(news -> webParsing.getNews(news))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Создаем Observable для получения списка новостей из xml
     * @return - Observable
     */
    public static Observable<List<News>> queryNewsLinks() {
        SiteParsing webParsing = new SiteParsing();
        return Observable.create( (Observable.OnSubscribe<List<News>>) subscriber -> {
                        subscriber.onNext(webParsing.getNewsLinks(WebSetting.SITE_URL));
                        subscriber.onCompleted();
                }).subscribeOn(Schedulers.newThread());
    }
}
