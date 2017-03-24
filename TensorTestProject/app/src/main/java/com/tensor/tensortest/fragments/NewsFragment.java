package com.tensor.tensortest.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tensor.tensortest.R;
import com.tensor.tensortest.Utils.Settings;
import com.tensor.tensortest.Web.SiteXmlParsing;
import com.tensor.tensortest.Web.WebSetting;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by develop on 23.03.2017.
 */

public class NewsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_news, container, false);

        queryURLs(WebSetting.SITE_URL)
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> links) {
                        Observable.from(links)
                            .subscribe(new Action1<String>() {
                                @Override
                                public void call(String link) {
                                    Log.d(Settings.TAG, "Link: " + link);
                                }
                            });
                    }
                });

        return view;
    }

    /**
     * Обновление новостного листа
     */
    public void updateNewsList() {

    }

    private Observable<List<String>> queryURLs(String link) {
        SiteXmlParsing webParsing = new SiteXmlParsing();
        return Observable.create(
                new Observable.OnSubscribe<List<String>>() {
                    @Override
                    public void call(Subscriber<? super List<String>> subscriber) {
                        subscriber.onNext(webParsing.getURLs(link));
                        subscriber.onCompleted();
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
