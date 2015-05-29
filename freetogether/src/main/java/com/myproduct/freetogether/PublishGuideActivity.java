package com.myproduct.freetogether;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import yiqihi.mobile.com.commonlib.BaseActivity;

public class PublishGuideActivity extends BaseActivity {
    private TextView tv_title;
    private ImageView iv_phone, iv_more;

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
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void setData() {

    }
}
