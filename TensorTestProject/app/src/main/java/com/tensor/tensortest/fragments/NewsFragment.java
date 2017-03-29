package com.tensor.tensortest.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tensor.tensortest.App.App;
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
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
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
    private final int COUNT_RETRY_FOR_REQUEST = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_news, container, false);

        newsRecyclerView = (RecyclerView) view.findViewById(R.id.newsRecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        newsRecyclerView.setLayoutManager(manager);

        adapter = new NewsAdapter();
        newsRecyclerView.setAdapter(adapter);

        swipeRefresher = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeRefresher.setOnRefreshListener(() -> {
                refresh();
                swipeRefresher.setRefreshing(true);
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                List<News> fff = App.getNews();
                Log.d(Settings.TAG, "10 seconds ");
            }
        }, 10000);

        refresh();

        return view;
    }

    public void refresh() {

        SiteXmlParsing webParsing = new SiteXmlParsing();
        Observable<List<String>> myObserveble = Observable.create(
                new Observable.OnSubscribe<List<String>>() {
                      @Override
                      public void call(Subscriber<? super List<String>> subscriber) {
                          subscriber.onNext(webParsing.getNewsLinks(WebSetting.SITE_URL));
                          subscriber.onCompleted();
                      }
                }
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        Subscriber<List<String>> mySubscriber = new Subscriber<List<String>>() {
            @Override
            public void onCompleted() {
                Log.d(Settings.TAG, "Успешно");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(Settings.TAG, "Ошибка: " + e.toString());
            }

            @Override
            public void onNext(List<String> news) {

            }
        };

        myObserveble.subscribe(mySubscriber);
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
            .doOnCompleted(() -> {
                    Log.d(Settings.TAG, "Загрузка новостей завершена");
                    closeLoading();
            })
            .subscribe((urls) -> {
                    Log.d(Settings.TAG, "Ссылки на новости загружены");


                    /*Observable.from(Arrays.asList(urls))
                            .doOnCompleted(() -> {
                                Log.d(Settings.TAG, "Загрузка новостей завершена");
                                adapter.notifyDataSetChanged();
                                closeLoading();
                            })
                            .subscribe((url) -> {
                                queryNews(urls.toString()).subscribe((returnNews) -> {
                                    News news = (News) returnNews;
                                    App.getNews().add(news);
                                    Log.d(Settings.TAG, "Новость: " + url.toString());
                                });
                            }, (throwable) -> {
                                throwable.printStackTrace();
                                Log.d(Settings.TAG, "Ошибка принятия новости");
                            });*/

                    //List<News> news = (List<News>)(urls);
                    //Log.d(Settings.TAG, "Вернулся список: " + Integer.toString(news.size()));
//
//
                    //swipeRefresher.setRefreshing(false);
                    //((MainActivity) getActivity()).setToolbarTitle(R.string.news);

            }, (throwable) -> {
                throwable.printStackTrace();
                Log.d(Settings.TAG, "Общая ошибка");
            });
    }

    private void closeLoading() {
        swipeRefresher.setRefreshing(false);
        ((MainActivity) getActivity()).setToolbarTitle(R.string.news);
    }


    /*private Observable<News> queryNews(String urls) {
        SiteXmlParsing webParsing = new SiteXmlParsing();

        return Observable.create(new Observable.OnSubscribe<News>() {
            @Override
            public void call(Subscriber<? super News> subscriber) {
                subscriber.onNext(webParsing.getNews(urls));
                subscriber.onError(new Throwable());
                subscriber.onCompleted();
            }

        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }*/

    private Func1<Throwable, Observable<? extends String>> createRequestErrorHandler() {
        return (throwable) -> null;
    }

}
