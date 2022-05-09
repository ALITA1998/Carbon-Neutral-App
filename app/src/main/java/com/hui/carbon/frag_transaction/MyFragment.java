package com.hui.carbon.frag_transaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hui.carbon.R;


import com.google.android.material.button.MaterialButton;
import com.hui.carbon.TransactionActivity;
import com.hui.carbon.UniteApp;
import com.leon.lib.settingview.LSettingItem;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFragment extends Fragment {
    ArrayList<Fragment> fragments;
    private TextView myUserName;
    private MaterialButton logOut;
    private LSettingItem mySell;
    private LSettingItem myBalance;
    private LSettingItem myRelease;
    UniteApp uniteApp;

    public MyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        uniteApp = (UniteApp) getActivity().getApplication();
        View view = inflater.inflate(R.layout.fragment_my, container, false);

        fragments = new ArrayList<>();
        fragments.add(HomeFragment.newInstance());

        fragments.add(MyFragment.newInstance());

        fragments.add(MyReleaseFragment.newInstance());

        myUserName = view.findViewById(R.id.frag_my_username);
        myUserName.setText(uniteApp.account);
//        logOut = view.findViewById(R.id.logOut);
        myBalance = view.findViewById(R.id.item_myBalance);
        mySell = view.findViewById(R.id.item_mySell);
        myRelease = view.findViewById(R.id.item_myRelease);

        mySell.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                Intent intent = new Intent(getActivity(), SellActivity.class);
                startActivity(intent);
            }
        });

        myRelease.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                //获得Fragmentmanager实例
                final FragmentManager fm = getActivity().getSupportFragmentManager();

                FragmentTransaction transaction = fm.beginTransaction();
                MyReleaseFragment myReleaseFragment = new MyReleaseFragment();
                transaction.replace(R.id.fragment_container, myReleaseFragment);
                transaction.commit();

            }
        });
        myBalance.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click(boolean isChecked) {
                //获得Fragmentmanager实例
                final FragmentManager fm = getActivity().getSupportFragmentManager();

                FragmentTransaction transaction = fm.beginTransaction();
                MyBalanceFragment myBalanceFragment = new MyBalanceFragment();
                transaction.replace(R.id.fragment_container, myBalanceFragment);
                transaction.commit();

            }
        });
//
//        SharedPreferences sp = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
//        myUserName.setText(sp.getString("name", ""));
//
//        logOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), LoginActivity.class);
//                SharedPreferences.Editor edit = sp.edit();
//                edit.putString("name", "");
//                edit.putString("password", "");
//                edit.apply();
//                startActivity(intent);
//            }
//        });
        return view;
    }


    public static MyFragment newInstance() {

        return new MyFragment();
    }
}
