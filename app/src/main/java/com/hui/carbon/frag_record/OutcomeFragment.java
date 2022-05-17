package com.hui.carbon.frag_record;
import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import com.hui.carbon.R;
import com.hui.carbon.db.DBManager;
import com.hui.carbon.db.TypeBean;
import java.util.List;

import rxhttp.wrapper.param.RxHttp;

/**
 * A simple {@link Fragment} subclass.
 */
public class OutcomeFragment extends BaseRecordFragment {


    // 重写
    @Override
    public void loadDataToGV() {
        super.loadDataToGV();
        //获取数据库当中的数据源
        List<TypeBean> outlist = DBManager.getTypeList(0);
        typeList.addAll(outlist);
        adapter.notifyDataSetChanged();
        typeTv.setText("其他");
        typeIv.setImageResource(R.mipmap.ic_qita_fs);
    }

    @Override
    public void saveAccountToDB() {

        accountBean.setKind(0);
        //根据类型计算碳排放量
        String type = accountBean.getTypename();
        float carbonUnitData = DBManager.getCarbonUnitData(type);
        float money = accountBean.getMoney();
        float carbon_data = money * carbonUnitData;
        accountBean.setMoney(carbon_data);

        DBManager.insertItemToAccounttb(accountBean);
        httpInsertItemToAccountTb(accountBean, uniteApp.account);
        //更新碳配额
        uniteApp.carbon_balance -= carbon_data;
        httpUpdateUserCarbonBal(uniteApp.carbon_balance);
    }

    @SuppressLint("CheckResult")
    public void httpUpdateUserCarbonBal(Float carbon_balance) {

        String url = "http://192.168.43.196:8080/updateUserCarbonBal/"+uniteApp.account +"?user_carbon_bal=" + carbon_balance;
        RxHttp.get(url)
                .asString()
                .subscribe(s -> {
                    Log.d("QQQQQQQ", "用户信息更新成功！");
                }, throwable -> {
                    Toast.makeText(getActivity(), "用户信息更新失败！" + throwable, Toast.LENGTH_SHORT).show();
                });
    }
}
