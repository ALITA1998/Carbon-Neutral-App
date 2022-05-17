package com.hui.carbon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hui.carbon.adapter.AccountAdapter;
import com.hui.carbon.db.AccountBean;
import com.hui.carbon.db.DBManager;
import com.hui.carbon.entity.AccountList;
import com.hui.carbon.utils.BudgetDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rxhttp.wrapper.param.RxHttp;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ListView todayLv;  //展示今日收支情况的ListView
    ImageView searchIv;
//    Button editBtn;
//    ImageButton moreBtn;

    ImageView addIv, meIv;
    //声明数据源
    List<AccountBean>mDatas;
    AccountAdapter adapter;
    int year,month,day;
    //头布局相关控件
    View headerView;
    TextView topOutTv,topInTv,topbudgetTv,topConTv,topWarningTv;
    ImageView topShowIv;
    SharedPreferences preferences;

    private MeActivity fm_me;

    UniteApp uniteApp;
    String TAG = "主活动";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uniteApp = (UniteApp) getApplication();
        initTime();
        initView();
        preferences = getSharedPreferences("budget", Context.MODE_PRIVATE);

        //添加ListView的头布局
        addLVHeaderView();
        mDatas = new ArrayList<>();

        //设置适配器：加载每一行数据到列表当中
        adapter = new AccountAdapter(this, mDatas);
        todayLv.setAdapter(adapter);
    }
     /** 初始化自带的View的方法*/
    private void initView() {
        todayLv = findViewById(R.id.main_lv);
//        editBtn = findViewById(R.id.main_btn_edit);
//        moreBtn = findViewById(R.id.main_btn_more);
        addIv = findViewById(R.id.img_add);
        meIv = findViewById(R.id.img_me);

        searchIv = findViewById(R.id.main_iv_search);
//        editBtn.setOnClickListener(this);
//        moreBtn.setOnClickListener(this);
        addIv.setOnClickListener(this);
        meIv.setOnClickListener(this);
        searchIv.setOnClickListener(this);
        setLVLongClickListener();
    }
    /** 设置ListView的长按事件*/
    private void setLVLongClickListener() {
        todayLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {  //点击了头布局
                    return false;
                }
                int pos = position-1;
                AccountBean clickBean = mDatas.get(pos);  //获取正在被点击的这条信息

                //弹出提示用户是否删除的对话框
                showDeleteItemDialog(clickBean);
                return false;
            }
        });
    }
    /* 弹出是否删除某一条记录的对话框*/
    private void showDeleteItemDialog(final  AccountBean clickBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息").setMessage("您确定要删除这条记录么？")
                .setNegativeButton("取消",null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int click_id = clickBean.getId();
                        //执行删除的操作
                        DBManager.deleteItemFromAccounttbById(click_id);
                        mDatas.remove(clickBean);   //实时刷新，移除集合当中的对象
                        adapter.notifyDataSetChanged();   //提示适配器更新数据
                        setTopTvShow();   //改变头布局TextView显示的内容
                    }
                });
        builder.create().show();   //显示对话框
    }

    /** 给ListView添加头布局的方法*/
    private void addLVHeaderView() {
        //将布局转换成View对象
        headerView = getLayoutInflater().inflate(R.layout.item_mainlv_top, null);
        todayLv.addHeaderView(headerView);
        //查找头布局可用控件
        topOutTv = headerView.findViewById(R.id.item_mainlv_top_tv_out);
        topInTv = headerView.findViewById(R.id.item_mainlv_top_tv_in);
        topbudgetTv = headerView.findViewById(R.id.item_mainlv_top_tv_budget);
        topConTv = headerView.findViewById(R.id.item_mainlv_top_tv_day);
        topWarningTv = headerView.findViewById(R.id.item_mainlv_top_tv_warning);

//        topShowIv = headerView.findViewById(R.id.item_mainlv_top_iv_hide);

        topbudgetTv.setOnClickListener(this);
        headerView.setOnClickListener(this);
//        topShowIv.setOnClickListener(this);

    }
    /* 获取今日的具体时间*/
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }



    /* 设置头布局当中文本内容的显示*/
    private void setTopTvShow() {

        //获取今日排放和吸收总额，显示在view当中
        float incomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 1);
        float outcomeOneDay = DBManager.getSumMoneyOneDay(year, month, day, 0);
        String infoOneDay = "今日排放(kg) "+outcomeOneDay+"  吸收(kg)"+incomeOneDay;
        topConTv.setText(infoOneDay);
//        获取本月排放和吸收总额
        float incomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 1);
        float outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
        topInTv.setText(incomeOneMonth + "kg");
        topOutTv.setText(outcomeOneMonth + "kg");


//    设置显示运算剩余
        float bmoney = preferences.getFloat("bmoney", 0);//
//        if (bmoney == 0) {
//            topbudgetTv.setText("0 g");
//        }else{
//            float syMoney = bmoney-outcomeOneMonth;
//            topbudgetTv.setText(syMoney +"g");
//        }
        float syMoney = bmoney-outcomeOneMonth + incomeOneMonth;
        topbudgetTv.setText(syMoney +"kg");

        //利用正则表达式获取数字


        float OutNum = Float.parseFloat(getNumber(topOutTv.getText().toString()));
        if(OutNum <= 0){
            topWarningTv.setText("干得漂亮！");
        }
        else if(OutNum <= 600){
            topWarningTv.setText("较少");
        }else if(OutNum < 1500){
            topWarningTv.setText("加油");
        }else {
            topWarningTv.setText("已超标！");
        }



    }

    // 加载数据库数据
    private void loadDBData() {
//        if(!uniteApp.account.equals("游客")){
//            List<AccountBean> accountBeanList = httpGetAccountList(uniteApp.account);
//            Log.d(TAG,accountBeanList.toString());
//        }
        List<AccountBean> list = DBManager.getAccountListOneDayFromAccounttb(year, month, day);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        //获得Fragmentmanager实例
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        switch (v.getId()) {
            case R.id.main_iv_search:
                Intent it = new Intent(this, SearchActivity.class);  //跳转界面
                startActivity(it);
                break;
            case R.id.img_add:
//                addIv.setImageResource(R.mipmap.add_48_pressed);
                Intent it1 = new Intent(this, RecordActivity.class);  //跳转界面
                startActivity(it1);
                break;
//            case R.id.main_btn_more:
//                MoreDialog moreDialog = new MoreDialog(this);
//                moreDialog.show();
//                moreDialog.setDialogSize();
//                break;
            case R.id.img_me:


                Intent it2 = new Intent(this, MeActivity.class);  //跳转界面

                startActivity(it2);
                break;

//            case R.id.item_mainlv_top_tv_budget:
//                showBudgetDialog();
//                break;
//            case R.id.item_mainlv_top_iv_hide:
//                // 切换TextView明文和密文
//                toggleShow();
//                break;
        }
        if (v == headerView) {
            //头布局被点击了
            Intent intent = new Intent();
            intent.setClass(this, MonthChartActivity.class);
            startActivity(intent);
        }
    }
    /** 显示运算设置对话框*/
    private void showBudgetDialog() {
        BudgetDialog dialog = new BudgetDialog(this);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(new BudgetDialog.OnEnsureListener() {
            @Override
            public void onEnsure(float money) {
                //将数据写入到共享参数当中，进行存储
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("bmoney",money);
                editor.commit();
                //计算净吸收
                float outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
                float syMoney = money-outcomeOneMonth;//剩余 = 预算-支出
                topbudgetTv.setText(syMoney + "kg");
            }
        });
    }

    //正则表达式
    public  String getNum(String str) {
        String dest = "";
        if (str != null) {
            dest = str.replaceAll("[^0-9]","");

        }
        return dest;
    }

    static public String getNumber(String str){
        // 控制正则表达式的匹配行为的参数(小数)
        Pattern p = Pattern.compile("(\\d+\\.\\d+)");
        //Matcher类的构造方法也是私有的,不能随意创建,只能通过Pattern.matcher(CharSequence input)方法得到该类的实例.
        Matcher m = p.matcher(str);
        //m.find用来判断该字符串中是否含有与"(\\d+\\.\\d+)"相匹配的子串
        if (m.find()) {
            //如果有相匹配的,则判断是否为null操作
            //group()中的参数：0表示匹配整个正则，1表示匹配第一个括号的正则,2表示匹配第二个正则,在这只有一个括号,即1和0是一样的
            str = m.group(1) == null ? "" : m.group(1);
        } else {
            //如果匹配不到小数，就进行整数匹配
            p = Pattern.compile("(\\d+)");
            m = p.matcher(str);
            if (m.find()) {
                //如果有整数相匹配
                str = m.group(1) == null ? "" : m.group(1);
            } else {
                //如果没有小数和整数相匹配,即字符串中没有整数和小数，就设为空
                str = "";
            }
        }
        return str;
    }


    @SuppressLint("CheckResult")
    public List<AccountBean> httpGetAccountList(String username) {
        List<AccountBean> account = new ArrayList<>();

        String url = "http://192.168.43.196:8080/accountList/" + username;
        RxHttp.get(url)
                .asString()
                .subscribe(s -> {
                    Gson gson = new Gson();
                    AccountList accountList = gson.fromJson(s, AccountList.class);
                    account.addAll(accountList.getData());
                    //直接进行同步
                    DBManager.syncHttpData(account);
                    Log.d("QQ", "run: " + accountList.toString());

                }, throwable -> {
                    Log.d(TAG, "httpGetJson: ");
                    Log.d(TAG, "httpGetJson: "+throwable);
                    Toast.makeText(this, "获取信息失败" + throwable, Toast.LENGTH_SHORT).show();
                });
        return account;
    }

    // 当activity获取焦点时，会调用的方法
    @Override
    protected void onResume() {
        super.onResume();
        loadDBData();
        setTopTvShow();
    }

//    boolean isShow = true;
//    /**
//     * 点击头布局眼睛时，如果原来是明文，就加密，如果是密文，就显示出来
//     * */
//    private void toggleShow() {
//        if (isShow) {   //明文====》密文
//            PasswordTransformationMethod passwordMethod = PasswordTransformationMethod.getInstance();
//            topInTv.setTransformationMethod(passwordMethod);   //设置隐藏
//            topOutTv.setTransformationMethod(passwordMethod);   //设置隐藏
//            topbudgetTv.setTransformationMethod(passwordMethod);   //设置隐藏
//            topShowIv.setImageResource(R.mipmap.ih_hide);
//            isShow = false;   //设置标志位为隐藏状态
//        }else{  //密文---》明文
//            HideReturnsTransformationMethod hideMethod = HideReturnsTransformationMethod.getInstance();
//            topInTv.setTransformationMethod(hideMethod);   //设置隐藏
//            topOutTv.setTransformationMethod(hideMethod);   //设置隐藏
//            topbudgetTv.setTransformationMethod(hideMethod);   //设置隐藏
//            topShowIv.setImageResource(R.mipmap.ih_show);
//            isShow = true;   //设置标志位为隐藏状态
//        }
//    }
}
