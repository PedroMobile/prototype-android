package com.photozig.prototype.rest.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by pedrohenrique on 04/02/17.
 */

public class AppInfo {

    private String assetsLocation;

    @SerializedName("objects")
    private ArrayList<ZigObject> zigObjects;

    public String getAssetsLocation() {
        return assetsLocation;
    }

    public ArrayList<ZigObject> getZigObjects() {
        return zigObjects;
    }
}
