package com.photozig.prototype.rest.models;

import java.util.ArrayList;

/**
 * Created by macbook on 02/12/2017.
 */

public class ZigObject {

    private String name;
    private String bg;
    private String im;
    private String sg;

    private ArrayList<TxtObject> txts;

    public String getName() {
        return name;
    }

    public String getBg() {
        return bg;
    }

    public String getIm() {
        return im;
    }

    public String getSg() {
        return sg;
    }

    public ArrayList<TxtObject> getTxts() {
        return txts;
    }
}
