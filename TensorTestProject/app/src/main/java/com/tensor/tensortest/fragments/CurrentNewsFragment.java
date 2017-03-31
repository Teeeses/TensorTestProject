package com.tensor.tensortest.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tensor.tensortest.R;
import com.tensor.tensortest.beans.News;

/**
 * Created by develop on 31.03.2017.
 */

public class CurrentNewsFragment extends Fragment {

    private static News currentNews;

    private TextView tvTitle;

    public static CurrentNewsFragment newInstance(News news) {
        CurrentNewsFragment.currentNews = news;
        return new CurrentNewsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_current_news, container, false);

        tvTitle = (TextView) view.findViewById(R.id.tvTitle);

        tvTitle.setText(currentNews.getTitle());
        return view;
    }
}
