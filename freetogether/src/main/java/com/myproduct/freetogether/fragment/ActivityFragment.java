package com.myproduct.freetogether.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.myapplication.yiqihi.constant.Constants;
import com.myapplication.yiqihi.event.BaseMessageEvent;
import com.myapplication.yiqihi.http.MethodHelper;
import com.myapplication.yiqihi.task.YQHBaseTask;
import com.myproduct.freetogether.R;
import com.myproduct.freetogether.adapter.GuideAdapter;
import com.myproduct.freetogether.bean.ActivitiesResponse;
import com.myproduct.freetogether.bean.Item;
import com.myproduct.freetogether.bean.Picture;

import java.util.ArrayList;
import java.util.List;

import yiqihi.mobile.com.commonlib.BaseFragment;
import yiqihi.mobile.com.commonlib.CommonUtility;
import yiqihi.mobile.com.commonlib.customview.MyListView;

public class ActivityFragment extends BaseFragment implements MyListView.OnListViewRefreshListener {
    private static final String TAG = "ActivityFragment";
    private MyListView myListView;
    public static final String CATALOG = "catalog";
    private int mCatalog;
    private GuideAdapter mGuideAdapter;
    private String mCountry = "";
    private String mLocation = "";
    private ActivitiesResponse mResponse;

    private int currentPage = 1;
    private boolean isLoadMore = false;

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
        myListView.setOnListViewRefreshListener(this);
        myListView.setIsCanRefresh(false);
        myListView.setAdapter(mGuideAdapter);
        initData();

    }

    public void initData() {
        new YQHBaseTask(getActivity(), true, YQHBaseTask.ACTION_READACTIVITIES, MethodHelper.readActivities(currentPage, mCatalog, mCountry, mLocation).toString()).exec(false);
    }

    private void bindData() {
        if (isLoadMore)
            mGuideAdapter.appendToList(mResponse.items);
        else
            mGuideAdapter.refresh(mResponse.items);

    }

    @Override
    public int getResource() {
        return R.layout.fragment_guide;
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
//     initData();
    }

    public void onEventMainThread(BaseMessageEvent event) {
        if (YQHBaseTask.ACTION_READACTIVITIES.equals(event.mAction)) {
            Log.e(TAG, event.message.toString());
            myListView.onRefreshComplete();
            String data = event.message.toString();
            System.out.println("哇咔咔:" + data);
            if (TextUtils.isEmpty(data)) {
                CommonUtility.showToast(getActivity(), "获取数据失败!");
            } else {
                mResponse = JSON.parseObject(data, ActivitiesResponse.class);
                if (!mResponse.xeach) {
                    CommonUtility.showToast(getActivity(), "获取数据失败!");
                } else {
                    bindData();
                }
            }
        }
    }
}
