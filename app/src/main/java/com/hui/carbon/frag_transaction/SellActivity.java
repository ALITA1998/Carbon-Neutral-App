package com.hui.carbon.frag_transaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hui.carbon.R;
import com.hui.carbon.UniteApp;
import com.hui.carbon.entity.Goods;
import com.google.android.material.textfield.TextInputEditText;
import com.rxjava.rxlife.RxLife;

import rxhttp.wrapper.param.RxHttp;

public class SellActivity extends AppCompatActivity {
    private String goods_name;
    private TextInputEditText goods_price;
    private TextInputEditText goods_desc;
    private TextInputEditText goods_carbon_balance;

    private Button btn_sell;

    UniteApp uniteApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uniteApp = (UniteApp)getApplication();
        setContentView(R.layout.activity_sell);
        goods_name = uniteApp.account;
        goods_price = findViewById(R.id.goods_price);
        goods_desc = findViewById(R.id.goods_desc);
        goods_carbon_balance = findViewById(R.id.goods_carbon_balance);
//        goode_icon = findViewById(R.id.goods_icon);

        btn_sell = findViewById(R.id.sell);

        btn_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = goods_name;
                if(name.equals("游客")){
                    Toast.makeText(SellActivity.this, "请先登录！", Toast.LENGTH_SHORT).show();
                    return;
                }
                String price = goods_price.getText().toString().trim();
                String desc = goods_desc.getText().toString().trim();
                String carbon_balance = goods_carbon_balance.getText().toString().trim();
//                String icon = goode_icon.getText().toString().trim();
                String url = "http://192.168.43.196:8080/add?name=" + name + "&price=" + price +  "&carbon_balance=" +carbon_balance + "&description=" + desc;
                RxHttp.get(url).asObject(Goods.class).as(RxLife.asOnMain(SellActivity.this))
                        .subscribe(s -> {
                            Log.d("q", "onClick: "+s.toString());
                            Toast.makeText(SellActivity.this, "发布成功!", Toast.LENGTH_SHORT).show();
                        }, throwable -> {
                            Log.d("q", "onClick: "+throwable);
                        });

            }
        });
    }
}
