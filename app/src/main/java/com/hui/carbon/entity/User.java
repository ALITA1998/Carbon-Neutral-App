package com.hui.carbon.entity;

import java.math.BigDecimal;

/**
 * Created by liuli 2022/4/10
 */
public class User {
    private String name;            //用户名
    private String password;        //密码
    private Float money;            //余额
    private Float user_carbon_bal;        //碳配额
    public User(String name, String password, Float money, Float carbon_bal) {
        this.name = name;
        this.password = password;
        this.money = money;
        this.user_carbon_bal = carbon_bal;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }


    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public Float getUser_carbon_bal() {
        return user_carbon_bal;
    }

    public void setUser_carbon_bal(Float carbon_bal) {
        this.user_carbon_bal = carbon_bal;
    }
}

