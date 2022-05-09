package com.hui.carbon.frag_transaction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hui.carbon.R;
import com.hui.carbon.entity.Goods;

import java.util.List;

/**
 * @Authur : Yancy
 * @E-mail : 986244073@qq.com
 * @Create : 2019/6/11
 * @Desc :
 */

public class GoodsListAdapter extends RecyclerView.Adapter<GoodsListAdapter.GoodsViewHolder> {
    private List<Goods> goods;
    private GoodClickListenner listenner;
    private Context context;
//    UniteApp uniteApp;

    public GoodsListAdapter(List<Goods> goods, GoodClickListenner listenner) {
        this.goods = goods;
        this.listenner = listenner;
    }

    @NonNull
    @Override
    public GoodsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
//        uniteApp = (UniteApp)parent.getContext().getApplicationContext();

        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.goods, parent, false);
        GoodsViewHolder holder = new GoodsViewHolder(row);
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenner.onClick(v, holder.getLayoutPosition());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull GoodsViewHolder holder, int position) {
        holder.bind(context, goods.get(position));

    }


    @Override
    public int getItemCount() {
        return goods.size();
    }

    public class GoodsViewHolder extends RecyclerView.ViewHolder {
        private TextView carbon_balance;
        private ImageView img;
        private TextView price;
        private TextView goods_user_name;


        public GoodsViewHolder(@NonNull View itemView) {
            super(itemView);
            carbon_balance = itemView.findViewById(R.id.carbon_balance);
//            img = itemView.findViewById(R.id.image);
            goods_user_name = itemView.findViewById(R.id.goods_user_name);
            price = itemView.findViewById(R.id.price);

        }

        public void bind(Context context, Goods goods) {
            carbon_balance.setText(goods.getCarbon_balance().toString());
//            goods_user_name.setText(uniteApp.account);
            goods_user_name.setText(goods.getName());
            price.setText("RMB："+goods.getPrice().toString());
//            Glide.with(context).load(goods.getIcon()).override(320, 320).into(img);
        }
    }
}
