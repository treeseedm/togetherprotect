package com.myproduct.freetogether;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myapplication.yiqihi.constant.Constants;
import com.myproduct.freetogether.adapter.ViewPagerAdapter;

import yiqihi.mobile.com.commonlib.BaseActivity;
import yiqihi.mobile.com.commonlib.customview.PagerSlidingTabStrip;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG="MainActivity";
    private PagerSlidingTabStrip mSlidingTab;
    private ViewPager mViewPager;
    private ViewPagerAdapter mAdapter;
    private String mCountry = "";
    private String mLocation = "";
    private RelativeLayout rl_search;
    private String mAction;
    private TextView tv_title;
    @Override
    public int getResourceView() {
        return R.layout.activity_main;
    }

    @Override
    public void initParams() {
        mAction = getIntent().getAction();
        mCountry=getIntent().getStringExtra(Constants.COUNTRY);
        mLocation=getIntent().getStringExtra(Constants.LOCATION);
    }

    @Override
    protected void initView() {
        rl_search = findViewByIdHelper(R.id.rl_search);
        if ("searchActivity".equals(mAction)) {
            rl_search.setVisibility(View.GONE);
        } else {
            rl_search.setVisibility(View.VISIBLE);
        }
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this, mCountry, mLocation);
        mSlidingTab = findViewByIdHelper(R.id.pagerslidingtab);
        mViewPager = findViewByIdHelper(R.id.vp_freetogether);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(5);
        mSlidingTab.setViewPager(mViewPager);
        mSlidingTab.updateTextColor(0);
        tv_title=findViewByIdHelper(R.id.tv_title);
        tv_title.setText("自由结伴");
    }

    @Override
    protected void setListener() {
        rl_search.setOnClickListener(this);
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
        }
    }
}
