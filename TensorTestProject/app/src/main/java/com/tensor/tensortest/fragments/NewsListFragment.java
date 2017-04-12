package com.tensor.tensortest.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tensor.tensortest.app.App;
import com.tensor.tensortest.MainActivity;
import com.tensor.tensortest.R;
import com.tensor.tensortest.Utils.Settings;
import com.tensor.tensortest.Web.RxRequest;
import com.tensor.tensortest.adapters.NewsAdapter;
import com.tensor.tensortest.beans.News;

import java.util.Collections;
import java.util.List;

import rx.Subscriber;

/**
 * Created by develop on 23.03.2017.
 * Фрагмент со списком новостей
 */

public class NewsListFragment extends Fragment {

    private NewsAdapter adapter;
    private SwipeRefreshLayout swipeRefresher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_news, container, false);

        RecyclerView newsRecyclerView = (RecyclerView) view.findViewById(R.id.newsRecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        newsRecyclerView.setLayoutManager(manager);

        adapter = new NewsAdapter(getContext(), (View recyclerView, int position) -> {
            if(position >= 0) {
                ((MainActivity) getActivity()).openCurrentNewsFragment(App.getNews().get(position));
            }
            Log.d(Settings.TAG, Integer.toString(position));
        });
        newsRecyclerView.setAdapter(adapter);
        newsRecyclerView.addOnItemTouchListener(adapter);


        swipeRefresher = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeRefresher.setOnRefreshListener(() -> update());

        update();

        return view;
    }

    /**
     * Обновление новостного листа
     */
    public void update() {
        swipeRefresher.setRefreshing(true);
        Log.d(Settings.TAG, "Internet status: " + Boolean.toString(App.isNetworkStatus()));

        if(App.isNetworkStatus()) {
            refreshFromDatabase();
            refreshFromSite();
        } else {
            refreshFromDatabase();
            swipeRefresher.setRefreshing(false);
        }
    }

    /**
     * Получение данных из базы данных
     */
    private void refreshFromDatabase() {
        List<News> listNews = App.getDataSource().getAllNews();
        //Сортируем список полученный из базы по времени добовления новости(элемент с индексом 0 - самая свежая новость)
        Collections.sort(listNews, (a, b) -> a.getTimeMills() < b.getTimeMills() ? 1  : -1);

        List<News> generalList = App.getNews();
        //Добавляем только те новости, которых нет в списке новостей
        for(int i = listNews.size() - 1; i >= 0; i--) {
            if(generalList.size() != 0) {
                if (listNews.get(i).getTimeMills() > generalList.get(0).getTimeMills()) {

                    Log.d(Settings.TAG, "Добавляем новость из базы в список: " + listNews.get(i).getTitle());
                    generalList.add(0, listNews.get(i));
                }
            } else {
                generalList.add(0, listNews.get(i));
            }
        }
    }

    /**
     * Получение данных с сайта
     */
    public void refreshFromSite() {
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
                Toast.makeText(getActivity(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                swipeRefresher.setRefreshing(false);
            }

            @Override
            public void onNext(News news) {
                Log.d(Settings.TAG, "Новость: " + news.getTitle() + " " + news.getShortDescription());
                App.getNews().add(0, news);
                adapter.notifyDataSetChanged();
            }
        };
        RxRequest.getNewsObservable().subscribe(mySubscriber);
    }
}
