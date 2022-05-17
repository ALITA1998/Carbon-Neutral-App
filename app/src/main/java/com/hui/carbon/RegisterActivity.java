package com.hui.carbon;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hui.carbon.entity.User;
import com.rxjava.rxlife.RxLife;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import rxhttp.wrapper.param.RxHttp;
/**
 * Created by liuli 2022/4/10
 */

/**
 * 此类 implements View.OnClickListener 之后，
 * 就可以把onClick事件写到onCreate()方法之外
 * 这样，onCreate()方法中的代码就不会显得很冗余
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private String realCode;

    private Button mBtRegisteractivityRegister;
    private RelativeLayout mRlRegisteractivityTop;
    private ImageView mIvRegisteractivityBack;
    private LinearLayout mLlRegisteractivityBody;
    private EditText mEtRegisteractivityUsername;
    private EditText mEtRegisteractivityPassword1;
    private EditText mEtRegisteractivityPassword2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();



    }

    private void initView(){
        mBtRegisteractivityRegister = findViewById(R.id.bt_registeractivity_register);
        mRlRegisteractivityTop = findViewById(R.id.rl_registeractivity_top);
        mIvRegisteractivityBack = findViewById(R.id.iv_registeractivity_back);
        mLlRegisteractivityBody = findViewById(R.id.ll_registeractivity_body);
        mEtRegisteractivityUsername = findViewById(R.id.et_registeractivity_username);
        mEtRegisteractivityPassword1 = findViewById(R.id.et_registeractivity_password1);
        mEtRegisteractivityPassword2 = findViewById(R.id.et_registeractivity_password2);

        /**
         * 注册页面能点击的就三个地方
         * top处返回箭头、刷新验证码图片、注册按钮
         */
        mIvRegisteractivityBack.setOnClickListener(this);

        mBtRegisteractivityRegister.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_registeractivity_back: //返回登录页面
                Intent intent1 = new Intent(this, LoginActivity.class);
                startActivity(intent1);
                finish();
                break;

            case R.id.bt_registeractivity_register:    //注册按钮
                //获取用户输入的用户名、密码、验证码
                final String username = mEtRegisteractivityUsername.getText().toString().trim();
                final String password = mEtRegisteractivityPassword1.getText().toString().trim();
                String affirm_str = mEtRegisteractivityPassword2.getText().toString().trim();

                //注册验证
                if (username.equals("") || password.equals("")) {
                    Toast.makeText(RegisterActivity.this, "请输入账号和密码！", Toast.LENGTH_SHORT).show();
                } else if (!affirm_str.equals(password)) {

                    Toast.makeText(RegisterActivity.this, "两次输入的密码不一样，请重试！", Toast.LENGTH_LONG).show();
                }else{
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                Socket socket = new Socket("103.46.128.53", 43872);   //服务端实例化socket对象
//                                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream(), "utf-8");
//                                InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream(), "utf-8");
//                                //发送数据 格式为（登录，账号，密码）
//                                outputStreamWriter.write("up " + username+ " " + password);
//                                System.out.println("up " + username+ " " + password);
//                                outputStreamWriter.flush();
//
//                                //收到信息0代表是登录失败，1代表登录成功
//                                final char[] a = new char[10];
//                                final int length = inputStreamReader.read(a);
//
//                                if(a[0]=='0') {
//                                    Looper.prepare();
//                                    Toast. makeText(RegisterActivity. this, "用户名重复！换个用户名试试吧!", Toast. LENGTH_LONG). show();
//                                    Looper.loop();
//                                } else if(a[0]=='1') {
//                                    Looper.prepare();
//                                    new AlertDialog.Builder(RegisterActivity.this)
////                                            .setIcon(R.drawable.wait)
//                                            .setTitle("提示")
//                                            .setMessage("是否使用本注册账号登录")
//                                            .setPositiveButton("是",                                     new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    //注册成功得到id
//                                                    String[] strings = (String.valueOf(a, 0, length)).split(" ");
//                                                    System.out.println(strings[0]);
//                                                    System.out.println(strings[1]);
//
//                                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                                                    intent.putExtra("name", username);
//                                                    startActivity(intent);
//                                                    //销毁这个activity
//                                                    finish();
////                                                    Looper.prepare();
//                                                    Toast.makeText(RegisterActivity. this, "注册成功!请返回登录!", Toast. LENGTH_LONG). show();
////                                                    Looper.loop();
//                                                }
//                                            })
//
//                                            .setNegativeButton("否",                                     new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialog, int which) {
//                                                    finish();
//                                                }
//                                            })
//                                            .create()
//                                            .show();
//                                    Looper.loop();
//                                }
//                                outputStreamWriter.close();
//                                inputStreamReader.close();
//                                socket.close();
//                            } catch (IOException e) {
//                                Looper.prepare();
//                                Toast.makeText(RegisterActivity.this, "系统连接错误！", Toast.LENGTH_LONG).show();
//                                Looper.loop();
//                                e.printStackTrace();
//                            }
//                        }
//                    }).start();
                    if (TextUtils.isEmpty(username) | TextUtils.isEmpty(password)) {
                        Toast.makeText(RegisterActivity.this, "请输入账号 or 密码", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String url = "http://192.168.43.196:8080/login?name=" + username + "&password=" + password;
                    RxHttp.get(url)
                            .asObject(User.class)
                            .as(RxLife.asOnMain(RegisterActivity.this))
                            .subscribe(s -> {
                                if(s.getName().equals("0")){
                                    Toast. makeText(RegisterActivity. this, "用户名重复！换个用户名试试吧!", Toast. LENGTH_LONG). show();
                                }else {
                                    Toast.makeText(RegisterActivity.this, "注册成功! " + username, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                }
                            }, throwable -> {
                                Toast.makeText(RegisterActivity.this, "失败"+throwable, Toast.LENGTH_SHORT).show();
                            });
                }
                break;
        }
    }
}

