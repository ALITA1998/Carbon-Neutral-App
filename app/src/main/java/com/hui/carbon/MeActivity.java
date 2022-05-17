package com.hui.carbon;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hui.carbon.bar.NavBar;
import com.hui.carbon.db.DBManager;


public class MeActivity extends Activity implements View.OnClickListener {

    NavBar account;
    NavBar transaction;
    NavBar version;
    NavBar about;
    NavBar logs;

    Button exit_account;

    long time = -2000;
    TextView user_name;
    UniteApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);


        app = (UniteApp) this.getApplication();
        user_name = findViewById(R.id.user_name);
        user_name.setText(app.account);


        account = findViewById(R.id.account);

        transaction = findViewById(R.id.transaction);

        exit_account =findViewById(R.id.exit_account);
        about = findViewById(R.id.about);
        logs = findViewById(R.id.logs);
        account.setOnClickListener(this);
        transaction.setOnClickListener(this);
        exit_account.setOnClickListener(this);
        logs.setOnClickListener(this);
        about.setOnClickListener(this);

    }

    //点击事件的处理
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.account:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;

            case R.id.exit_account:
                new AlertDialog. Builder(this)
                        .setTitle("提示")
                        .setIcon(R.drawable.user_picture)
                        .setMessage("当前用户为：" + app.account +"\n确定退出登录吗？")
                        .setPositiveButton("确定", new DialogInterface. OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    app.account = "游客";

                                    Toast.makeText(MeActivity.this, "已退出用户！", Toast.LENGTH_SHORT).show();
                                    user_name.setText("当前用户：" + app.account);
                                } catch (Exception e) {
                                    Toast.makeText(MeActivity.this, "系统出错！", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }

                            }})
                        .setNegativeButton("取消",null)
                        .create()
                        .show();

                break;
            case R.id.transaction:
                intent = new Intent(this, TransactionActivity.class);
                startActivity(intent);
                break;

            case R.id.about:
                Intent it1 = new Intent(this, AboutActivity.class);
                startActivity(it1);
                break;

            case R.id.logs:

                showDeleteDialog();
                break;
        }
    }

    public void onResume() {
        super.onResume();
        user_name.setText("当前用户：" + app.account);

    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除提示")
                .setMessage("您确定要删除所有记录么？\n注意：删除后无法恢复，请慎重选择！")
                .setPositiveButton("取消",null)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBManager.deleteAllAccount();
                        Toast.makeText(MeActivity.this,"删除成功！",Toast.LENGTH_SHORT).show();
                    }
                });
        builder.create().show();
    }
}