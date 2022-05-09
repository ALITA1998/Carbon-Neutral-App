package com.hui.carbon.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuli 2022/4/10
 */

public class GoodsList {
    @SerializedName("data")
    @Expose
    private List<Goods> data = null;

    public List<Goods> getData() {
        return data;
    }

    public void setData(List<Goods> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "GoodsList{" +
                "data=" + data +
                '}';
    }
}
