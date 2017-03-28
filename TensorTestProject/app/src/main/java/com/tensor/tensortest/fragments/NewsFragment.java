package com.tensor.tensortest.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tensor.tensortest.MainActivity;
import com.tensor.tensortest.R;
import com.tensor.tensortest.Utils.Settings;
import com.tensor.tensortest.Web.SiteXmlParsing;
import com.tensor.tensortest.Web.WebSetting;
import com.tensor.tensortest.adapters.NewsAdapter;
import com.tensor.tensortest.beans.News;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by develop on 23.03.2017.
 */

public class NewsFragment extends Fragment {

    private RecyclerView newsRecyclerView;
    private NewsAdapter adapter;

    private SwipeRefreshLayout swipeRefresher;

    private final int TIMEOUT_IN_SECONDS = 5;
    private final int COUNT_RETRY_FOR_REQUEST = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_news, container, false);

        newsRecyclerView = (RecyclerView) view.findViewById(R.id.newsRecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        newsRecyclerView.setLayoutManager(manager);


        swipeRefresher = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeRefresher.setOnRefreshListener(() -> {
                refreshNewsList();
                swipeRefresher.setRefreshing(true);
        });

        refreshNewsList();

        return view;
    }

    /**
     * Обновление новостного листа
     */
    public void refreshNewsList() {
        ((MainActivity)getActivity()).setToolbarTitle(R.string.loading);

        SiteXmlParsing webParsing = new SiteXmlParsing();

        Observable.create((subscriber) -> {
                subscriber.onNext(webParsing.getNewsLinks(WebSetting.SITE_URL));
                subscriber.onCompleted();
            })
            .timeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .retry(COUNT_RETRY_FOR_REQUEST) //попытки запроса
            .onErrorResumeNext(createRequestErrorHandler())
            .subscribeOn(Schedulers.newThread())
            .subscribe((urls) -> {
                    Observable.from(Arrays.asList(urls))
                            .subscribe((url) -> {
                                queryNews(urls.toString()).subscribe((listNews) -> {

                                    //adapter = new NewsAdapter(listNews);
                                    //newsRecyclerView.setAdapter(adapter);

                                    swipeRefresher.setRefreshing(false);
                                    ((MainActivity) getActivity()).setToolbarTitle(R.string.news);
                                });
                            }, (throwable) -> {
                                throwable.printStackTrace();
                                Log.d(Settings.TAG, "Ошибка принятия новости");
                            });

            }, (throwable) -> {
                throwable.printStackTrace();
                Log.d(Settings.TAG, "Общая ошибка");
            });
    }


    private Observable<News> queryNews(String urls) {
        SiteXmlParsing webParsing = new SiteXmlParsing();

        return Observable.create(new Observable.OnSubscribe<News>() {
            @Override
            public void call(Subscriber<? super News> subscriber) {
                subscriber.onNext(webParsing.getNews(urls));
                subscriber.onError(new Throwable());
                subscriber.onCompleted();
            }

        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private Func1<Throwable, Observable<? extends String>> createRequestErrorHandler() {
        return (throwable) -> null;
    }

}
