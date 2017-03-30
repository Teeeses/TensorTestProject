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

import com.tensor.tensortest.App.App;
import com.tensor.tensortest.R;
import com.tensor.tensortest.Utils.Settings;
import com.tensor.tensortest.Web.RxRequest;
import com.tensor.tensortest.adapters.NewsAdapter;
import com.tensor.tensortest.beans.News;
import rx.Subscriber;

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

        adapter = new NewsAdapter();
        newsRecyclerView.setAdapter(adapter);

        swipeRefresher = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeRefresher.setOnRefreshListener(() -> {
                refresh();
                swipeRefresher.setRefreshing(true);
        });

        refresh();
        swipeRefresher.setRefreshing(true);

        return view;
    }

    /**
     * Обновление новостного листа
     */
    public void refresh() {
        Subscriber<News> mySubscriber = new Subscriber<News>() {
            @Override
            public void onCompleted() {
                Log.d(Settings.TAG, "Успешно " + Integer.toString(App.getNews().size()));
                adapter.notifyDataSetChanged();
                swipeRefresher.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(Settings.TAG, "Ошибка: " + e.toString());
                swipeRefresher.setRefreshing(false);
            }

            @Override
            public void onNext(News news) {
                Log.d(Settings.TAG, "Новость: " + news.getTitle() + " " + news.getShort_description());
                App.getNews().add(news);
                adapter.notifyDataSetChanged();
            }
        };
        RxRequest.getNewsObservable().subscribe(mySubscriber);
    }
}
