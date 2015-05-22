package com.myapplication.yiqihi;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.custom.view.FilterItemView;
import com.mongodb.BasicDBObject;
import com.myapplication.yiqihi.adapter.GridViewAdapter;
import com.myapplication.yiqihi.adapter.LocationAdapter;
import com.myapplication.yiqihi.adapter.SearchpageAdapter;
import com.myapplication.yiqihi.bean.Facet;
import com.myapplication.yiqihi.bean.Item;
import com.myapplication.yiqihi.bean.Product;
import com.myapplication.yiqihi.bean.SearchResponse;
import com.myapplication.yiqihi.constant.Constants;
import com.myapplication.yiqihi.event.MessageEvent;
import com.myapplication.yiqihi.http.MethodHelper;
import com.myapplication.yiqihi.task.YQHBaseTask;

import java.util.List;

import yiqihi.mobile.com.commonlib.BaseActivity;
import yiqihi.mobile.com.commonlib.CommonUtility;
import yiqihi.mobile.com.commonlib.MobileDeviceUtil;
import yiqihi.mobile.com.commonlib.customview.DisScrollListView;
import yiqihi.mobile.com.commonlib.customview.GestureListener;
import yiqihi.mobile.com.commonlib.customview.MyListView;

public class SearchListActivity extends BaseActivity implements
        MyListView.OnListViewResizeListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener,
        MyListView.OnListViewRefreshListener, AdapterView.OnItemClickListener {
    private static final String TAG = "SearchListActivity";
    public static final String CATEGORY_KEY = "category";
    private EditText etSearch;
    private TextView txtRight;

    private String mCategory;
    private MyListView lv_product;
    private DisScrollListView lv_country;
    private LocationAdapter mListLocationAdapter;
    private RadioGroup rg_cityContainer;
    private RadioButton rb_continent, rb_country, rb_city;


    private PopupWindow filterPop;
    private PopupWindow countryPop;
    private String mAction;

    private RelativeLayout rl_sort;

    private SearchpageAdapter searchAdapter;
    private BasicDBObject jsonObject;
    private SearchResponse mProductResponse;

    //排序
    private TextView tv_defaultsort, tv_priceText, tv_sortprice, tv_updatetimetext, tv_sortupdatetime;
    private Drawable up_unselect, up_select, down_unselect, down_select;

    private int distance;
    private Animation animationOut, animationIn;

    private ScrollView sv_filter;
    private int currentPage = 1;
    private int field = 1;
    private int mode = 0;

    private int headerHeight;
    private View viewHeader;
    private LinearLayout ll_cityAndSortContainer;
    private String mContinent;
    private String mCountry;
    private String mCity;
    FrameLayout.LayoutParams params;

    @Override
    public int getResourceView() {
        return R.layout.activity_list;
    }

    @Override
    public void initParams() {

        mCategory = getIntent().getStringExtra(CATEGORY_KEY);
        mAction = getIntent().getAction();
        mContinent = getIntent().getStringExtra(Constants.CONTINENT);
        mCountry = getIntent().getStringExtra(Constants.COUNTRY);
        mCity = getIntent().getStringExtra(Constants.LOCATION);
    }

    @Override
    protected void initView() {
        etSearch = findViewByIdHelper(R.id.et_search);
        findViewByIdHelper(R.id.fl_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchListActivity.this, SearchActivity.class));
            }
        });
        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchListActivity.this, SearchActivity.class));
            }
        });
        etSearch.setText(mCity == "" ? mCountry : mCity);


        lv_product = (MyListView) findViewById(R.id.lv_product);
        lv_product.setOnListViewRefreshListener(this);
        lv_product.setOnListViewResizeListener(this);
        lv_product.setIsCanRefresh(false);

        searchAdapter = new SearchpageAdapter(this);
        lv_product.setAdapter(searchAdapter);
        txtRight = findViewByIdHelper(R.id.txt_right);
        txtRight.setText("筛选");

        ll_cityAndSortContainer = findViewByIdHelper(R.id.ll_cityAndSortContainer);
        params = (FrameLayout.LayoutParams) ll_cityAndSortContainer.getLayoutParams();
        ll_cityAndSortContainer.setVisibility(View.GONE);
        rg_cityContainer = (RadioGroup) findViewById(R.id.rg_cityContainer);
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(ViewGroup.LayoutParams.MATCH_PARENT,
                View.MeasureSpec.UNSPECIFIED);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(ViewGroup.LayoutParams.WRAP_CONTENT,
                View.MeasureSpec.UNSPECIFIED);
        ll_cityAndSortContainer.measure(widthMeasureSpec, heightMeasureSpec);
        headerHeight = ll_cityAndSortContainer.getMeasuredHeight();
        Log.i("headerHeight","========================================="+headerHeight);
        View dummyHeader = new View(this);
        AbsListView.LayoutParams absParams = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, headerHeight -
                MobileDeviceUtil.dip2px(this, 30));

//        dummyHeader.setBackgroundColor(getResources().getColor(R.color.red));
        dummyHeader.setLayoutParams(absParams);

        lv_product.addHeaderView(dummyHeader);
        rg_cityContainer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (!countryPop.isShowing()) {
                    countryPop.showAsDropDown(rg_cityContainer);
                }
                switch (checkedId) {
                    case R.id.rb_continent:
                        mListLocationAdapter.setTag(Constants.CONTINENT, "");
                        mListLocationAdapter.refresh(continetList);
                        break;
                    case R.id.rb_country:
                        mListLocationAdapter.setTag(Constants.COUNTRY, "");
                        mListLocationAdapter.refresh(countryList);
                        break;
                    case R.id.rb_city:
                        mListLocationAdapter.setTag(Constants.LOCATION, rb_country.getText().toString());
                        mListLocationAdapter.refresh(cityList);
                        break;
                    default:
                        break;
                }

            }

        });
        viewHeader = ll_cityAndSortContainer;
        rb_continent = (RadioButton) findViewById(R.id.rb_continent);
        rb_continent.setText(mContinent);
        rb_country = (RadioButton) findViewById(R.id.rb_country);
        rb_country.setText(mCountry);
        rb_city = (RadioButton) findViewById(R.id.rb_city);
        rb_city.setText(mCity);
        rl_sort = (RelativeLayout) findViewById(R.id.rl_sort);
        tv_defaultsort = (TextView) findViewById(R.id.tv_defaultsort);
        tv_sortprice = (TextView) findViewById(R.id.tv_sortprice);
        tv_priceText = (TextView) findViewById(R.id.tv_priceText);
        tv_updatetimetext = (TextView) findViewById(R.id.tv_updatetimetext);
        tv_sortupdatetime = (TextView) findViewById(R.id.tv_sortupdatetime);
        up_select = getResources().getDrawable(R.drawable.sort_up_checked);
        up_unselect = getResources().getDrawable(R.drawable.sort_up_uncheck);
        down_select = getResources().getDrawable(R.drawable.sort_down_checked);
        down_unselect = getResources().getDrawable(R.drawable.sort_down_uncheck);
        findViewByIdHelper(R.id.v_line).setVisibility(View.VISIBLE);

        View view = this.getLayoutInflater().inflate(R.layout.popu_search, null);
        countryPop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
      //  countryPop.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
        countryPop.setOutsideTouchable(true);// 点击外侧能够取消
        countryPop.setFocusable(true);// 获取焦点不然列表点击无效
        lv_country = (DisScrollListView) view.findViewById(R.id.lv_country);
        mListLocationAdapter = new LocationAdapter(this, mCategory);
        lv_country.setAdapter(mListLocationAdapter);


        distance = MobileDeviceUtil.dip2px(this, 50);


        animationIn = AnimationUtils.loadAnimation(this, R.anim.header_in);
//        animationIn.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                ll_cityAndSortContainer.setLayoutParams(params);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
        animationOut = AnimationUtils.loadAnimation(this, R.anim.header_out);
//        animationOut.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                ll_cityAndSortContainer.setLayoutParams(params);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
        view = this.getLayoutInflater().inflate(R.layout.layout_filter, null);
        int offset = MobileDeviceUtil.dip2px(SearchListActivity.this, 50);
        filterPop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        filterPop.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
        filterPop.setOutsideTouchable(true);// 点击外侧能够取消
        filterPop.setFocusable(true);// 获取焦点不然列表点击无效
        filterPop.setAnimationStyle(R.style.popwin_anim_style);
        initFilterView(view);

    }


    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        Log.e(TAG, "===========onloadmore");
        sort(field, mode, true, false);
    }

    int position = 0;

    @Override
    public void resize() {
//        int dis = scrollDis();
//        if (dis > position) {//上
//            myHander.sendEmptyMessage(STOPTOP);
//        } else {//下
//            myHander.sendEmptyMessage(STOPBOTTOM);
//        }
//        position=dis;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Item item = mListLocationAdapter.getList().get(position);
        String value = item.value;
        if (rb_city.isChecked()) {
            mCity = value;
        } else if (rb_country.isChecked()) {
            mCountry = value;
            mCity = "";
        } else if (rb_continent.isChecked()) {
            mCountry = "";
            mCity = "";
            mContinent = value;
        }
        currentPage = 1;
        sort(field, mode, false, true);
    }

    private class SortOnClick implements View.OnClickListener {

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            Object tag = v.getTag();
            int mode = 0;
            int field = 1;
            switch (v.getId()) {
                case R.id.tv_defaultsort:

                    tv_sortprice.setTag(null);
                    tv_priceText.setTag(null);
                    tv_updatetimetext.setTag(null);
                    tv_sortupdatetime.setTag(null);
                    tv_priceText.setTextColor(getResources().getColor(android.R.color.black));
                    tv_updatetimetext.setTextColor(getResources().getColor(android.R.color.black));
                    tv_sortprice.setCompoundDrawablesWithIntrinsicBounds(null, up_unselect, null, down_unselect);
                    tv_sortupdatetime.setCompoundDrawablesWithIntrinsicBounds(null, up_unselect, null, down_unselect);
                    if (tag != null) {
                        int t = Integer.valueOf(tag.toString());
                        switch (t) {
                            case 1:
                                mode = 0;
                                v.setTag(0);
                                break;
                            case 0:
                                mode = 1;
                                v.setTag(1);
                                break;
                        }
                    } else {
                        mode = 0;
                        v.setTag(0);
                    }
                    field = 1;

                    break;
                case R.id.tv_sortprice:
                case R.id.tv_priceText:

                    tv_sortupdatetime.setCompoundDrawablesWithIntrinsicBounds(null, up_unselect, null, down_unselect);
                    tv_defaultsort.setTag(null);
                    tv_updatetimetext.setTag(null);
                    tv_sortupdatetime.setTag(null);
                    tv_priceText.setTextColor(getResources().getColor(R.color.green));
                    tv_updatetimetext.setTextColor(getResources().getColor(android.R.color.black));
                    if (tag != null) {
                        int t = Integer.valueOf(tag.toString());
                        switch (t) {
                            case 1:
                                mode = 0;

                                tv_priceText.setTag(0);
                                tv_sortprice.setTag(0);
                                tv_sortprice.setCompoundDrawablesWithIntrinsicBounds(null, up_select, null, down_unselect);
                                break;
                            case 0:
                                mode = 1;
                                tv_priceText.setTag(1);
                                tv_sortprice.setTag(1);
                                tv_sortprice.setCompoundDrawablesWithIntrinsicBounds(null, up_unselect, null, down_select);
                                break;
                        }
                    } else {
                        mode = 0;
                        tv_priceText.setTag(0);
                        tv_sortprice.setTag(0);
                        tv_sortprice.setCompoundDrawablesWithIntrinsicBounds(null, up_select, null, down_unselect);
                    }
                    field = 2;
                    break;
                case R.id.tv_updatetimetext:
                case R.id.tv_sortupdatetime:
                    tv_sortprice.setCompoundDrawablesWithIntrinsicBounds(null, up_unselect, null, down_unselect);
                    tv_defaultsort.setTag(null);
                    tv_sortprice.setTag(null);
                    tv_priceText.setTag(null);
                    tv_updatetimetext.setTextColor(getResources().getColor(R.color.green));
                    tv_priceText.setTextColor(getResources().getColor(android.R.color.black));
                    if (tag != null) {
                        int t = Integer.valueOf(tag.toString());
                        switch (t) {
                            case 1:
                                mode = 0;
                                tv_sortupdatetime.setCompoundDrawablesWithIntrinsicBounds(null, up_select, null,
                                        down_unselect);
                                tv_updatetimetext.setTag(0);
                                tv_sortupdatetime.setTag(0);
                                break;
                            case 0:
                                mode = 1;
                                tv_sortupdatetime.setCompoundDrawablesWithIntrinsicBounds(null, up_unselect, null,
                                        down_select);
                                tv_updatetimetext.setTag(1);
                                tv_sortupdatetime.setTag(1);
                                break;
                        }
                    } else {
                        mode = 0;
                        tv_sortupdatetime.setCompoundDrawablesWithIntrinsicBounds(null, up_select, null, down_unselect);

                        tv_updatetimetext.setTag(0);
                        tv_sortupdatetime.setTag(0);
                    }
                    field = 2;
                    break;
                default:
                    break;
            }
            sort(field, mode, false, false);
        }
    }

    @Override
    protected void setListener() {
        txtRight.setOnClickListener(this);
        tv_defaultsort.setOnClickListener(new SortOnClick());
        tv_sortprice.setOnClickListener(new SortOnClick());
        tv_priceText.setOnClickListener(new SortOnClick());
        tv_updatetimetext.setOnClickListener(new SortOnClick());
        tv_sortupdatetime.setOnClickListener(new SortOnClick());
        lv_product.setLongClickable(true);
        lv_product.setOnTouchListener(new ListGestureListener(this));
        lv_country.setOnItemClickListener(this);
    }

    private class MyGestureListener extends GestureListener {

        public MyGestureListener(Context context) {
            super(context);
        }

        @Override
        public boolean left() {
            myHander.sendEmptyMessage(1);
            return super.left();
        }

        @Override
        public boolean right() {
            myHander.sendEmptyMessage(0);
            return super.right();
        }
    }

    private float mDistanceY;

    private class ListGestureListener extends GestureListener {

        public ListGestureListener(Context context) {
            super(context);
        }

        @Override
        public boolean left() {
            myHander.sendEmptyMessage(1);
            return super.left();
        }

        @Override
        public boolean right() {
            myHander.sendEmptyMessage(0);
            return super.right();
        }

        @Override
        public boolean top(float distanceY) {
            mDistanceY = distanceY;
            myHander.sendEmptyMessage(TOP);
            return super.top(distanceY);
        }

        @Override
        public boolean bottom(float distanceY) {
            mDistanceY = distanceY;
            myHander.sendEmptyMessage(BOTTOM);
            return super.bottom(distanceY);
        }

        @Override
        public boolean topStop() {

            myHander.sendEmptyMessage(STOPTOP);
            return super.topStop();
        }
        @Override
        public boolean bottomStop(){
            myHander.sendEmptyMessage(STOPBOTTOM);
            return super.bottomStop();
        }
    }

    @Override
    protected void setData() {
        if ("showFilter".equals(mAction)) {
            myHander.sendEmptyMessageDelayed(3, 1000);
            return;
        }
        sort(field, mode, false, false);
    }


    private void setVisiable(int visiable) {
        ll_cityAndSortContainer.setVisibility(visiable);
        lv_product.setVisibility(visiable);
    }

    public void onEventMainThread(MessageEvent event) {
        String data = event.message.toString();
        String action = event.mAction;
        if (YQHBaseTask.ACTION_UPDATEPANEL.equals(action)) {
            updatePanel(data.toString());
            return;
        } else if (YQHBaseTask.ACTION_UPDATEFILTER.equals(action)) {
            if ("continent".equals(data)) {
                fvDazhou.setGridVisiable();
                countryAdapter.reset();
                locationAdapter.reset();
                rb_country.setText("全部");
                rb_city.setText("全部");
                currentPage = 1;
                sort(field, mode, false, false);
                fvDazhou.setSaveOrUpdateVisiable(View.VISIBLE, dazhouAdapter.getSelectValue());
                fvCountry.setSaveOrUpdateVisiable(View.GONE, "");
                fvLocation.setSaveOrUpdateVisiable(View.GONE, "");
            } else if ("country".equals(data)) {
                fvCountry.setGridVisiable();
                currentPage = 1;
                locationAdapter.reset();
                sort(field, mode, false, false);

                rb_city.setText("全部");
                //显示国家
                fvCountry.setSaveOrUpdateVisiable(View.VISIBLE, countryAdapter.getSelectValue());
                fvLocation.setSaveOrUpdateVisiable(View.GONE, "");
                fvCategory.setSaveOrUpdateVisiable(View.GONE, "");
            } else if ("location".equals(data)) {
                fvLocation.setGridVisiable();
                fvLocation.setSaveOrUpdateVisiable(View.VISIBLE, locationAdapter.getSelectValue());
                fvCategory.setSaveOrUpdateVisiable(View.GONE, "");
            } else if ("category".equals(data)) {
                fvCategory.setGridVisiable();
                fvCategory.setSaveOrUpdateVisiable(View.VISIBLE, categoryAdapter.getSelectValue());
            }

            return;
        } else if (YQHBaseTask.ACTION_CHANGETEXT.equals(action)) {
            etSearch.setText(data);
            return;
        }
        rg_cityContainer.clearCheck();
        countryPop.dismiss();

        Log.e(TAG, event.message.toString());


        if (TextUtils.isEmpty(data)) {
            CommonUtility.showToast(this, "获取数据失败!");
        } else {
            mProductResponse = JSON.parseObject(data, SearchResponse.class);
            if (YQHBaseTask.ACTION_SEARCHTRIP.equals(mProductResponse.action)) {
                setEtSearch(true);
                setVisiable(View.VISIBLE);

                List<Facet> listFacets = mProductResponse.facets;
                if (listFacets == null) return;
                for (Facet facet : listFacets) {
                    String name = facet.name;
                    if ("continent".equals(name)) {
                        continetList = facet.items;

                    } else if ("country".equals(name)) {
                        countryList = facet.items;

                    } else if ("category".equals(name)) {
                        categoryList = facet.items;

                    } else if ("location".equals(name)) {
                        cityList = facet.items;

                    }
                }
                bindProduct();
            }
        }

    }

    private void setEtSearch(boolean visiable) {
        if (visiable && etSearch.getText().toString().length() > 0) {
            etSearch.setBackgroundColor(getResources().getColor(R.color.white));
            etSearch.setTextColor(getResources().getColor(R.color.green));
            etSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_close, 0);
        } else {
            etSearch.setBackgroundColor(getResources().getColor(R.color.transparent));
            etSearch.setTextColor(getResources().getColor(R.color.white));
            etSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        }
    }

    private void updatePanel(String label) {
        int num = 0;
        if ("language".equals(label)) {
            String[] ids = languageAdapter.getSelectId().split("-");
            num = ids.length;
            if (num > 0) {
                fvLanguage.setSelectNumVisiable(View.VISIBLE, "" + num);
                if (fvLanguage.gv_data.isShown()) {
                    fvLanguage.setSaveOrUpdateVisiable(View.VISIBLE, "清除全部");
                } else {
                    fvLanguage.setSaveOrUpdateVisiable(View.VISIBLE, "确认更改");
                }
            } else
                fvLanguage.setSelectNumVisiable(View.GONE, "" + num);
        } else if ("service".equals(label)) {
            String[] ids = serviceAdapter.getSelectId().split("-");
            num = ids.length;
            if (num > 0) {
                fvService.setSelectNumVisiable(View.VISIBLE, "" + num);
                if (fvService.gv_data.isShown()) {
                    fvService.setSaveOrUpdateVisiable(View.VISIBLE, "清除全部");
                } else {
                    fvService.setSaveOrUpdateVisiable(View.VISIBLE, "确认更改");
                }
            } else
                fvService.setSelectNumVisiable(View.GONE, "" + num);
        } else if ("price".equals(label)) {
            String[] ids = priceAdapter.getSelectId().split("-");
            num = ids.length;
            if (num > 0) {
                fvPrice.setSelectNumVisiable(View.VISIBLE, "" + num);
                if (fvPrice.gv_data.isShown()) {
                    fvPrice.setSaveOrUpdateVisiable(View.VISIBLE, "清除全部");
                } else {
                    fvPrice.setSaveOrUpdateVisiable(View.VISIBLE, "确认更改");
                }
            } else
                fvPrice.setSelectNumVisiable(View.GONE, "" + num);
        }

    }

    private void bindProduct() {
        rl_sort.setVisibility(View.VISIBLE);
        rg_cityContainer.setVisibility(View.VISIBLE);
        updateFilter();
        bindCity();
        bindItems();

    }

    private void bindCity() {
        List<Item> requires = mProductResponse.requires;
        boolean hasCountry = false;
        boolean hasCity = false;
        boolean hasContinent = false;
        for (Item item : requires) {
            if (Constants.CONTINENT.equals(item.name)) {
                rb_continent.setText(item.value);
                rb_continent.setVisibility(View.VISIBLE);
                hasContinent = true;
            } else if (Constants.COUNTRY.equals(item.name)) {
                hasCountry = true;
                rb_country.setText(item.value);
                rb_country.setVisibility(View.VISIBLE);
            } else if (Constants.LOCATION.equals(item.name)) {
                hasCity = true;
                rb_city.setVisibility(View.VISIBLE);
                rb_city.setText(item.value);
            }
        }
        if (!hasContinent) {
            rb_continent.setVisibility(View.VISIBLE);
            rb_continent.setText("全部");
        }
        if (!hasCountry) {
            rb_country.setVisibility(View.VISIBLE);
            rb_country.setText("全部");
        }
        if (!hasCity) {
            rb_city.setVisibility(View.VISIBLE);
            rb_city.setText("全部");
        }
//        rb_continent.setText();
    }

    private void bindItems() {

        List<Product> items = mProductResponse.items;
        if (items.size() == 10) {
            currentPage++;
            lv_product.setHasMore(true);
        } else {
            lv_product.setHasMore(false);
        }
        if (mIsLoadMore) {
            searchAdapter.appendToList(items);
        } else
            searchAdapter.refresh(items);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_right:
                if (mProductResponse != null)
                    showFilter();
                break;
            case R.id.fv_dazhou:
                fvDazhou.setGridVisiable();
                break;
            case R.id.fv_category:
                fvCategory.setGridVisiable();
                break;
            case R.id.fv_country:
                fvCountry.setGridVisiable();
                break;
            case R.id.fv_location:
                fvLocation.setGridVisiable();
                break;
            case R.id.fv_language:
                fvLanguage.setGridVisiable();
                break;
            case R.id.fv_service:
                fvService.setGridVisiable();
                break;
            case R.id.fv_price:
                fvPrice.setGridVisiable();
                break;
            case R.id.btn_ok:
                ok();
                break;
            case R.id.btn_reset:
                reset();
                break;
            default:
                break;
        }
    }

    private float lastX;
    private float distances = 100;
    private static final int TOP = 4;
    private static final int BOTTOM = 5;
    private static final int STOPTOP = 6;
    private static final int STOPBOTTOM = 7;
    private Handler myHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (!filterPop.isShowing()) {
                    filterPop.showAtLocation(findViewByIdHelper(R.id.ll_parent), Gravity.RIGHT, 0, 0);
                }
            } else if (msg.what == 3) {
                showFilter();
            } else if (msg.what == TOP) {

                int topMargin = params.topMargin - (int) mDistanceY;
                if(topMargin>=0){

                }
                Log.v("top", "TOP==" + (int) topMargin);
                if (Math.abs(topMargin) >= headerHeight) {
                    topMargin = -headerHeight;
                }
                params.setMargins(0, topMargin, 0, 0);
                ll_cityAndSortContainer.setLayoutParams(params);

            } else if (msg.what == BOTTOM) {
                int topMargin = params.topMargin - (int) mDistanceY;
                Log.v("bottom", "BOTTOM==" + (int) topMargin);
                if (topMargin >= 0)
                    topMargin = 0;
                params.setMargins(0, topMargin, 0, 0);
                ll_cityAndSortContainer.setLayoutParams(params);

            } else if (msg.what == STOPTOP) {//向上滑动停止
                int topMargin = params.topMargin;
                Log.v("STOPTOP", "" + (int) topMargin);
                params.setMargins(0, -headerHeight, 0, 0);
                ll_cityAndSortContainer.startAnimation(animationOut);
                ll_cityAndSortContainer.setLayoutParams(params);
            } else if (msg.what == STOPBOTTOM) {//向下滑动停止显示
                int topMargin = params.topMargin;
                Log.v("STOPDOTTOM", "" + (int) topMargin);
                params.setMargins(0, 0, 0, 0);
                ll_cityAndSortContainer.startAnimation(animationIn);
                ll_cityAndSortContainer.setLayoutParams(params);
            } else {
                if (filterPop.isShowing()) {
                    filterPop.dismiss();
                }
            }
        }
    };

    private void showFilter() {
        updateFilter();
        filterPop.showAtLocation(findViewByIdHelper(R.id.ll_parent), Gravity.RIGHT, 0, 0);
    }

    private void updateFilter() {
        if (mProductResponse != null && filterPop != null) {
            List<Facet> listFacets = mProductResponse.facets;
            if (listFacets == null) return;
            for (Facet facet : listFacets) {
                String name = facet.name;
                if ("continent".equals(name)) {
                    dazhouAdapter.setLabel(name);
                    fvDazhou.setLeftIcon(facet.icon);
                } else if ("country".equals(name)) {
                    countryAdapter.setLabel(name);
                    fvCountry.setLeftIcon(facet.icon);
                } else if ("category".equals(name)) {
                    categoryAdapter.setLabel(name);
                    fvCategory.setLeftIcon(facet.icon);
                } else if ("location".equals(name)) {
                    locationAdapter.setLabel(name);
                    fvLocation.setLeftIcon(facet.icon);
                }
            }
            listFacets = mProductResponse.filters;
            for (Facet facet : listFacets) {
                String name = facet.name;
                if ("language".equals(name)) {
                    fvLanguage.setLeftIcon(facet.icon);

                } else if ("service".equals(name)) {
                    fvService.setLeftIcon(facet.icon);
                } else if ("price".equals(name)) {
                    fvPrice.setLeftIcon(facet.icon);
                }
            }
            setFilterData();
            setMulitFilter();
        }

    }

    private FilterItemView fvDazhou, fvCategory, fvLocation, fvCountry;
    private GridViewAdapter dazhouAdapter, categoryAdapter, locationAdapter, countryAdapter;

    private FilterItemView fvLanguage, fvService, fvPrice;
    private GridViewAdapter languageAdapter, serviceAdapter, priceAdapter;

    private List<Item> continetList;
    private List<Item> countryList;
    private List<Item> cityList;
    private List<Item> categoryList;
    private TextView tv_totalSize;
    private Button btn_reset, btn_ok;
    private LinearLayout ll_filtercontainer;

    private void initFilterView(View view) {
        sv_filter = (ScrollView) view.findViewById(R.id.sv_filter);
        sv_filter.setLongClickable(true);
        sv_filter.setOnTouchListener(new MyGestureListener(this));
        tv_totalSize = (TextView) view.findViewById(R.id.tv_totalSize);
        btn_reset = (Button) view.findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(this);
        btn_ok = (Button) view.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);
        fvDazhou = (FilterItemView) view.findViewById(R.id.fv_dazhou);
        fvDazhou.setOnClickListener(this);

        fvCategory = (FilterItemView) view.findViewById(R.id.fv_category);
        fvCategory.setOnClickListener(this);


        fvLocation = (FilterItemView) view.findViewById(R.id.fv_location);
        fvLocation.setOnClickListener(this);

        fvCountry = (FilterItemView) view.findViewById(R.id.fv_country);
        fvCountry.setOnClickListener(this);

        dazhouAdapter = new GridViewAdapter(this, false);
        fvDazhou.gv_data.setAdapter(dazhouAdapter);

        categoryAdapter = new GridViewAdapter(this, false);
        fvCategory.gv_data.setAdapter(categoryAdapter);

        countryAdapter = new GridViewAdapter(this, false);
        fvCountry.gv_data.setAdapter(countryAdapter);

        locationAdapter = new GridViewAdapter(this, false);
        fvLocation.gv_data.setAdapter(locationAdapter);


        fvLanguage = (FilterItemView) view.findViewById(R.id.fv_language);
        fvLanguage.setOnClickListener(this);
        languageAdapter = new GridViewAdapter(this, true);
        fvLanguage.gv_data.setAdapter(languageAdapter);
        fvLanguage.tv_saveorupdate.setOnClickListener(new MyOnClickListener(fvLanguage));

        fvService = (FilterItemView) view.findViewById(R.id.fv_service);
        fvService.setOnClickListener(this);
        serviceAdapter = new GridViewAdapter(this, true);
        fvService.gv_data.setAdapter(serviceAdapter);
        fvService.tv_saveorupdate.setOnClickListener(new MyOnClickListener(fvService));

        fvPrice = (FilterItemView) view.findViewById(R.id.fv_price);
        fvPrice.setOnClickListener(this);
        priceAdapter = new GridViewAdapter(this, true);
        fvPrice.gv_data.setAdapter(priceAdapter);
        fvPrice.tv_saveorupdate.setOnClickListener(new MyOnClickListener(fvPrice));

    }

    private void setFilterData() {
        dazhouAdapter.refresh(continetList);
        countryAdapter.refresh(countryList);
        locationAdapter.refresh(cityList);
        categoryAdapter.refresh(categoryList);

        fvDazhou.setSaveOrUpdateVisiable(View.VISIBLE, dazhouAdapter.getSelectValue());
        fvCountry.setSaveOrUpdateVisiable(View.VISIBLE, countryAdapter.getSelectValue());
        fvLocation.setSaveOrUpdateVisiable(View.VISIBLE, locationAdapter.getSelectValue());
    }

    private void reset() {
        dazhouAdapter.reset();
        countryAdapter.reset();
        locationAdapter.reset();
        categoryAdapter.reset();

        languageAdapter.reset();
        serviceAdapter.reset();
        priceAdapter.reset();
    }

    private void ok() {
        filterPop.dismiss();
        currentPage = 1;
        sort(field, mode, false, false);
    }

    private boolean mIsLoadMore = false;

    private void sort(int field, int mode, boolean isloadmore, boolean updateLocation) {
        this.mIsLoadMore = isloadmore;
        this.field = field;
        this.mode = mode;
        String continent = "";
        String countryName = "";
        String cityName = "";
        String category = "";
        String language = "";
        String serviceTag = "";
        String priceTag = "";
        if (!updateLocation) {
            continent = dazhouAdapter.getSelectValue();
            if (TextUtils.isEmpty(continent))
                continent = rb_continent.getText().toString();

            countryName = countryAdapter.getSelectValue();
            if (TextUtils.isEmpty(countryName))
                countryName = rb_country.getText().toString();
            cityName = locationAdapter.getSelectValue();
            if (TextUtils.isEmpty(cityName))
                cityName = rb_city.getText().toString();
            category = categoryAdapter.getSelectId();
            if (TextUtils.isEmpty(category))
                category = mCategory;
        } else {
            continent = mContinent;
            countryName = mCountry;
            cityName = mCity;
            category = mCategory;
        }

        language = languageAdapter.getSelectId();
        serviceTag = serviceAdapter.getSelectId();
        priceTag = priceAdapter.getSelectId();
        new YQHBaseTask(SearchListActivity.this, true, YQHBaseTask.ACTION_SEARCHTRIP,
                MethodHelper.searchTrip(continent, countryName, cityName,
                        category, language, field, mode, serviceTag, priceTag, currentPage).toString()).exec(false);
    }

    private void setMulitFilter() {
        tv_totalSize.setText("共" + mProductResponse.totalCount + "条");
        List<Facet> listFacets = mProductResponse.filters;
        for (Facet facet : listFacets) {
            String name = facet.name;
            if ("language".equals(name)) {
                languageAdapter.setLabel(name);
                languageAdapter.refresh(facet.items);

            } else if ("service".equals(name)) {
                serviceAdapter.setLabel(name);
                serviceAdapter.refresh(facet.items);

            } else if ("price".equals(name)) {
                priceAdapter.setLabel(name);
                priceAdapter.refresh(facet.items);

            }
        }
    }

    private void hideFiler() {

    }

    /**
     * Called when the checked state of a compound button has changed.
     *
     * @param buttonView The compound button view whose state has changed.
     * @param isChecked  The new checked state of buttonView.
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


    }

    public class MyOnClickListener implements View.OnClickListener {
        private FilterItemView itemView;

        public MyOnClickListener(FilterItemView itemView) {
            this.itemView = itemView;
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            TextView tv = (TextView) v;
            GridViewAdapter adapter = (GridViewAdapter) itemView.gv_data.getAdapter();
            if (tv.getText().toString().equals("清除全部")) {
                adapter.reset();
                itemView.setSaveOrUpdateVisiable(View.GONE, "");
                itemView.setSelectNumVisiable(View.GONE, "");
            } else if (tv.getText().toString().equals("确认更改")) {
                itemView.gv_data.setVisibility(View.GONE);
            }
        }
    }

    public int scrollDis() {
        View c = lv_product.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = lv_product.getFirstVisiblePosition();
        int top = c.getTop();
        return -top + (firstVisiblePosition - 1) * c.getHeight();
    }

}
