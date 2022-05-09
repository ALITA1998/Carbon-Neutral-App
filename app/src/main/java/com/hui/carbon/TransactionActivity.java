package com.hui.carbon;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hui.carbon.frag_transaction.HomeFragment;
import com.hui.carbon.frag_transaction.MyFragment;
import com.hui.carbon.frag_transaction.MyReleaseFragment;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

public class TransactionActivity extends AppCompatActivity {
    ArrayList<Fragment> fragments;
    private BottomNavigationView bottomNavigationView;
//    private Banner banner;
//    private List<Integer> images = new ArrayList<>();
    final FragmentManager fm = getSupportFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        fragments = new ArrayList<>();
        fragments.add(HomeFragment.newInstance());

        fragments.add(MyFragment.newInstance());

        fragments.add(MyReleaseFragment.newInstance());

        fm.beginTransaction().add(R.id.fragment_container, fragments.get(0), "HOME")
                .add(R.id.fragment_container, fragments.get(1), "MY")
                .add(R.id.fragment_container, fragments.get(2), "MYREL")
                .commit();
        fm.beginTransaction()
                .hide(fragments.get(1))
                .hide(fragments.get(2))
                .commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.tab_home:
                        fm.beginTransaction()
                                .hide(fragments.get(1))
                                .hide(fragments.get(2))
                                .show(fragments.get(0))
                                .commit();
                        return true;
                    case R.id.tab_my:
                        fm.beginTransaction()
                                .hide(fragments.get(0))
                                .hide(fragments.get(2))
                                .show(fragments.get(1))
                                .commit();
                        return true;
                }
                return false;
            }
        });
    }

}
