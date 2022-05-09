package com.hui.carbon.frag_record;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hui.carbon.R;
import com.hui.carbon.RecordActivity;
import com.hui.carbon.UniteApp;
import com.hui.carbon.auto_get.StepCounter;
import com.hui.carbon.db.AccountBean;
import com.hui.carbon.db.DBManager;
import com.hui.carbon.db.TypeBean;
import com.hui.carbon.utils.BeiZhuDialog;
import com.hui.carbon.utils.KeyBoardUtils;
import com.hui.carbon.utils.SelectTimeDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 记录页面当中的排放模块
 */
public abstract class BaseRecordFragment extends Fragment implements View.OnClickListener {

    KeyboardView keyboardView;
    EditText moneyEt;
    ImageView typeIv;
    TextView typeTv,beizhuTv,timeTv;
    TextView type1Tv, typeNum;
    Button auto_get_btn;
    GridView typeGv;
    List<TypeBean>typeList;
    TypeBaseAdapter adapter;
    AccountBean accountBean;   //将需要插入到记录表当中的数据保存成对象的形式
    UniteApp app;

    long time = -2000;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountBean = new AccountBean();   //创建对象
        accountBean.setTypename("其他");
        accountBean.setsImageId(R.mipmap.ic_qita_fs);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_outcome, container, false);
        initView(view);
        setInitTime();
        //给GridView填充数据的方法
        loadDataToGV();
        setGVListener(); //设置GridView每一项的点击事件
        return view;
    }
    /* 获取当前时间，显示在timeTv上*/
    private void setInitTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = sdf.format(date);
        timeTv.setText(time);
        accountBean.setTime(time);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        accountBean.setYear(year);
        accountBean.setMonth(month);
        accountBean.setDay(day);
    }

    /* 设置GridView每一项的点击事件*/
    private void setGVListener() {
        typeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.selectPos = position;
                adapter.notifyDataSetInvalidated();  //提示绘制发生变化了

                TypeBean typeBean = typeList.get(position);
                String typename = typeBean.getTypename();
                typeTv.setText(typename);

                type1Tv.setText(typename);
                float _num = DBManager.getCarbonUnitData(typename);
                String num = ": " + _num +"kgCO2eq/" + unitMatch(typename);
                typeNum.setText(num);
                moneyEt.setText(null);

                accountBean.setTypename(typename);
                int simageId = typeBean.getSimageId();
                typeIv.setImageResource(simageId);
                accountBean.setsImageId(simageId);
            }
        });
    }

    /* 给GridView填出数据的方法，子类根据排放或者吸收类型重写*/
    public void loadDataToGV() {
        typeList = new ArrayList<>();
        adapter = new TypeBaseAdapter(getContext(), typeList);
        typeGv.setAdapter(adapter);
    }

    private void initView(View view) {
        keyboardView = view.findViewById(R.id.frag_record_keyboard);
        moneyEt = view.findViewById(R.id.frag_record_et_money);
        typeIv = view.findViewById(R.id.frag_record_iv);
        typeGv = view.findViewById(R.id.frag_record_gv);
        typeTv = view.findViewById(R.id.frag_record_tv_type);
        type1Tv = view.findViewById(R.id.frag_record_tv_type1);
        typeNum = view.findViewById(R.id.frag_record_tv_typeNum);
        auto_get_btn = view.findViewById(R.id.frag_record_auto_get);

        beizhuTv = view.findViewById(R.id.frag_record_tv_beizhu);
        timeTv = view.findViewById(R.id.frag_record_tv_time);
        beizhuTv.setOnClickListener(this);
        timeTv.setOnClickListener(this);
        auto_get_btn.setOnClickListener(this);
        app = (UniteApp) getActivity().getApplication();
        //让自定义软键盘显示出来
        KeyBoardUtils boardUtils = new KeyBoardUtils(keyboardView, moneyEt);
        boardUtils.showKeyboard();
        //设置接口，监听确定按钮按钮被点击了
        boardUtils.setOnEnsureListener(new KeyBoardUtils.OnEnsureListener() {
            @Override
            public void onEnsure() {
                //获取输入数据
                String moneyStr = moneyEt.getText().toString();
                if (TextUtils.isEmpty(moneyStr)||moneyStr.equals("0")) {
                    getActivity().finish();
                    return;
                }
                float carbon_data = Float.parseFloat(moneyStr);

                accountBean.setMoney(carbon_data);
                //获取记录的信息，保存在数据库当中
                saveAccountToDB();
                // 返回上一级页面
                getActivity().finish();
            }
        });
    }
    /* 让子类一定要重写这个方法*/
    public abstract void saveAccountToDB();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frag_record_tv_time:
                showTimeDialog();
                break;
            case R.id.frag_record_tv_beizhu:
                showBZDialog();
                break;
            case R.id.frag_record_auto_get:
                //这里是重点，老师要求新增的功能都在这里，即自动获取
                autoGetData();
                break;

        }
    }
    /* 弹出显示时间的对话框*/
    private void showTimeDialog() {
        SelectTimeDialog dialog = new SelectTimeDialog(getContext());
        dialog.show();
        //设定确定按钮被点击了的监听器
        dialog.setOnEnsureListener(new SelectTimeDialog.OnEnsureListener() {
            @Override
            public void onEnsure(String time, int year, int month, int day) {
                timeTv.setText(time);
                accountBean.setTime(time);
                accountBean.setYear(year);
                accountBean.setMonth(month);
                accountBean.setDay(day);
            }
        });
    }

    /* 弹出备注对话框*/
    public  void showBZDialog(){
        final BeiZhuDialog dialog = new BeiZhuDialog(getContext());
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(new BeiZhuDialog.OnEnsureListener() {
            @Override
            public void onEnsure() {
                String msg = dialog.getEditText();
                if (!TextUtils.isEmpty(msg)) {
                    beizhuTv.setText(msg);
                    accountBean.setBeizhu(msg);
                }
                dialog.cancel();
            }
        });
    }

    public String unitMatch(String typename){
        switch (typename){
            case "其他":
                return "";
            case "火车":
            case "汽车":
            case "巴士":
            case "电动汽车":
            case "共享单车":
                return "km";
            case "飞机":
                return "hour";
            case "步行":
                return "步";
            case "用水":
                return "吨";
            case "用电":
                return "度";
            case "天然气":
                return "立方米";
            case "干垃圾":
            case "湿垃圾":
                return "kg";
            case "植树":
            case "家庭绿化":
                return "平方米";
            case "捐赠":
                return "unknown";
        }
        return "";
    }

    public void autoGetData(){
        String typeName = typeTv.getText().toString();

        Intent intent;
        switch (typeName){
            case "步行":
                intent = new Intent(getActivity(), StepCounter.class);
                startActivity(intent);
                break;
            case "用水":
                waitDia("您今天用了3.6吨水。", (float) 3.6);
                break;
            case "用电":
                waitDia("您今天用了8.7度电。", (float) 8.7);
                break;
            case "火车":
                waitDia("您今天没有火车出行。", (float) 0);
                break;
            case "汽车":
                waitDia("您今天汽车出行20.9km。", (float) 20.9);
                break;
            case "巴士":
                waitDia("您今天没有巴士出行。", (float) 0);
                break;
            case "飞机":
                waitDia("您今天没有飞机出行。", (float) 0);
                break;
            case "电动汽车":
                waitDia("您今天电动汽车出行6.3km。", (float) 6.3);
                break;
            case "共享单车":
                waitDia("您今天共享单车出行4.8km。", (float) 4.8);
                break;
            case "天然气":
                waitDia("您今天用了3.3立方米天然气。", (float) 3.3);
                break;
            case "干垃圾":
                waitDia("您今天产生了2.5kg干垃圾。", (float) 2.5);
                break;
            case "湿垃圾":
                waitDia("您今天产生了1.9kg干垃圾。", (float) 1.9);
                break;
            case "植树":
                waitDia("您今天未植树。", (float) 0);
                break;
            case "家庭绿化":
                waitDia("您今天的家庭绿化是10平方米。", (float) 10);
                break;
            case "捐赠":
                waitDia("您今天捐赠了5kgCO2。", (float) 5);
                break;

        }

//        if(typeName.equals("步行")){
//            intent = new Intent(getActivity(), StepCounter.class);
//            startActivity(intent);
//        }else {
//            Toast.makeText(getActivity(),"抱歉，该功能还在开发中", Toast.LENGTH_LONG).show();
//        }

    }



    public void waitDia(String message, float carbon_data) {
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

                    ((RecordActivity)getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //todo refresh ui
                            progressDialog.dismiss();
                            moneyEt.setText(carbon_data + "");
//                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

                            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                                    .setIcon(R.drawable.wait)//设置标题的图片
                                    .setTitle("获取成功！")//设置对话框的标题
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

    @Override
    public void onResume() {
        super.onResume();
        moneyEt.setText("" + app.stepCount);
    }

}
