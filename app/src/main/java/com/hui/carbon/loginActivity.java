package com.hui.carbon;


import android.accounts.Account;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.hui.carbon.db.AccountBean;
import com.hui.carbon.db.DBManager;
import com.hui.carbon.entity.AccountList;
import com.hui.carbon.entity.Goods;
import com.hui.carbon.entity.GoodsList;
import com.hui.carbon.entity.User;
import com.rxjava.rxlife.RxLife;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import rxhttp.wrapper.param.RxHttp;
/**
 * Created by Liuli 2022/4/10
 */

/**
 * 此类 implements View.OnClickListener 之后，
 * 就可以把onClick事件写到onCreate()方法之外
 * 这样，onCreate()方法中的代码就不会显得很冗余
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 声明自己写的 DBOpenHelper 对象
     * DBOpenHelper(extends SQLiteOpenHelper) 主要用来
     * 创建数据表
     * 然后再进行数据表的增、删、改、查操作
     */

    private TextView mTvLoginactivityRegister;
    private RelativeLayout mRlLoginactivityTop;
    private EditText mEtLoginactivityUsername;
    private EditText mEtLoginactivityPassword;
    private LinearLayout mLlLoginactivityTwo;
    private Button mBtLoginactivityLogin;

    UniteApp uniteApp;
    final String TAG = "Login";
    /**
     * 创建 Activity 时先来重写 onCreate() 方法
     * 保存实例状态
     * super.onCreate(savedInstanceState);
     * 设置视图内容的配置文件
     * setContentView(R.layout.activity_login);
     * 上面这行代码真正实现了把视图层 View 也就是 layout 的内容放到 Activity 中进行显示
     * 初始化视图中的控件对象 initView()
     * 实例化 DBOpenHelper，待会进行登录验证的时候要用来进行数据查询
     * mDBOpenHelper = new DBOpenHelper(this);
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();


    }

    /**
     * onCreae()中大的布局已经摆放好了，接下来就该把layout里的东西
     * 声明、实例化对象然后有行为的赋予其行为
     * 这样就可以把视图层View也就是layout 与 控制层 Java 结合起来了
     */
    private void initView() {
        // 初始化控件
        mBtLoginactivityLogin = findViewById(R.id.bt_loginactivity_login);
        mTvLoginactivityRegister = findViewById(R.id.tv_loginactivity_register);
        mRlLoginactivityTop = findViewById(R.id.rl_loginactivity_top);
        mEtLoginactivityUsername = findViewById(R.id.et_loginactivity_username);
        mEtLoginactivityPassword = findViewById(R.id.et_loginactivity_password);
        mLlLoginactivityTwo = findViewById(R.id.ll_loginactivity_two);

        // 设置点击事件监听器
        mBtLoginactivityLogin.setOnClickListener(this);
        mTvLoginactivityRegister.setOnClickListener(this);

        //控制全局变量
        uniteApp = (UniteApp) getApplication();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            // 跳转到注册界面
            case R.id.tv_loginactivity_register:
                startActivity(new Intent(this, RegisterActivity.class));
                finish();
                break;
            /**
             * 登录验证：
             *
             * 从EditText的对象上获取文本编辑框输入的数据，并把左右两边的空格去掉
             *  String name = mEtLoginactivityUsername.getText().toString().trim();
             *  String password = mEtLoginactivityPassword.getText().toString().trim();
             *  进行匹配验证,先判断一下用户名密码是否为空，
             *  if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password))
             *  再进而for循环判断是否与数据库中的数据相匹配
             *  if (name.equals(user.getName()) && password.equals(user.getPassword()))
             *  一旦匹配，立即将match = true；break；
             *  否则 一直匹配到结束 match = false；
             *
             *  登录成功之后，进行页面跳转：
             *
             *  Intent intent = new Intent(this, MainActivity.class);
             *  startActivity(intent);
             *  finish();//销毁此Activity
             */
            case R.id.bt_loginactivity_login:
                login();
//                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
//                    ArrayList<User> data = mDBOpenHelper.getAllUserData();
//                    boolean match = false;
//                    for (int i = 0; i < data.size(); i++) {
//                        User user = data.get(i);
//                        if (name.equals(user.getName()) && password.equals(user.getPassword())) {
//                            match = true;
//                            break;
//                        } else {
//                            match = false;
//                        }
//                    }
//                    if (match) {
//                        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(this, MainActivity.class);
//                        startActivity(intent);
//                        finish();//销毁此Activity
//                    } else {
//                        Toast.makeText(this, "用户名或密码不正确，请重新输入", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(this, "请输入你的用户名或密码", Toast.LENGTH_SHORT).show();
//                }

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Socket socket = new Socket("103.46.128.53", 43872);  //服务端实例化socket对象
//
//                            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream(), "utf-8");
//                            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream(), "utf-8");
//                            //发送数据 格式为（登录，账号，密码）
//                            outputStreamWriter.write("in " + name + " " + password);
//                            outputStreamWriter.flush();
//
//                            //收到信息0代表是登录失败，1代表登录成功
//                            char[] a = new char[10];
//                            int length = inputStreamReader.read(a);
//
//                            if (a[0] == '1') {
//                                app.account = name;
//
//                                finish();
//
//                                Looper.prepare();
//                                Toast.makeText(loginActivity.this, "登录成功！欢迎回来!", Toast.LENGTH_LONG).show();
//                                Looper.loop();
//
//                            } else if (a[0] == '0') {
//
//                                //注册成功得到id
//                                String[] strings = (String.valueOf(a, 0, length)).split(" ");
//
//                                Looper.prepare();
//                                Toast.makeText(loginActivity.this, "账号或密码不正确!请重试！", Toast.LENGTH_LONG).show();
//                                Looper.loop();
//                            } else {
//                                Looper.prepare();
//                                Toast.makeText(loginActivity.this, "系统连接错误！", Toast.LENGTH_LONG).show();
//                                Looper.loop();
//                            }
//                            outputStreamWriter.close();
//                            inputStreamReader.close();
//                            socket.close();
//                        } catch (IOException e) {
//                            Looper.prepare();
//                            Toast.makeText(loginActivity.this, "系统连接错误！", Toast.LENGTH_LONG).show();
//                            Looper.loop();
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();

                break;
        }
    }

    public void login(){
        final String name = mEtLoginactivityUsername.getText().toString().trim();
        final String password = mEtLoginactivityPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name) | TextUtils.isEmpty(password)) {
            Toast.makeText(this, "请输入账号 or 密码", Toast.LENGTH_SHORT).show();
            return;
        }
//        String url = "https://www.easy-mock.com/mock/5cf2759064873463eb866334/sell/login";
        String url = "http://192.168.43.196:8080/login/" + name;
        RxHttp.get(url)
//                .asString()
                .asObject(User.class)
                .as(RxLife.asOnMain(this))
                .subscribe(s -> {
                    if (s.getName().equals(name) && s.getPassword().equals(password)) {
                        Toast.makeText(LoginActivity.this, "登陆成功! " + name, Toast.LENGTH_SHORT).show();
                        //设置用户全局信息，用于其他界面的初始化
                        uniteApp.account = name;
                        uniteApp.carbon_balance =s.getUser_carbon_bal();
                        uniteApp.money = s.getMoney();

                        // 登录成功后开始同步数据库
                        httpGetAccountList(uniteApp.account);
//                       List<AccountBean> accountBeanList = httpGetAccountList(uniteApp.account);
//                        DBManager.syncHttpData(accountBeanList);
//                        Log.d("ACCCCCCCC", accountBeanList.toString());
//                        intent.putExtra("user", (Parcelable) s);
                        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString("name", s.getName());
                        edit.putString("password", s.getPassword());
                        edit.apply();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(LoginActivity.this, "账号或者密码错误! " , Toast.LENGTH_SHORT).show();
                    }
                    Log.d(TAG, "login: " + s.toString());
                }, throwable -> {
                    Toast.makeText(this, "登陆失败~" + throwable, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "login: " + throwable);
                    Log.d(TAG, "login: " + name);
                });
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

                    Log.d("QQ", "run: " + account.toString());

                }, throwable -> {
                    Log.d(TAG, "httpGetJson: ");
                    Log.d(TAG, "httpGetJson: "+throwable);
                    Toast.makeText(this, "获取信息失败" + throwable, Toast.LENGTH_SHORT).show();
                });
        return account;
    }

}



