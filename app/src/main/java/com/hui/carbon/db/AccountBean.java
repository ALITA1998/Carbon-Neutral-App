package com.hui.carbon.db;
/** 描述记录一条数据的相关内容类*/
public class AccountBean {
    int id;
    String username;  //用户姓名
    String typename;   //类型
    int simageid;   //被选中类型图片
    String beizhu;   //备注
    float money;  //价格
    String time ;  //保存时间字符串
    int year;
    int month;
    int day;
    int kind;   //类型  收入---1   支出---0


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public int getsimageid() {
        return simageid;
    }

    public void setsImageId(int sImageId) {
        this.simageid = sImageId;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public String getUsername(){return username;}

    public void setUsername(String username){this.username = username;}

    public AccountBean() {
    }

    public AccountBean(int id, String username, String typename, int simageid, String beizhu, float money, String time, int year, int month, int day, int kind) {
        this.id = id;
        this.typename = typename;
        this.simageid = simageid;
        this.beizhu = beizhu;
        this.money = money;
        this.time = time;
        this.year = year;
        this.month = month;
        this.day = day;
        this.kind = kind;
        this.username = username;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "id='" + id + '\'' +
                ", name='" + username + '\'' +
                ", typename='" + typename + '\'' +
                ", sImageId=" + simageid + '\'' +

                '}';
    }
}
