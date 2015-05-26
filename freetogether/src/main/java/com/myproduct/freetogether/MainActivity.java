package com.myproduct.freetogether;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.myproduct.freetogether.adapter.ViewPagerAdapter;

import yiqihi.mobile.com.commonlib.BaseActivity;
import yiqihi.mobile.com.commonlib.customview.PagerSlidingTabStrip;


public class MainActivity extends BaseActivity {
    private PagerSlidingTabStrip mSlidingTab;
    private ViewPager mViewPager;
    private ViewPagerAdapter mAdapter;
    private String mCountry = "";
    private String mLocation = "";

    @Override
    public int getResourceView() {
        return R.layout.activity_main;
    }

    @Override
    public void initParams() {

    }

    @Override
    protected void initView() {
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this,mCountry,mLocation );
        mSlidingTab = findViewByIdHelper(R.id.pagerslidingtab);
        mViewPager = findViewByIdHelper(R.id.vp_freetogether);
        mViewPager.setAdapter(mAdapter);
        mSlidingTab.setViewPager(mViewPager);
        mSlidingTab.updateTextColor(0);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void setData() {

    }
}
