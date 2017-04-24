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
import android.widget.TextView;
import android.widget.Toast;

import com.tensor.tensortest.app.App;
import com.tensor.tensortest.MainActivity;
import com.tensor.tensortest.R;
import com.tensor.tensortest.Utils.Settings;
import com.tensor.tensortest.Web.RxRequest;
import com.tensor.tensortest.adapters.NewsAdapter;
import com.tensor.tensortest.beans.News;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by develop on 23.03.2017.
 * Фрагмент со списком новостей
 */

public class NewsListFragment extends Fragment {

    private NewsAdapter adapter;
    private SwipeRefreshLayout swipeRefresher;

    private TextView tvDatabaseIsEmpty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_news, container, false);

        RecyclerView newsRecyclerView = (RecyclerView) view.findViewById(R.id.newsRecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        newsRecyclerView.setLayoutManager(manager);

        tvDatabaseIsEmpty = (TextView) view.findViewById(R.id.tvDatabaseIsEmpty);

        adapter = new NewsAdapter(getContext(), (View recyclerView, int position) -> {
            if(position >= 0) {
                ((MainActivity) getActivity()).openCurrentNewsFragment(App.getNews().get(position));
            }
            Log.d(Settings.TAG, Integer.toString(position));
        });
        newsRecyclerView.setAdapter(adapter);
        newsRecyclerView.addOnItemTouchListener(adapter);

        newsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) //check for scroll down
                {
                    int visibleItemCount = manager.getChildCount();
                    int totalItemCount = manager.getItemCount();
                    int pastVisiblesItems = manager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        if(!swipeRefresher.isRefreshing()) {
                            swipeRefresher.post(() ->
                                    swipeRefresher.setRefreshing(true)
                            );
                            refreshFromSite();
                            Log.v(Settings.TAG, "Конец");
                        }
                    }
                }
            }
        });



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

        List<News> generalList = App.getNews();
        List<News> addedList = new ArrayList<>();
        for (int i = listNews.size() - 1; i >= 0; i--) {
            boolean value = false;
            for (int j = 0; j < generalList.size(); j++) {
                String pubTimeOne = listNews.get(i).getPubDate();
                String pubTimeTwo = generalList.get(j).getPubDate();
                if (pubTimeOne.equals(pubTimeTwo)) {
                    value = true;
                    break;
                }
            }
            if (!value) {
                Log.d(Settings.TAG, "Добавляем новость из базы в список: " + listNews.get(i).getTitle());
                addedList.add(0, listNews.get(i));
            }
        }
        generalList.addAll(0, addedList);
        App.findCurrentPage();

        if(App.getNews().size() != 0) {
            tvDatabaseIsEmpty.setVisibility(View.GONE);
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
                App.addCurrentPage();
            }

            @Override
            public void onError(Throwable e) {
                Log.d(Settings.TAG, "Невозможно добавить новость: " + e.toString());
                Toast.makeText(getActivity(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                swipeRefresher.setRefreshing(false);
            }

            @Override
            public void onNext(News news) {
                if(App.checkIsNewsInList(App.getNews(), news.getName())) {
                    Log.d(Settings.TAG, "Новость добавлена в список: " + news.getTitle() + " " + news.getShortDescription());
                    tvDatabaseIsEmpty.setVisibility(View.GONE);
                    App.getNews().add(news);
                }
                adapter.notifyDataSetChanged();
            }
        };
        RxRequest.getNewsObservable().subscribe(mySubscriber);
    }
}
