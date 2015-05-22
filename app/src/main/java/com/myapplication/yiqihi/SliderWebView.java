package com.myapplication.yiqihi;

import android.webkit.WebView;

import com.myapplication.yiqihi.event.MessageEvent;

import yiqihi.mobile.com.commonlib.BaseActivity;

public class SliderWebView extends BaseActivity {
    private WebView mWebView;
    private String url;
    @Override
    public int getResourceView() {
        return R.layout.activity_webview;
    }

    @Override
    public void initParams() {
        url=getIntent().getStringExtra("url");
    }

    @Override
    protected void initView() {
        mWebView=findViewByIdHelper(R.id.wv_slider);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void setData() {
       mWebView.loadUrl(url);
    }

}
