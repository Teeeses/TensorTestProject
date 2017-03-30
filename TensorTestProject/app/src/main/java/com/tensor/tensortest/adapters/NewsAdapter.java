package com.tensor.tensortest.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tensor.tensortest.App.App;
import com.tensor.tensortest.R;
import com.tensor.tensortest.Utils.Settings;
import com.tensor.tensortest.beans.News;

import java.util.List;


/**
 * Created by develop on 24.03.2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{

    private List<News> news;

    public NewsAdapter() {
        this.news = App.getNews();
    }

    /**
     * Создание новых View и ViewHolder элемента списка, которые впоследствии могут переиспользоваться.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_news_item, viewGroup, false);
        return new ViewHolder(v);
    }

    /**
     * Заполнение виджетов View данными из элемента списка с номером i
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.tvNewTitle.setText(news.get(i).getTitle());
        viewHolder.tvPubDate.setText(Settings.timeToString(news.get(i).getPubDate()));
        viewHolder.tvShortDescription.setText(news.get(i).getShort_description());
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    /**
     * Реализация класса ViewHolder, хранящего ссылки на виджеты.
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNewTitle;
        private TextView tvPubDate;
        private TextView tvShortDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNewTitle = (TextView) itemView.findViewById(R.id.tvNewTitle);
            tvPubDate = (TextView) itemView.findViewById(R.id.tvPubDate);
            tvShortDescription = (TextView) itemView.findViewById(R.id.tvShortDescription);
        }
    }
}
