package com.myapplication.yiqihi;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ScrollView;

import com.alibaba.fastjson.JSON;
import com.myapplication.yiqihi.adapter.FloorExpandableListViewAdapter;
import com.myapplication.yiqihi.adapter.SearchpageAdapter;
import com.myapplication.yiqihi.bean.Floor;
import com.myapplication.yiqihi.bean.Guide;
import com.myapplication.yiqihi.bean.ReadSearchHotResponse;
import com.myapplication.yiqihi.bean.Slider;
import com.myapplication.yiqihi.constant.Constants;
import com.myapplication.yiqihi.event.MessageEvent;
import com.myapplication.yiqihi.http.MethodHelper;
import com.myapplication.yiqihi.task.YQHBaseTask;

import java.util.ArrayList;
import java.util.List;

import yiqihi.mobile.com.commonlib.BaseActivity;
import yiqihi.mobile.com.commonlib.CommonUtility;
import yiqihi.mobile.com.commonlib.customview.AutoHideXScrollView;
import yiqihi.mobile.com.commonlib.customview.DisScrollExpandableListView;
import yiqihi.mobile.com.commonlib.customview.DisScrollListView;
import yiqihi.mobile.com.commonlib.customview.ElasticScrollViewOfNewYear;
import yiqihi.mobile.com.customviewpagerindicator.CustomViewpagerIndicatorFragment;
import yiqihi.mobile.com.customviewpagerindicator.ImageInfo;
import yiqihi.mobile.com.customviewpagerindicator.ViewpagerIndicatorEvent;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private SearchpageAdapter myAdapter;
    private ReadSearchHotResponse mResponse;
    private CustomViewpagerIndicatorFragment mFragment;
    private DisScrollExpandableListView mExListView;
    private FloorExpandableListViewAdapter mFloorAdapter;
    private ArrayList<Floor> mAllFloorsGood;
    private EditText mSearch;
    private AutoHideXScrollView mRefreshScrollView;

    @Override
    public int getResourceView() {
        return R.layout.activity_refresh;
    }

    @Override
    public void initParams() {

    }

    @Override
    protected void initView() {
        mRefreshScrollView = findViewByIdHelper(R.id.sv_container);
        mRefreshScrollView.setonRefreshListener(new MyOnRefreshListener());
        View view = View.inflate(this, R.layout.activity_main, null);
        mRefreshScrollView.addChild(view, 1);
        myAdapter = new SearchpageAdapter(this);

        View headerView = View.inflate(this, R.layout.expandable_header, null);

        mExListView = (DisScrollExpandableListView) view.findViewById(R.id.elv_floor);
        mExListView.setGroupIndicator(null);
        mExListView.setDivider(null);
        mExListView.addHeaderView(headerView);
//
        mExListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true; // This way the expander cannot be collapsed
            }
        });
        mExListView.setOnGroupExpandListener(null);
        mSearch = (EditText) headerView.findViewById(R.id.et_country);
        mSearch.setFocusable(false);// 禁止让输入框获取焦点，直接执行点击事件
        mSearch.setFocusableInTouchMode(false);
        mSearch.setOnClickListener(this);

        mFragment = (CustomViewpagerIndicatorFragment) this.getSupportFragmentManager().findFragmentById(R.id.fg_viewpager);
    }

    @Override
    protected void setListener() {
        mSearch.setOnClickListener(this);
    }

    @Override
    protected void setData() {
        new YQHBaseTask(this, true, YQHBaseTask.ACTION_READSEARCHHOT, MethodHelper.getSearchRequestParams().toString()).exec(false);
    }

    public void onEventMainThread(MessageEvent event) {
        if (YQHBaseTask.ACTION_READSEARCHHOT.equals(event.mAction)) {
            Log.e(TAG, event.message.toString());
            mRefreshScrollView.onRefreshComplete();
            String data = event.message.toString();
            System.out.println("哇咔咔:" + data);
            if (TextUtils.isEmpty(data)) {
                CommonUtility.showToast(this, "获取数据失败!");
            } else {
                mResponse = JSON.parseObject(data, ReadSearchHotResponse.class);
                if (!mResponse.xeach) {
                    CommonUtility.showToast(this, "获取数据失败!");
                } else {
                    bindSlider();
                    bindFloor();
                }
            }
        }
    }

    public void onEventMainThread(ViewpagerIndicatorEvent event) {
        Slider slider = mResponse.slider.get(event.mPosition);
        Intent intent = new Intent(this, SliderWebView.class);
        intent.putExtra("url", slider.url);
        startActivity(intent);
    }

    private void bindSlider() {
        List<Slider> listSlider = mResponse.slider;
        if (listSlider != null && listSlider.size() > 0) {
            final ArrayList<ImageInfo> imageInfos = new ArrayList<ImageInfo>();
            for (Slider slider : listSlider) {
                ImageInfo imageInfo = new ImageInfo();
                imageInfo.imageUrl = Constants.ADDRESSD + slider.picture;
                imageInfos.add(imageInfo);
            }

            mFragment.setData(imageInfos);
        }
    }

    private void bindFloor() {
        mAllFloorsGood = new ArrayList<Floor>();
        List<Guide> guide = mResponse.guide;
        List<Guide> car = mResponse.car;
        List<Guide> group = mResponse.group;
        List<Guide> product = mResponse.product;
        if (guide != null && guide.size() > 0) {
            Floor floor = new Floor();
            floor.floorKey = Floor.GUIDE_KEY;
            floor.floorName = "超级导游";
            floor.goodsList = guide;
            mAllFloorsGood.add(floor);
        }
        if (car != null && car.size() > 0) {
            Floor floor = new Floor();
            floor.floorKey = Floor.CAR_KEY;
            floor.floorName = "最棒的租车";
            floor.goodsList = car;
            mAllFloorsGood.add(floor);
        }
        if (group != null && group.size() > 0) {
            Floor floor = new Floor();
            floor.floorKey = Floor.GROUP_KEY;
            floor.floorName = "自助拼团";
            floor.goodsList = group;
            mAllFloorsGood.add(floor);
        }
        if (product != null && product.size() > 0) {
            Floor floor = new Floor();
            floor.floorKey = Floor.PRODUCT_KEY;
            floor.floorName = "一日游";
            floor.goodsList = product;
            mAllFloorsGood.add(floor);
        }
        mFloorAdapter = new FloorExpandableListViewAdapter(this, mAllFloorsGood);
        mExListView.setAdapter(mFloorAdapter);
        for (int i = 0; i < mAllFloorsGood.size(); i++) {
            mExListView.expandGroup(i);
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_country:
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.setAction("search");
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    class MyOnRefreshListener implements ElasticScrollViewOfNewYear.OnRefreshListener {

        @Override
        public void onRefresh() {
            setData();
        }

    }

}
