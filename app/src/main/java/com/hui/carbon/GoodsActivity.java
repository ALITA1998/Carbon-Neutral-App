package com.hui.carbon;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.hui.carbon.db.AccountBean;
import com.hui.carbon.db.DBManager;
import com.hui.carbon.entity.Goods;
import com.hui.carbon.entity.GoodsList;

import java.util.ArrayList;
import java.util.List;

import rxhttp.wrapper.param.RxHttp;

public class GoodsActivity extends AppCompatActivity {
    private TextView tv_price;
    private TextView tv_name;
    private TextView tv_desc;
    private Integer goods_id;
    private TextView activity_goods_carbon_bal;
    private Button activity_goods_buy;
    UniteApp uniteApp;
    long time = -2000;
    boolean netFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);

        tv_name = findViewById(R.id.activity_goods_user_name);
        tv_price = findViewById(R.id.activity_goods_price);
        tv_desc = findViewById(R.id.activity_goods_desc);
        activity_goods_buy = findViewById(R.id.activity_goods_buy);
        activity_goods_carbon_bal = findViewById(R.id.activity_goods_carbon_bal);

        uniteApp = (UniteApp) getApplication();
        netFlag = true;
        //接收GoodsList传来的数据
        Goods goods = (Goods) getIntent().getSerializableExtra("goods");

        tv_name.setText(goods.getName().toString().trim());
        tv_price.setText(goods.getPrice().toString().trim());
        tv_desc.setText(goods.getDescription().toString().trim());
        activity_goods_carbon_bal.setText(goods.getCarbon_balance()+"");

        goods_id = goods.getId();
        activity_goods_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBuyDialog();

            }
        });



        Log.d("IDDDDDDDDD    ",goods_id+ "");
//        Glide.with(getApplicationContext()).load(goods.getIcon().toString()).into(imageView);

    }

    @SuppressLint("CheckResult")
    public void httpDeleteGoods() {
        List<Goods> goods = new ArrayList<>();
//        String url = "http://39.105.14.128:8080/list";
        String url = "http://192.168.43.196:8080/delete?id=" + goods_id;
        RxHttp.get(url)
                .asString()
                .subscribe(s -> {
                    Log.d("QQ", "删除成功！");

                }, throwable -> {
//                    Toast.makeText(this, "删除失败！" + throwable, Toast.LENGTH_SHORT).show();
                });
    }

    @SuppressLint("CheckResult")
    public void httpUpdateUserMoney(String userName, Float money) {
        String url = "http://192.168.43.196:8080/updateUserMoney/"+userName +"?money=" + money;
        RxHttp.get(url)
                .asString()
                .subscribe(s -> {
                    Log.d("QQQQQQQ", "用户余额信息更新成功！");

                }, throwable -> {
                    netFlag = false;
                    Toast.makeText(this, "用户余额信息更新失败！" + throwable, Toast.LENGTH_SHORT).show();
                });
    }

    @SuppressLint("CheckResult")
    public void httpUpdateOtherUserMoney(String userName, String money) {
        String url = "http://192.168.43.196:8080/updateOtherUserMoney/"+userName +"?money=" + money;
        RxHttp.get(url)
                .asString()
                .subscribe(s -> {
                    Log.d("QQQQQQQ", "用户余额信息更新成功！");

                }, throwable -> {
                    netFlag = false;
                    Toast.makeText(this, "用户余额信息更新失败！" + throwable, Toast.LENGTH_SHORT).show();
                });
    }

    @SuppressLint("CheckResult")
    public void httpUpdateUserCarbonBal(String userName, Float carbon_balance) {
        String url = "http://192.168.43.196:8080/updateUserCarbonBal/"+userName +"?user_carbon_bal=" + carbon_balance;
        RxHttp.get(url)
                .asString()
                .subscribe(s -> {
                    Log.d("QQQQQQQ", "用户信息更新成功！");

                }, throwable -> {
                    netFlag = false;
                    Toast.makeText(this, "用户信息更新失败！" + throwable, Toast.LENGTH_SHORT).show();
                });
    }

    /* 弹出是否购买的对话框*/
    private void showBuyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息").setMessage("您确定要购买吗？")
                .setNegativeButton("取消",null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(uniteApp.account.equals(tv_name.getText().toString())){
                            AlertDialog.Builder builder = new AlertDialog.Builder(GoodsActivity.this);
                            builder.setMessage("不可以购买自己发布的商品！")
                                    .setPositiveButton("确定", null);
                            builder.create().show();
                            Log.d("商品", "不可以购买自己发布的商品！");
                        }
                        else if(uniteApp.money < Float.parseFloat(MainActivity.getNumber(tv_price.getText().toString()))){
                            AlertDialog.Builder builder = new AlertDialog.Builder(GoodsActivity.this);
                            builder.setMessage("您的余额不足！")
                                    .setPositiveButton("确定", null);
                            builder.create().show();
                            Log.d("余额", "余额不足！");
                        }else {
                            uniteApp.carbon_balance += Float.parseFloat(activity_goods_carbon_bal.getText().toString().trim());
                            uniteApp.money -= Float.parseFloat(tv_price.getText().toString().trim());
                            Log.d("碳配额测试", uniteApp.carbon_balance + "");
                            waitDia("已成功支付" + tv_price.getText().toString() + "元");
                            //更新数据库的内容
                            httpDeleteGoods();
                            //更新当前用户的信息
                            httpUpdateUserMoney(uniteApp.account, uniteApp.money);
                            httpUpdateUserCarbonBal(uniteApp.account, uniteApp.carbon_balance);
                            //更新交易对象用户的信息
                            httpUpdateOtherUserMoney(tv_name.getText().toString(), tv_price.getText().toString());
//                            if(netFlag){
//                                uniteApp.carbon_balance += Float.parseFloat(activity_goods_carbon_bal.getText().toString().trim());
//                                uniteApp.money -= Float.parseFloat(tv_price.getText().toString().trim());
//                                Log.d("碳配额测试", uniteApp.carbon_balance + "");
//                                waitDia("已成功支付" + tv_price.getText().toString() + "元");
//                            }
                        }

                    }
                });
        builder.create().show();   //显示对话框
    }

    public void waitDia(String message) {
        if (System.currentTimeMillis() - time < 2000) {
            Toast.makeText(this, "请慢点点击！", Toast.LENGTH_SHORT).show();
        } else {
            time = System.currentTimeMillis();
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setIcon(R.drawable.wait);
            progressDialog.setTitle("请等待");
            progressDialog.setMessage("正在加载中...");
            progressDialog.show();

            //模拟获取数据时的加载
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);//显示1秒后，取消ProgressDialog
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //todo refresh ui
                            progressDialog.dismiss();
//                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

                            AlertDialog dialog = new AlertDialog.Builder(GoodsActivity.this)
                                    .setIcon(R.drawable.wait)//设置标题的图片
                                    .setTitle("支付成功！")//设置对话框的标题
                                    .setMessage(message)//设置对话框的内容
                                    //设置对话框的按钮
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.dismiss();
                                        }
                                    }).create();
                            dialog.show();

                        }
                    });
                }
            });
            t.start();


        }
    }
}
