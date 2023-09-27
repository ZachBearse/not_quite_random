package com.zachbearse.notquiterandom;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Fighter implements Serializable {
    private String name;
    private String gameSeries;
    private String number;
    private String image;
    private boolean checked = false;

    public Fighter(String name, String gameSeries, String number, String image) {
        this.name = name;
        this.gameSeries = gameSeries;
        this.number = number;
        this.image = image;
    }

    public String getName() {
        return name;
    }


    public String getGameSeries() {
        return gameSeries;
    }

    public String getNumber() {
        return number;
    }

    public String getImage() {
        return image;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

}
