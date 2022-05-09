package com.hui.carbon.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liuli 2022/4/10
 */

public class Goods implements Serializable {


    @SerializedName("id")
    @Expose
    private String id;
    //用户的名字
    @SerializedName("name")
    @Expose
    private String name;
    //用户要卖的碳余额
    @SerializedName("carbon_balance")
    @Expose
    private Double carbon_balance;
    //用户自定义的价格
    @SerializedName("price")
    @Expose
    private Double price;
    //可选商品描述
    @SerializedName("description")
    @Expose
    private String description;
//    @SerializedName("icon")
//    @Expose
//    private String icon;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCarbon_balance() {
        return carbon_balance;
    }

    public void setCarbon_balance(Double carbon_balance) {
        this.carbon_balance = carbon_balance;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public String getIcon() {
//        return icon;
//    }
//
//    public void setIcon(String icon) {
//        this.icon = icon;
//    }

    @Override
    public String toString() {
        return "Goods{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", carbon_balance='" + carbon_balance + '\'' +
                ", price=" + price + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
