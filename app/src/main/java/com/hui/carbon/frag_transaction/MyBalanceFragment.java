package com.hui.carbon.frag_transaction;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hui.carbon.MeActivity;
import com.hui.carbon.MonthChartActivity;
import com.hui.carbon.R;
import com.hui.carbon.RecordActivity;
import com.hui.carbon.SearchActivity;
import com.hui.carbon.TransactionActivity;
import com.hui.carbon.UniteApp;
import com.hui.carbon.db.DBManager;
import com.hui.carbon.utils.BeiZhuDialog;
import com.hui.carbon.utils.RechargeDialog;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rxhttp.wrapper.param.RxHttp;


public class MyBalanceFragment extends Fragment implements View.OnClickListener{

    TextView frag_my_bal_carbon;
    TextView frag_my_bal_money;
    Button recharge, tixian;
    float carbonBal;
    int year, month, day;
    long time = -2000;

    UniteApp uniteApp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uniteApp = (UniteApp) getActivity().getApplication();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_balance, container, false);
        //        initData();


        /* 获取今日的具体时间*/

        initTime();
        frag_my_bal_carbon = view.findViewById(R.id.frag_my_bal_carbon);
        frag_my_bal_money = view.findViewById(R.id.frag_my_bal_money);
        recharge = view.findViewById(R.id.frag_my_bal_recharge);
        tixian = view.findViewById(R.id.frag_my_bal_tixian);
        frag_my_bal_money.setText(uniteApp.money+ "");
        recharge.setOnClickListener(this);
        tixian.setOnClickListener(this);


        carbonBal = uniteApp.carbon_balance;
        frag_my_bal_carbon.setText(carbonBal + "");

        return view;

    }



    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frag_my_bal_recharge:
                showReDialog();
                break;
            case R.id.frag_my_bal_tixian:
////                addIv.setImageResource(R.mipmap.add_48_pressed);
//                Intent it1 = new Intent(this, RecordActivity.class);  //跳转界面
//                startActivity(it1);
                break;

        }

    }

    /* 弹出充值对话框*/
    public  void showReDialog(){
        final RechargeDialog dialog = new RechargeDialog(getContext());
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(new RechargeDialog.OnEnsureListener() {
            @Override
            public void onEnsure() {
                String msg = dialog.getEditText();

                if(isNumber(msg)){
                    float ReMoney = Float.parseFloat(msg);
                    uniteApp.money = ReMoney  + Float.parseFloat(frag_my_bal_money.getText().toString());
                    frag_my_bal_money.setText(uniteApp.money+ "");
                    httpUpdateUserMoney();
                    waitDia("已充值" + ReMoney +"元。");
                    Log.d("测试余额", "SSSSSSSSSSSSSSSSS" + uniteApp.money);
                }else{
                    Toast.makeText(getActivity(), "输入非法！", Toast.LENGTH_SHORT).show();
                }
                dialog.cancel();
            }
        });
    }

    @SuppressLint("CheckResult")
    public void httpUpdateUserMoney() {
        String url = "http://192.168.43.196:8080/updateUserMoney/"+uniteApp.account +"?money=" + uniteApp.money;
        RxHttp.get(url)
                .asString()
                .subscribe(s -> {
                    Log.d("QQQQQQQ", "用户余额信息更新成功！");

                }, throwable -> {
                    Toast.makeText(getActivity(), "用户余额信息更新失败！" + throwable, Toast.LENGTH_SHORT).show();
                });
    }




    public void waitDia(String message) {
        if (System.currentTimeMillis() - time < 2000) {
            Toast.makeText(getActivity(), "请慢点点击！", Toast.LENGTH_SHORT).show();
        } else {
            time = System.currentTimeMillis();
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
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

                    ((TransactionActivity)getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //todo refresh ui
                            progressDialog.dismiss();

//                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

                            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                                    .setIcon(R.drawable.wait)//设置标题的图片
                                    .setTitle("充值成功！")//设置对话框的标题
                                    .setMessage(message)//设置对话框的内容


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


    // 通过 -?[0-9]+(\\\\.[0-9]+)? 进行匹配是否为数字
    private static Pattern pattern = Pattern.compile("-?[0-9]+(\\\\.[0-9]+)?");

    /**
     * 通过正则表达式判断字符串是否为数字
     * @param str
     * @return
     */
    private static boolean isNumber(String str) {
        // 通过Matcher进行字符串匹配
        Matcher m = pattern.matcher(str);
        // 如果正则匹配通过 m.matches() 方法返回 true ，反之 false
        return m.matches();
    }


}


