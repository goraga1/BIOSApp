package com.example.lenovo.hkgorprivateapp;

import android.graphics.Bitmap;


public class DataModel {
    private Bitmap myBitmap;
    private String musicPath;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    private boolean checked;

    public String getMusicPath() {
        return musicPath;
    }

    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
    }


    public Bitmap getMyBitmap() {
        return myBitmap;
    }

    public void setMyBitmap(Bitmap myBitmap) {
        this.myBitmap = myBitmap;
    }


}
