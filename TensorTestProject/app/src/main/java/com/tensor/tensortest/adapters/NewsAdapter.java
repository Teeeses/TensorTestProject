package com.tensor.tensortest.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tensor.tensortest.app.App;
import com.tensor.tensortest.R;
import com.tensor.tensortest.Utils.Settings;
import com.tensor.tensortest.async.GetImageFromSrc;
import com.tensor.tensortest.beans.News;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * Created by develop on 24.03.2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> implements RecyclerView.OnItemTouchListener{

    private List<News> news;
    private OnItemClickListener listener;

    GestureDetector mGestureDetector;
    private Executor threadPoolExecutor;

    private int corePoolSize = 120;
    private int maximumPoolSize = 200;
    private int keepAliveTime = 20;
    private BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public NewsAdapter(Context context, OnItemClickListener listener) {
        this.listener = listener;
        this.news = App.getNews();

        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
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
        viewHolder.tvPubDate.setText(news.get(i).getPubDate());
        viewHolder.tvShortDescription.setText(news.get(i).getShortDescription());

        if(!news.get(i).isReady()) {
            try {
                new GetImageFromSrc().executeOnExecutor(threadPoolExecutor, news.get(i));
            } catch (RejectedExecutionException e) {
                e.printStackTrace();
                threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
            }
        }
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

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && listener != null && mGestureDetector.onTouchEvent(e)) {
            listener.onItemClick(childView, view.getChildAdapterPosition(childView));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {}

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}


}
