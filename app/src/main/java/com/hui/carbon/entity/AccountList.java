package com.hui.carbon.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hui.carbon.db.AccountBean;

import java.util.List;

/**
 * Created by liuli 2022/4/10
 */

public class AccountList {
    @SerializedName("data")
    @Expose
    private List<AccountBean> data = null;

    public List<AccountBean> getData() {
        return data;
    }

    public void setData(List<AccountBean> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "AccountList{" +
                "data=" + data +
                '}';
    }
}
