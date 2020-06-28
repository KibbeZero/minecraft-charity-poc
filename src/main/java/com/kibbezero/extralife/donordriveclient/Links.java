package com.kibbezero.extralife.donordriveclient;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Links implements Serializable
{

    @SerializedName("donate")
    @Expose
    private String donate;
    @SerializedName("page")
    @Expose
    private String page;
    private final static long serialVersionUID = 270594207835017294L;

    public String getDonate() {
        return donate;
    }

    public void setDonate(String donate) {
        this.donate = donate;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

}