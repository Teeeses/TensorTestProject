package com.tensor.tensortest.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tensor.tensortest.App.App;
import com.tensor.tensortest.R;
import com.tensor.tensortest.beans.News;

import java.util.List;

/**
 * Created by develop on 24.03.2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{

    private List<News> links;

    public NewsAdapter() {
        this.links = App.getNews();
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
        String title = links.get(i).getTitle();
        viewHolder.tvNewTitle.setText(title);
    }

    @Override
    public int getItemCount() {
        return links.size();
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

        public ViewHolder(View itemView) {
            super(itemView);
            tvNewTitle = (TextView) itemView.findViewById(R.id.tvNewTitle);
        }
    }
}
