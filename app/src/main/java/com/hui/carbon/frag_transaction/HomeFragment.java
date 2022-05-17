package com.hui.carbon.frag_transaction;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.hui.carbon.R;
import com.hui.carbon.GoodsActivity;
import com.hui.carbon.entity.Goods;
import com.hui.carbon.entity.GoodsList;
import com.youth.banner.Banner;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

import rxhttp.wrapper.param.RxHttp;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Integer> images = new ArrayList<>();
    private GoodsListAdapter listAdapter;
    private Banner banner;
    private RecyclerView shopList;
    private List<Goods> data;
    final String TAG = "QQQ";

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = httpGetJson();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        initData();

        data = httpGetJson();

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        banner = view.findViewById(R.id.banner);
        shopList = view.findViewById(R.id.shopList);
        swipeRefreshLayout = view.findViewById(R.id.refreshLayout);

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        data.add(0, data);
//                        listAdapter.notifyDataSetChanged();
                        data = httpGetJson();

                        listAdapter = new GoodsListAdapter(data, new GoodClickListener() {
                            @Override
                            public void onClick(View v, int position) {
                                Log.i("FFPLAY", "Clicked " + view + " on " + position);
//                Intent intent = new Intent(getActivity(), GoodsActivity.class);
//                intent.putExtra("goods", data.get(position));
                                if (position < data.size()) {
                                    Goods good = data.get(position);
                                    Intent intent = new Intent(getActivity(), GoodsActivity.class);
                                    intent.putExtra("goods", data.get(position));
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getActivity(), "点击失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        listAdapter.notifyDataSetChanged();
//                        shopList.setLayoutManager(new LinearLayoutManager(getActivity()));
                        swipeRefreshLayout.setRefreshing(false);


                    }
                }, 3000);
                listAdapter.notifyDataSetChanged();
            }

        });

        Log.d("QQQQQ", "onCreateView: " + data);
        listAdapter = new GoodsListAdapter(data, new GoodClickListener() {
            @Override
            public void onClick(View v, int position) {
                Log.i("FFPLAY", "Clicked " + view + " on " + position);
//                Intent intent = new Intent(getActivity(), GoodsActivity.class);
//                intent.putExtra("goods", data.get(position));
                if (position < data.size()) {
                    Goods good = data.get(position);
                    Intent intent = new Intent(getActivity(), GoodsActivity.class);
                    intent.putExtra("goods", data.get(position));
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "点击失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        shopList.setAdapter(listAdapter);

        shopList.setLayoutManager(new LinearLayoutManager(getActivity()));
        images.add(R.drawable.banner1);
        images.add(R.drawable.banner2);


        banner.setImageLoader(new GlideImageLoader());
        banner.setBannerAnimation(Transformer.DepthPage);

//        banner.setImageLoader(new GlideImageLoader(this,));
        banner.setImages(images);
        banner.start();
        return view;
    }

//    private void initData() {
////        DataLab lab = new DataLab(getContext());
////        this.data = lab.getGoods("data.json");
//        this.data = httpGetJson();
//    }

    public static HomeFragment newInstance() {

        return new HomeFragment();
    }

    @SuppressLint("CheckResult")
    public List<Goods> httpGetJson() {
        List<Goods> goods = new ArrayList<>();
//        String url = "http://39.105.14.128:8080/list";
            String url = "http://192.168.43.196:8080/list";
        RxHttp.get(url)
                .asString()
                .subscribe(s -> {
                    Gson gson = new Gson();
                    GoodsList goodsList = gson.fromJson(s, GoodsList.class);
                    goods.addAll(goodsList.getData());
                    Log.d("QQ", "run: " + goods.toString());

                }, throwable -> {
                    Log.d(TAG, "httpGetJson: ");
                    Log.d(TAG, "httpGetJson: "+throwable);
                    Toast.makeText(getActivity(), "获取信息失败" + throwable, Toast.LENGTH_SHORT).show();
                });
        return goods;
    }
}
