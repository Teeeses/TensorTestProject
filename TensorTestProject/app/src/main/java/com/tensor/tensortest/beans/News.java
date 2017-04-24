package com.tensor.tensortest.beans;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.tensor.tensortest.Utils.Settings;

/**
 * Created by develop on 24.03.2017.
 */

public class News {

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

    //Изображение новости
    private byte[] image;

    private String imageSrc;

    //Получены новости или нет
    private Boolean ready = false;

    //Уникальное имя новости вида: "r289210712211456"
    private String name;



    public News() {}

    public Boolean isReady() {
        return ready;
    }

    public void setReady(Boolean ready) {
        this.ready = ready;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        //setTimeMills();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }
}
