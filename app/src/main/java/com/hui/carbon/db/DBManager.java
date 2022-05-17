package com.hui.carbon.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.hui.carbon.RecordActivity;
import com.hui.carbon.UniteApp;
import com.hui.carbon.entity.Goods;
import com.hui.carbon.frag_transaction.SellActivity;
import com.hui.carbon.utils.FloatUtils;
import com.rxjava.rxlife.RxLife;

import java.util.ArrayList;
import java.util.List;

import rxhttp.wrapper.param.RxHttp;

/*
* 负责管理数据库的类
*   主要对于表当中的内容进行操作，增删改查
* */
public class DBManager {
    UniteApp uniteApp;
    private static SQLiteDatabase db;
    /* 初始化数据库对象*/
    public static void initDB(Context context){
        DBOpenHelper helper = new DBOpenHelper(context);  //得到帮助类对象
        db = helper.getWritableDatabase();      //得到数据库对象
    }

    /**
     * 读取数据库当中的数据，写入内存集合里
     *   kind :表示吸收或者排放
     * */
    public static List<TypeBean>getTypeList(int kind){
        List<TypeBean>list = new ArrayList<>();
        //读取typetb表当中的数据
        String sql = "select * from typetb where kind = "+kind;
        Cursor cursor = db.rawQuery(sql, null);
//        循环读取游标内容，存储到对象当中
        while (cursor.moveToNext()) {
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            int imageId = cursor.getInt(cursor.getColumnIndex("imageId"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind1 = cursor.getInt(cursor.getColumnIndex("kind"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            float carbon = cursor.getFloat(cursor.getColumnIndex("carbon"));
            TypeBean typeBean = new TypeBean(id, typename, imageId, sImageId, kind, carbon);
            list.add(typeBean);
        }
        return list;
    }

    /*
    * 向记录表当中插入一条元素
    * */
    public static void insertItemToAccounttb(AccountBean bean){
        ContentValues values = new ContentValues();
        values.put("typename",bean.getTypename());
        values.put("simageid",bean.getsimageid());
        Log.d("simageid",bean.getsimageid() + "");
        values.put("beizhu",bean.getBeizhu());
        values.put("money",bean.getMoney());
        values.put("time",bean.getTime());
        values.put("year",bean.getYear());
        values.put("month",bean.getMonth());
        values.put("day",bean.getDay());
        values.put("kind",bean.getKind());
        values.put("username",bean.getUsername());
        db.insert("accounttb",null,values);
    }


    /*
    * 获取记录表当中某一天的所有排放或者吸收情况
    * */
    public static List<AccountBean>getAccountListOneDayFromAccounttb(int year,int month,int day){
        List<AccountBean>list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=? and day=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + ""});
        //遍历符合要求的每一行数据
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String beizhu = cursor.getString(cursor.getColumnIndex("beizhu"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int simageid = cursor.getInt(cursor.getColumnIndex("simageid"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            String username = cursor.getString(cursor.getColumnIndex("username"));
            AccountBean accountBean = new AccountBean(id, username, typename, simageid, beizhu, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;
    }

    /*
     * 获取记录表当中某一月的所有排放或者吸收情况
     * */
    public static List<AccountBean>getAccountListOneMonthFromAccounttb(int year,int month){
        List<AccountBean>list = new ArrayList<>();
        String sql = "select * from accounttb where year=? and month=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + ""});
        //遍历符合要求的每一行数据
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String beizhu = cursor.getString(cursor.getColumnIndex("beizhu"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int simageid = cursor.getInt(cursor.getColumnIndex("simageid"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            String username = cursor.getString(cursor.getColumnIndex("username"));
            AccountBean accountBean = new AccountBean(id, username,typename, simageid, beizhu, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;
    }
    /**
     * 获取某一天的排放或者吸收的总额   kind：排放==0    吸收===1
     * */
    public static float getSumMoneyOneDay(int year,int month,int day,int kind){
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and month=? and day=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + "", kind + ""});
        // 遍历
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        return total;
    }
    /**
     * 获取某一月的排放或者吸收的总额   kind：排放==0    吸收===1
     * */
    public static float getSumMoneyOneMonth(int year,int month,int kind){
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        // 遍历
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        return total;
    }
    /** 统计某月份排放或者吸收情况有多少条  吸收-1   排放-0*/
    public static int getCountItemOneMonth(int year,int month,int kind){
        int total = 0;
        String sql = "select count(money) from accounttb where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(cursor.getColumnIndex("count(money)"));
            total = count;
        }
        return total;
    }
    /**
     * 获取某一年的排放或者吸收的总额   kind：排放==0    吸收===1
     * */
    public static float getSumMoneyOneYear(int year,int kind){
        float total = 0.0f;
        String sql = "select sum(money) from accounttb where year=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", kind + ""});
        // 遍历
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        return total;
    }

    /*
    * 根据传入的id，删除accounttb表当中的一条数据
    * */
    public static int deleteItemFromAccounttbById(int id){
        int i = db.delete("accounttb", "id=?", new String[]{id + ""});
        return i;
    }
    /**
     * 根据备注搜索吸收或者排放的情况列表
     * */
    public static List<AccountBean>getAccountListByRemarkFromAccounttb(String beizhu){
        List<AccountBean>list = new ArrayList<>();
        String sql = "select * from accounttb where beizhu like '%"+beizhu+"%'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String bz = cursor.getString(cursor.getColumnIndex("beizhu"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int simageid = cursor.getInt(cursor.getColumnIndex("simageid"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            int month = cursor.getInt(cursor.getColumnIndex("month"));
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            String username = cursor.getString(cursor.getColumnIndex("username"));
            AccountBean accountBean = new AccountBean(id, username, typename, simageid, bz, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;
    }

    /**
     * 查询记录的表当中有几个年份信息
     * */
    public static List<Integer>getYearListFromAccounttb(){
        List<Integer>list = new ArrayList<>();
        String sql = "select distinct(year) from accounttb order by year asc";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            list.add(year);
        }
        return list;
    }

    /*
    * 删除accounttb表格当中的所有数据
    * */
    public static void deleteAllAccount(){
        String sql = "delete from accounttb";
        db.execSQL(sql);
    }

    /**
     * 查询指定年份和月份的吸收或者排放每一种类型的总数
     * */
    public static List<ChartItemBean>getChartListFromAccounttb(int year,int month,int kind){
        List<ChartItemBean>list = new ArrayList<>();
        float sumMoneyOneMonth = getSumMoneyOneMonth(year, month, kind);  //求出排放或者吸收总钱数
        String sql = "select typename,sImageId,sum(money)as total from accounttb where year=? and month=? and kind=? group by typename " +
                "order by total desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        while (cursor.moveToNext()) {
            int sImageId = cursor.getInt(cursor.getColumnIndex("simageid"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            float total = cursor.getFloat(cursor.getColumnIndex("total"));
            //计算所占百分比  total /sumMonth
            float ratio = FloatUtils.div(total,sumMoneyOneMonth);
            ChartItemBean bean = new ChartItemBean(sImageId, typename, ratio, total);
            list.add(bean);
        }
        return list;
    }

    /**
    * 获取这个月当中某一天吸收排放最大额，是多少
     * */
    public static float getMaxMoneyOneDayInMonth(int year,int month,int kind){
        String sql = "select sum(money) from accounttb where year=? and month=? and kind=? group by day order by sum(money) desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            return money;
        }
        return 0;
    }

    /** 根据指定月份每一日吸收或者排放的总数的集合*/
    public static List<BarChartItemBean>getSumMoneyOneDayInMonth(int year,int month,int kind){
        String sql = "select day,sum(money) from accounttb where year=? and month=? and kind=? group by day";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        List<BarChartItemBean>list = new ArrayList<>();
        while (cursor.moveToNext()) {
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            float smoney = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            BarChartItemBean itemBean = new BarChartItemBean(year, month, day, smoney);
            list.add(itemBean);
        }
        return list;
    }


    /**根据类型查找碳中和的单位数据*/
     public static float getCarbonUnitData(String type){

         String sql = "select * from typetb where typename like '"+type+"'";
         Cursor cursor = db.rawQuery(sql, null);

         float carbon_data = 1;
         while (cursor.moveToNext()){
             carbon_data = cursor.getFloat(cursor.getColumnIndex("carbon"));
         }

         return carbon_data;

     }

    /**同步http请求发来的数据和本地数据库的数据*/
    public static void syncHttpData(List<AccountBean> accountBeanList){
        deleteAllAccount();
        for(int i = 0; i < accountBeanList.size(); i++){
            insertItemToAccounttb(accountBeanList.get(i));
        }
        Log.d("同步", "成功！");
    }

}
