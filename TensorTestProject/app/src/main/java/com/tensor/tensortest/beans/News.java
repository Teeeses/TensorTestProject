package com.tensor.tensortest.beans;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.tensor.tensortest.Utils.Settings;

/**
 * Created by develop on 24.03.2017.
 */

public class News {

    //Ссылка на новость
    private String link;

    //Заголовок новости
    private String title;

    //Короткое описание на карточки с новостью
    private String shortDescription;

    //Полное описание
    private String description;

    //Дата публикации
    private String pubDate;

    //Время публикации в миллисекундах
    private long timeMills;

    //src у img в html документе
    private String linkImage;

    //Надпись под изображением
    private String imageTitle;

    //Изображение новости
    private Bitmap image;

    //Получены новости или нет
    private Boolean ready = false;



    public News() {}

    public Boolean isReady() {
        return ready;
    }

    public void setReady(Boolean ready) {
        this.ready = ready;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
        setTimeMills();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public long getTimeMills() {
        return timeMills;
    }

    public void setTimeMills() {
        timeMills = Settings.stringToMills(pubDate);
    }

    public Boolean getReady() {
        return ready;
    }
}
