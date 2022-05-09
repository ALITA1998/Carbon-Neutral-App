package com.hui.carbon.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.hui.carbon.R;

public class DBOpenHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;

    public DBOpenHelper(@Nullable Context context) {
        super(context,"tally.db" , null, 1);
        db = getReadableDatabase();
    }

//    创建数据库的方法，只有项目第一次运行时，会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
//        创建表示类型的表
        String sql = "create table typetb(id integer primary key autoincrement,typename varchar(10),imageId integer,sImageId integer,kind integer,carbon float)";
        db.execSQL(sql);
        insertType(db);
        //创建记录表
        sql = "create table accounttb(id integer primary key autoincrement,typename varchar(10),sImageId integer,beizhu varchar(80),money float," +
                "time varchar(60),year integer,month integer,day integer,kind integer)";
        db.execSQL(sql);


//        //用户登录表
//        db.execSQL("CREATE TABLE IF NOT EXISTS user(" +
//                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
//                "name TEXT," +
//                "password TEXT)");
    }

    private void insertType(SQLiteDatabase db) {
//      向typetb表当中插入元素
        String sql = "insert into typetb (typename,imageId,sImageId,kind,carbon) values (?,?,?,?,?)";
        db.execSQL(sql,new Object[]{"其他", R.mipmap.ic_qita,R.mipmap.ic_qita_fs,0, 1});

        db.execSQL(sql,new Object[]{"火车", R.mipmap.ic_huoche,R.mipmap.ic_huoche_fs,0, 0.042});
        db.execSQL(sql,new Object[]{"汽车", R.mipmap.ic_qiche,R.mipmap.ic_qiche_fs,0, 0.257});
        db.execSQL(sql,new Object[]{"巴士", R.mipmap.ic_jiaotong,R.mipmap.ic_jiaotong_fs,0, 0.103});
        db.execSQL(sql,new Object[]{"飞机", R.mipmap.ic_feiji,R.mipmap.ic_feiji_fs,0, 3.00906});
        db.execSQL(sql,new Object[]{"电动汽车", R.mipmap.ic_diandong,R.mipmap.ic_diandong_fs,0, 1});
        db.execSQL(sql,new Object[]{"共享单车", R.mipmap.ic_bike,R.mipmap.ic_bike_fs,0, 1});
        db.execSQL(sql,new Object[]{"步行", R.mipmap.ic_walk,R.mipmap.ic_walk_fs,0, 1});


        db.execSQL(sql,new Object[]{"用水", R.mipmap.ic_water,R.mipmap.ic_water_fs,0,0.91});
        db.execSQL(sql,new Object[]{"用电", R.mipmap.ic_yongdian,R.mipmap.ic_yongdian_fs,0,0.681});
        db.execSQL(sql,new Object[]{"天然气", R.mipmap.ic_tianranqi,R.mipmap.ic_tianranqi_fs,0,2.19});
        db.execSQL(sql,new Object[]{"干垃圾", R.mipmap.ic_ganlaji,R.mipmap.ic_ganlaji_fs,0,2.06});
        db.execSQL(sql,new Object[]{"湿垃圾", R.mipmap.ic_shilaji,R.mipmap.ic_shilaji_fs,0,2.06});


        db.execSQL(sql,new Object[]{"其他", R.mipmap.in_qt,R.mipmap.in_qt_fs,1,1});
        db.execSQL(sql,new Object[]{"植树", R.mipmap.ic_tree,R.mipmap.ic_tree_fs,1,1});
        db.execSQL(sql,new Object[]{"家庭绿化", R.mipmap.ic_lvhua,R.mipmap.ic_lvhua_fs,1,1});
        db.execSQL(sql,new Object[]{"捐赠", R.mipmap.ic_donate,R.mipmap.ic_donate_fs,1,1});



    }


    // 数据库版本在更新时发生改变，会调用此方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
