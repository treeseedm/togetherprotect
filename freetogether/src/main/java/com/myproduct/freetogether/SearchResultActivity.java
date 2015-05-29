package com.myproduct.freetogether;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myapplication.yiqihi.constant.Constants;
import com.myproduct.freetogether.adapter.ViewPagerAdapter;

import yiqihi.mobile.com.commonlib.BaseActivity;
import yiqihi.mobile.com.commonlib.customview.PagerSlidingTabStrip;


public class SearchResultActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private PagerSlidingTabStrip mSlidingTab;
    private ViewPager mViewPager;
    private ViewPagerAdapter mAdapter;
    private String mCountry = "";
    private String mLocation = "";
    private TextView tv_title;
    private LinearLayout ll_manager, ll_publish, ll_managerpublish;

    @Override
    public int getResourceView() {
        return R.layout.activity_search_result;
    }

    @Override
    public void initParams() {
        mCountry = getIntent().getStringExtra(Constants.COUNTRY);
        mLocation = getIntent().getStringExtra(Constants.LOCATION);
    }

    @Override
    protected void initView() {
        ll_manager = findViewByIdHelper(R.id.ll_manager);
        ll_publish = findViewByIdHelper(R.id.ll_publish);
        ll_managerpublish = findViewByIdHelper(R.id.ll_managerpublish);
        ll_manager.setVisibility(View.VISIBLE);

        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this, mCountry, mLocation);
        mSlidingTab = findViewByIdHelper(R.id.pagerslidingtab);
        mViewPager = findViewByIdHelper(R.id.vp_freetogether);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(5);
        mSlidingTab.setViewPager(mViewPager);
        mSlidingTab.updateTextColor(0);
        tv_title = findViewByIdHelper(R.id.tv_title);
        tv_title.setText("自由结伴");
    }

    @Override
    protected void setListener() {
        ll_publish.setOnClickListener(this);
        ll_managerpublish.setOnClickListener(this);
    }

    @Override
    protected void setData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.ll_publish:
                startActivity(new Intent(this, PublishGuideActivity.class));
                break;
            case R.id.ll_managerpublish:
                break;
            default:
                break;
        }
    }
}
