package com.myproduct.freetogether;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myproduct.freetogether.adapter.GridViewAdapter;
import com.myproduct.freetogether.bean.CheckItem;
import com.myproduct.freetogether.bean.Detail;

import org.apmem.tools.layouts.FlowLayout;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import yiqihi.mobile.com.commonlib.BaseActivity;
import yiqihi.mobile.com.commonlib.CommonUtility;
import yiqihi.mobile.com.commonlib.PickerSelectListener;
import yiqihi.mobile.com.commonlib.customview.DisScrollGridView;

public class DetailActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_title;
    private ImageView iv_phone, iv_more;

    private TextView tv_sex, tv_age, tv_height, tv_lang,tv_yaoqiu;
    private ImageView iv_go_sex, iv_go_age, iv_go_height, iv_go_lang;
    private DisScrollGridView job;
    private EditText et_des;
    private Button btn_ok;
    private RelativeLayout rl_sex, rl_age, rl_height, rl_lang;
    private GridViewAdapter mAdapter;


    @Override
    public int getResourceView() {
        return R.layout.activity_demand;
    }

    @Override
    public void initParams() {

    }

    @Override
    protected void initView() {
        iv_phone = findViewByIdHelper(R.id.iv_phone);
        iv_phone.setVisibility(View.VISIBLE);
        iv_more = findViewByIdHelper(R.id.iv_more);
        iv_more.setImageResource(R.drawable.icon_more);
        tv_title = findViewByIdHelper(R.id.tv_title);
        tv_title.setText("对同伴的要求");

        rl_sex = findViewByIdHelper(R.id.rl_sex);
        rl_age = findViewByIdHelper(R.id.rl_age);
        rl_height = findViewByIdHelper(R.id.rl_height);
        rl_lang = findViewByIdHelper(R.id.rl_lang);

        tv_sex = findViewByIdHelper(R.id.tv_sex);
        tv_age = findViewByIdHelper(R.id.tv_age);
        tv_height = findViewByIdHelper(R.id.tv_height);
        tv_lang = findViewByIdHelper(R.id.tv_lang);

        iv_go_sex = findViewByIdHelper(R.id.iv_go_sex);
        iv_go_age = findViewByIdHelper(R.id.iv_go_age);
        iv_go_height = findViewByIdHelper(R.id.iv_go_height);
        iv_go_lang = findViewByIdHelper(R.id.iv_go_lang);

        job = findViewByIdHelper(R.id.gv_job);
        mAdapter = new GridViewAdapter(this, false);
        List<CheckItem> listItem = new ArrayList<CheckItem>();
        for (int i = 0; i < Detail.jobValues.length; i++) {
            CheckItem item = new CheckItem();
            if (i == 0)
                item.selected = true;
            else
                item.selected = false;
            item.value = Detail.jobValues[i];
            listItem.add(item);
        }
        mAdapter.refresh(listItem);
        job.setAdapter(mAdapter);
        et_des = findViewByIdHelper(R.id.et_des);
        btn_ok = findViewByIdHelper(R.id.btn_ok);

        tv_yaoqiu=findViewByIdHelper(R.id.tv_yaoqiu);
    }

    @Override
    protected void setListener() {
        rl_sex.setOnClickListener(this);
        rl_age.setOnClickListener(this);
        rl_height.setOnClickListener(this);
        rl_lang.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
    }

    @Override
    protected void setData() {

    }

    private PopupWindow mSexPop;
    private PopupWindow mAgePop;
    private PopupWindow mHeightPop;
    private PopupWindow mLangPop;

    private void initCarPickerPop(PopupWindow popupWindow, String[] values, final TextView textView) {
        if (popupWindow == null) {
            popupWindow = new PopupWindow( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            final PopupWindow finalPopupWindow = popupWindow;
            View dataPopView = CommonUtility.InitTextPicker(DetailActivity.this, new PickerSelectListener() {
                @Override
                public void onOKClick(String year, String month, String day) {

                }

                @Override
                public void onCancleClick() {
                    finalPopupWindow.dismiss();
                }

                @Override
                public void onOKClick(int index, String value) {
                    finalPopupWindow.dismiss();
                    textView.setText(value);
                    textView.setTag(index);
                }
            }, values);
            popupWindow.setContentView(dataPopView);
            popupWindow.showAtLocation(findViewByIdHelper(R.id.ll_parent), Gravity.CENTER, 0, 0);
        } else {
            popupWindow.showAtLocation(findViewByIdHelper(R.id.ll_parent), Gravity.CENTER, 0, 0);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_sex:
                initCarPickerPop(mSexPop, Detail.sexValues, tv_sex);
                break;
            case R.id.rl_age:
                initCarPickerPop(mAgePop, Detail.ageValues, tv_age);
                break;
            case R.id.rl_height:
                initCarPickerPop(mHeightPop, Detail.heightValues, tv_height);
                break;
            case R.id.rl_lang:
                initCarPickerPop(mLangPop, Detail.langValues, tv_lang);
                break;
            case R.id.btn_ok:
                save();
                break;
            default:
                break;
        }
    }

    private void save() {
        try {
            Detail detail = new Detail();
            if (tv_sex.getTag() != null)
                detail.sex = Integer.valueOf(tv_sex.getTag().toString());
            if (tv_age.getTag() != null)
                detail.age = Integer.valueOf(tv_age.getTag().toString());
            if (tv_height.getTag() != null)
                detail.height = Integer.valueOf(tv_height.getTag().toString());
            if (tv_lang.getTag() != null)
                detail.lang = Integer.valueOf(tv_lang.getTag().toString());
            detail.note = et_des.getText().toString();
            detail.job = mAdapter.getSelectPosition();
            detail.detail = tv_sex.getText() + "  " + tv_height.getText() + "  " + mAdapter.getSelectValue() + detail.note;
            Intent intent = getIntent();
            intent.putExtra("detail", detail);
            setResult(2, intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
