package com.hui.carbon.frag_record;
import androidx.fragment.app.Fragment;
import com.hui.carbon.R;
import com.hui.carbon.db.DBManager;
import com.hui.carbon.db.TypeBean;
import java.util.List;
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
    }
}
