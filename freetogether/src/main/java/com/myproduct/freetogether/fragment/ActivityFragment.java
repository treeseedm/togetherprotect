package com.myproduct.freetogether.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.myapplication.yiqihi.constant.Constants;
import com.myapplication.yiqihi.event.BaseMessageEvent;
import com.myapplication.yiqihi.http.ClientUtil;
import com.myapplication.yiqihi.http.MethodHelper;
import com.myapplication.yiqihi.task.YQHBaseTask;
import com.myapplication.yiqihi.task.YQHBaseTaskByListeners;
import com.myproduct.freetogether.R;
import com.myproduct.freetogether.adapter.CarAdapter;
import com.myproduct.freetogether.adapter.DaigouAdapter;
import com.myproduct.freetogether.adapter.GuideAdapter;
import com.myproduct.freetogether.bean.ActivitiesResponse;
import com.myproduct.freetogether.bean.Item;
import com.myproduct.freetogether.bean.Picture;

import org.x.net.Msg;
import org.x.net.MsgEvent;

import java.util.ArrayList;
import java.util.List;

import yiqihi.mobile.com.commonlib.BaseFragment;
import yiqihi.mobile.com.commonlib.CommonUtility;
import yiqihi.mobile.com.commonlib.customview.MyListView;

public class ActivityFragment extends BaseFragment implements MyListView.OnListViewRefreshListener, MsgEvent {
    private static final String TAG = "ActivityFragment";
    private MyListView myListView;
    public static final String CATALOG = "catalog";
    private int mCatalog;
    private GuideAdapter mGuideAdapter;
    private CarAdapter mCarAdapter;
    private DaigouAdapter mDaigouAdapter;
    private String mCountry = "";
    private String mLocation = "";
    private ActivitiesResponse mResponse;

    private int currentPage = 1;
    protected boolean mIsLoadMore;

    protected boolean mIsVisiable;
    private boolean mHaveLoaded = false;
    protected boolean mPrepare;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCatalog = getArguments().getInt(CATALOG);
        mCountry = getArguments().getString(Constants.COUNTRY);
        mLocation = getArguments().getString(Constants.LOCATION);
    }

    @Override
    public void initView() {
        mPrepare = true;
        myListView = findViewByIdHelper(R.id.mlv_listview);
        myListView.setOnListViewRefreshListener(this);
        myListView.setIsCanRefresh(false);
//        myListView.setAdapter(mGuideAdapter);
        lazyLoad();
    }

    public void initData(boolean isLoadMore) {
        this.mIsLoadMore = isLoadMore;
        new YQHBaseTaskByListeners(getActivity(), true, YQHBaseTask.ACTION_READACTIVITIES, MethodHelper.readActivities(currentPage, mCatalog, mCountry, mLocation).toString(), this).exec(false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisiable = isVisibleToUser;
        lazyLoad();
    }

    public void lazyLoad() {
        if (!mIsVisiable || !mPrepare || mHaveLoaded) {
            return;
        }
        mHaveLoaded = true;
        initData(false);
    }

    private void bindData() {
        if (currentPage < mResponse.pageCount) {
            currentPage++;
            myListView.setHasMore(true);
        } else {
            myListView.setHasMore(false);
        }
        switch (mCatalog) {
            case 7:
                if (mGuideAdapter == null) {
                    mGuideAdapter = new GuideAdapter(getActivity());
                    myListView.setAdapter(mGuideAdapter);
                }
                if (mIsLoadMore)
                    mGuideAdapter.appendToList(mResponse.items);
                else
                    mGuideAdapter.refresh(mResponse.items);
                break;
            case 11:
                if (mDaigouAdapter == null) {
                    mDaigouAdapter = new DaigouAdapter(getActivity());
                    myListView.setAdapter(mDaigouAdapter);
                }
                if (mIsLoadMore)
                    mDaigouAdapter.appendToList(mResponse.items);
                else
                    mDaigouAdapter.refresh(mResponse.items);
                break;
            default:
                if (mCarAdapter == null) {
                    mCarAdapter = new CarAdapter(getActivity());
                    myListView.setAdapter(mCarAdapter);
                }
                if (mIsLoadMore)
                    mCarAdapter.appendToList(mResponse.items);
                else
                    mCarAdapter.refresh(mResponse.items);
                break;
        }


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
        initData(true);
    }

    @Override
    public void onResponse(Msg.DataType type, String action, String key, Object data) {
        myListView.onRefreshComplete();
        String result = data.toString();
        Log.e(TAG, "response:" + result);
        if (TextUtils.isEmpty(result)) {
            CommonUtility.showToast(getActivity(), "获取数据失败!");
        } else {
            if(YQHBaseTask.ACTION_READACTIVITIES.equals(action)){
                mResponse = JSON.parseObject(result, ActivitiesResponse.class);
                if(mResponse==null){
                    return;
                }
                if (!mResponse.xeach) {
                    CommonUtility.showToast(getActivity(), "获取数据失败!");
                } else {
                    bindData();
                }
            }

        }
    }
}
