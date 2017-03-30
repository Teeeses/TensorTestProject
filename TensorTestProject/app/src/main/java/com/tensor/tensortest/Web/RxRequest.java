package com.tensor.tensortest.Web;

import com.tensor.tensortest.beans.News;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by develop on 30.03.2017.
 */

public class RxRequest {


    public static Observable<News> getNewsObservable() {
        SiteParsing webParsing = new SiteParsing();
        return Observable.create(
                new Observable.OnSubscribe<News>() {
                    @Override
                    public void call(Subscriber<? super News> subscriber) {

                        List<News> news = webParsing.getNewsLinks(WebSetting.SITE_URL);
                        for(News current: news) {
                            subscriber.onNext(webParsing.getNews(current));
                        }
                        subscriber.onCompleted();
                    }
                }
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}