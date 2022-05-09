package com.hui.carbon.frag_transaction;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
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

        carbonBal = 1500 - DBManager.getSumMoneyOneMonth(year, month, 0) + DBManager.getSumMoneyOneMonth(year, month, 1);
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

    /* 弹出备注对话框*/
    public  void showReDialog(){
        final RechargeDialog dialog = new RechargeDialog(getContext());
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(new RechargeDialog.OnEnsureListener() {
            @Override
            public void onEnsure() {
                String msg = dialog.getEditText();
                float ReMoney = Float.parseFloat(msg);
                if (!TextUtils.isEmpty(msg)) {
                    uniteApp.money = ReMoney  + Float.parseFloat(frag_my_bal_money.getText().toString());
                    waitDia("已充值" + ReMoney +"元。", ReMoney);
                }
                dialog.cancel();
            }
        });
    }



    public void waitDia(String message, float re_money) {
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
                            frag_my_bal_money.setText((re_money  + Float.parseFloat(frag_my_bal_money.getText().toString()) + ""));
//                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

                            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                                    .setIcon(R.drawable.wait)//设置标题的图片
                                    .setTitle("充值成功！")//设置对话框的标题
                                    .setMessage(message)//设置对话框的内容
                                    //设置对话框的按钮
//                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            Toast.makeText(MainActivity.this, "点击了取消按钮", Toast.LENGTH_SHORT).show();
//                                            dialog.dismiss();
//                                        }
//                                    })
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


