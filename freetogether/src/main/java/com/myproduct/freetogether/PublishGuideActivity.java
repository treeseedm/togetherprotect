package com.myproduct.freetogether;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import yiqihi.mobile.com.commonlib.BaseActivity;
import yiqihi.mobile.com.commonlib.CommonUtility;
import yiqihi.mobile.com.commonlib.DataSelectListener;

public class PublishGuideActivity extends BaseActivity implements View.OnClickListener, DataSelectListener {
    private TextView tv_title,et_date;
    private ImageView iv_phone, iv_more;
    private ImageView iv_go_data, iv_go_car, iv_go_pingtan, iv_go_savetime, iv_add1, iv_add2, iv_add3;
    private EditText et_title, et_location, et_datanum, et_totalpeople, et_zhaomupeople, et_money, et_des, et_savetime;
    private RelativeLayout rl_myinfo, rl_yaoqiu;
    private CheckBox cb_agree;
    private SimpleDraweeView sdv_image1, sdv_image2, sdv_image3;
    private LinearLayout ll_publish, ll_managerpublish;

    @Override
    public int getResourceView() {
        return R.layout.activity_publish_guide;
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
        tv_title.setText("发布<拼导游>");

        iv_go_data=findViewByIdHelper(R.id.iv_go_data);
        iv_go_car=findViewByIdHelper(R.id.iv_go_car);
        iv_go_pingtan=findViewByIdHelper(R.id.iv_go_pingtan);
        iv_go_savetime=findViewByIdHelper(R.id.iv_go_savetime);
        iv_add1=findViewByIdHelper(R.id.iv_add1);
        iv_add2=findViewByIdHelper(R.id.iv_add2);
        iv_add3=findViewByIdHelper(R.id.iv_add3);

        et_title=findViewByIdHelper(R.id.et_title);
        et_location=findViewByIdHelper(R.id.et_location);
        et_date=findViewByIdHelper(R.id.et_date);
        et_datanum=findViewByIdHelper(R.id.et_datanum);
        et_totalpeople=findViewByIdHelper(R.id.et_totalpeople);
        et_zhaomupeople=findViewByIdHelper(R.id.et_zhaomupeople);
        et_money=findViewByIdHelper(R.id.et_money);
        et_des=findViewByIdHelper(R.id.et_des);
        et_savetime=findViewByIdHelper(R.id.et_savetime);

        rl_myinfo=findViewByIdHelper(R.id.rl_myinfo);
        rl_yaoqiu=findViewByIdHelper(R.id.rl_yaoqiu);

        cb_agree=findViewByIdHelper(R.id.cb_agree);

        sdv_image1=findViewByIdHelper(R.id.sdv_image1);
        sdv_image2=findViewByIdHelper(R.id.sdv_image2);
        sdv_image3=findViewByIdHelper(R.id.sdv_image3);

        ll_publish=findViewByIdHelper(R.id.ll_publish);
        ll_managerpublish=findViewByIdHelper(R.id.ll_managerpublish);
    }

    @Override
    protected void setListener() {
        iv_go_data.setOnClickListener(this);
        et_date.setOnClickListener(this);
//        iv_go_car=findViewByIdHelper(R.id.iv_go_car);
//        iv_go_pingtan=findViewByIdHelper(R.id.iv_go_pingtan);
//        iv_go_savetime=findViewByIdHelper(R.id.iv_go_savetime);
//        iv_add1=findViewByIdHelper(R.id.iv_add1);
//        iv_add2=findViewByIdHelper(R.id.iv_add2);
//        iv_add3=findViewByIdHelper(R.id.iv_add3);
    }

    @Override
    protected void setData() {

    }

    private PopupWindow mDataPickerPop;
    private View rootView;
    private void initDataPopView(boolean isShow){
        if(mDataPickerPop==null){
            rootView=getWindow().getDecorView().findViewById(android.R.id.content);
            View dataPopView= CommonUtility.initDateTimePicker(PublishGuideActivity.this,PublishGuideActivity.this);
            mDataPickerPop=new PopupWindow(dataPopView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mDataPickerPop.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_border));
            mDataPickerPop.showAtLocation(rootView, Gravity.CENTER,0,0);
        }else{
           if(isShow)
               mDataPickerPop.showAtLocation(rootView, Gravity.CENTER,0,0);
            else
               mDataPickerPop.dismiss();
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_go_data:
            case R.id.et_date:
                CommonUtility.showToast(PublishGuideActivity.this,"haha");
                initDataPopView(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onOKClick(String year, String month, String day) {
        String dateStr = year + "-" +month + "-"  + day;
        et_date.setText(dateStr);
        onCancleClick();
    }

    @Override
    public void onCancleClick() {
        if(mDataPickerPop!=null)
            mDataPickerPop.dismiss();
    }
}
