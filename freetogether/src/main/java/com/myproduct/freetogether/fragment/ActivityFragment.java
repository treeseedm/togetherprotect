package com.myproduct.freetogether.fragment;

import android.os.Bundle;

import com.myapplication.yiqihi.constant.Constants;
import com.myapplication.yiqihi.http.MethodHelper;
import com.myapplication.yiqihi.task.YQHBaseTask;
import com.myproduct.freetogether.R;
import com.myproduct.freetogether.adapter.GuideAdapter;
import com.myproduct.freetogether.bean.Item;

import java.util.ArrayList;
import java.util.List;

import yiqihi.mobile.com.commonlib.BaseFragment;
import yiqihi.mobile.com.commonlib.customview.MyListView;

public class ActivityFragment extends BaseFragment {
    private MyListView myListView;
    public static final String CATALOG = "catalog";
    private int mCatalog;
    private GuideAdapter mGuideAdapter;
    private int currentPage = 1;
    private String mCountry = "";
    private String mLocation = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCatalog = getArguments().getInt(CATALOG);
        mCountry = getArguments().getString(Constants.COUNTRY);
        mLocation = getArguments().getString(Constants.LOCATION);
    }

    @Override
    public void initView() {
        mGuideAdapter = new GuideAdapter(getActivity());
        myListView = findViewByIdHelper(R.id.mlv_listview);
        myListView.setAdapter(mGuideAdapter);
        initData();

    }

    public void initData() {
        new YQHBaseTask(getActivity(), true, YQHBaseTask.ACTION_READACTIVITIES, MethodHelper.readActivities(currentPage, mCatalog, mCountry, mLocation).toString());
    }

    @Override
    public int getResource() {
        return R.layout.fragment_guide;
    }
}
