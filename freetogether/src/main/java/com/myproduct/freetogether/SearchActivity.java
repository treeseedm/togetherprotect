package com.myproduct.freetogether;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.myapplication.yiqihi.constant.Constants;
import com.myapplication.yiqihi.event.BaseMessageEvent;
import com.myapplication.yiqihi.http.MethodHelper;
import com.myapplication.yiqihi.task.YQHBaseTask;
import com.myproduct.freetogether.adapter.LocationAdapter;
import com.myproduct.freetogether.bean.Item;
import com.myproduct.freetogether.bean.LocationResponse;

import yiqihi.mobile.com.commonlib.BaseActivity;
import yiqihi.mobile.com.commonlib.CommonUtility;

public class SearchActivity extends BaseActivity implements TextView.OnEditorActionListener, View.OnFocusChangeListener, TextWatcher, AdapterView.OnItemClickListener, View.OnClickListener {
    private EditText etSearch;
    private LocationAdapter mLocationAdapter;
    private LocationResponse locationResponse;
    private ListView List_country;
    private TextView txtRight;

    @Override
    public int getResourceView() {
        return R.layout.activity_search;
    }

    @Override
    public void initParams() {

    }

    @Override
    protected void initView() {
        etSearch = findViewByIdHelper(R.id.et_search);
        etSearch.setEnabled(true);
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(etSearch, InputMethodManager.SHOW_FORCED);
        CommonUtility.showSoft(etSearch);
        mLocationAdapter = new LocationAdapter(this, "");
        List_country = (ListView) findViewById(R.id.List_country);
        List_country.setAdapter(mLocationAdapter);
        txtRight = findViewByIdHelper(R.id.txt_right);
        txtRight.setText("筛选");
    }

    @Override
    protected void setListener() {
        etSearch.setOnEditorActionListener(this);
        etSearch.setOnFocusChangeListener(this);
        etSearch.addTextChangedListener(this);
        List_country.setOnItemClickListener(this);
        txtRight.setOnClickListener(this);
    }

    @Override
    protected void setData() {

    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }

    /**
     * Called when the focus state of a view has changed.
     *
     * @param v        The view whose state has changed.
     * @param hasFocus The new focus state of v.
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            startSearch(etSearch.getText().toString().trim());
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }


    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        startSearch(etSearch.getText().toString().trim());
    }

    private void startSearch(String location) {

        new YQHBaseTask(SearchActivity.this, false, YQHBaseTask.ACTION_FINDLOCATION, MethodHelper.findLocationParams(location).toString()).exec(false);
    }

    public void onEventMainThread(BaseMessageEvent event) {
        String data = event.message.toString();
        String action = event.mAction;
        if (TextUtils.isEmpty(data)) {
            CommonUtility.showToast(this, "获取数据失败!");
        } else {
            locationResponse = JSON.parseObject(data, LocationResponse.class);
            if (YQHBaseTask.ACTION_FINDLOCATION.equals(locationResponse.action)) {

                locationResponse = JSON.parseObject(data, LocationResponse.class);
                if (!locationResponse.xeach) {
                    CommonUtility.showToast(this, "获取数据失败!");
                } else {
                    bindLocation();
                }
            }
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void bindLocation() {
        if (locationResponse.items != null && locationResponse.items.size() > 0) {
            List_country.setVisibility(View.VISIBLE);
            mLocationAdapter.refresh(locationResponse.items);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String continent = "";
        String country = "";
        String city = "";
        Item item = mLocationAdapter.getList().get(position);
        if ("1".equals(item.type)) {//州，国家
            continent = item.continent;
            country = item.value;
        } else if ("2".equals(item.type)) {//国家，城市
            country = item.country;
            city = item.value;
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction("searchActivity");
        intent.putExtra(Constants.CONTINENT, continent);
        intent.putExtra(Constants.COUNTRY, country);
        intent.putExtra(Constants.LOCATION, city);
        startActivity(intent);
        finish();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_right:
                break;
            default:
                break;
        }
    }
}
