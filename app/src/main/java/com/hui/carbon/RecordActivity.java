package com.hui.carbon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.tabs.TabLayout;
import com.hui.carbon.adapter.RecordPagerAdapter;
import com.hui.carbon.frag_record.IncomeFragment;
import com.hui.carbon.frag_record.OutcomeFragment;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    EditText carbonData;
    UniteApp app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        //1.查找控件
        tabLayout = findViewById(R.id.record_tabs);
        viewPager = findViewById(R.id.record_vp);
        carbonData = findViewById(R.id.frag_record_et_money);
        app = (UniteApp) getApplication();
        //2.设置ViewPager加载页面
        initPager();
    }

    private void initPager() {
//        初始化ViewPager页面的集合
        List<Fragment>fragmentList = new ArrayList<>();
//        创建吸收和排放页面，放置在Fragment当中
        OutcomeFragment outFrag = new OutcomeFragment(); //支出
        IncomeFragment inFrag = new IncomeFragment(); //收入
        fragmentList.add(outFrag);
        fragmentList.add(inFrag);

//        创建适配器
        RecordPagerAdapter pagerAdapter = new RecordPagerAdapter(getSupportFragmentManager(), fragmentList);
//        设置适配器
        viewPager.setAdapter(pagerAdapter);
        //将TabLayout和ViwePager进行关联
        tabLayout.setupWithViewPager(viewPager);
    }

    /* 点击事件*/
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.record_iv_back:
                finish();
                break;
        }
    }



}
