package com.tensor.tensortest.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tensor.tensortest.MainActivity;
import com.tensor.tensortest.R;
import com.tensor.tensortest.Web.SiteXmlParsing;
import com.tensor.tensortest.Web.WebSetting;
import com.tensor.tensortest.adapters.NewsAdapter;

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

    private RecyclerView newsRecyclerView;
    private NewsAdapter adapter;

    private SwipeRefreshLayout swipeRefresher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_news, container, false);

        newsRecyclerView = (RecyclerView) view.findViewById(R.id.newsRecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        newsRecyclerView.setLayoutManager(manager);


        swipeRefresher = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshNewsList();
                swipeRefresher.setRefreshing(true);
            }
        });

        refreshNewsList();

        return view;
    }

    /**
     * Обновление новостного листа
     */
    public void refreshNewsList() {
        ((MainActivity)getActivity()).setToolbarTitle(R.string.loading);
        queryURLs(WebSetting.SITE_URL)
            .subscribe(new Action1<List<String>>() {
                @Override
                public void call(List<String> links) {
                    adapter = new NewsAdapter(links);
                    newsRecyclerView.setAdapter(adapter);

                    swipeRefresher.setRefreshing(false);
                    ((MainActivity)getActivity()).setToolbarTitle(R.string.news);
                }
            });
    }

    private Observable<List<String>> queryURLs(String link) {
        SiteXmlParsing webParsing = new SiteXmlParsing();

        return Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                subscriber.onNext(webParsing.getURLs(link));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
