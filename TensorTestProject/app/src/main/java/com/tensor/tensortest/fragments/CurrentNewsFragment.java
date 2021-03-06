package com.tensor.tensortest.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tensor.tensortest.MainActivity;
import com.tensor.tensortest.GifImageView;
import com.tensor.tensortest.R;
import com.tensor.tensortest.Utils.Settings;
import com.tensor.tensortest.app.App;
import com.tensor.tensortest.beans.News;

/**
 * Created by develop on 31.03.2017.
 */

public class CurrentNewsFragment extends Fragment {

    private static News currentNews;

    private GifImageView gifView;
    private TextView tvDescription;
    private TextView tvImageTitle;
    private ImageView ivImage;

    private Handler updateHandler = new Handler();
    private Runnable run = new Runnable() {
        @Override
        public void run() {
            if(currentNews.isReady()) {
                updateInfo();
            } else {
                updateHandler.postDelayed(run, 1000);
            }
        }
    };

    public static CurrentNewsFragment newInstance(News news) {
        CurrentNewsFragment.currentNews = news;
        return new CurrentNewsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_current_news, container, false);

        ivImage = (ImageView) view.findViewById(R.id.ivImage);
        tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        tvImageTitle = (TextView) view.findViewById(R.id.tvImageTitle);

        gifView = (GifImageView) view.findViewById(R.id.loadingGif);
        gifView.setGifImageResource(R.drawable.loading);

        Log.d(Settings.TAG, "isReady: " + Boolean.toString(currentNews.getReady()));
        updateInfo();

        return view;
    }

    /**
     * Обновляем данные новости, если они есть
     */
    private void updateInfo() {
        if(currentNews != null) {
            if (currentNews.getImage() != null) {
                Bitmap image = Settings.bytesToBitmap(currentNews.getImage());
                float mode = App.getWidthScreen() / image.getWidth();
                ivImage.setImageBitmap(Bitmap.createScaledBitmap(image, (int) (image.getWidth() * mode), (int) (image.getHeight() * mode), false));
                gifView.setVisibility(View.GONE);
            }
            tvImageTitle.setText(currentNews.getTitle());

            tvDescription.setText(currentNews.getDescription());
        }
    }

    @Override
    public void onPause() {
        updateHandler.removeCallbacks(run);
        ((MainActivity)getActivity()).setProgressBarVisibility(View.GONE);
        super.onPause();
    }

    @Override
    public void onResume() {
        if(!currentNews.isReady()) {
            updateHandler.postDelayed(run, 0);
        }
        super.onResume();
    }
}
