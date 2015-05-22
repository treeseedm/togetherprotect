package com.myproduct.freetogether.fragment;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.myproduct.freetogether.R;
import com.myproduct.freetogether.adapter.GuideAdapter;
import com.myproduct.freetogether.bean.Guide;

import java.util.ArrayList;
import java.util.List;

import yiqihi.mobile.com.commonlib.BaseFragment;
import yiqihi.mobile.com.commonlib.customview.MyListView;

public class GuideFragment extends BaseFragment {
    private MyListView myListView;
    public static final String TAG = "tag";
    private String tag;
    private GuideAdapter mGuideAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tag = getArguments().getString(TAG);
    }

    @Override
    public void initView() {
        mGuideAdapter = new GuideAdapter(getActivity());
        myListView = findViewByIdHelper(R.id.mlv_listview);
        myListView.setAdapter(mGuideAdapter);
        List<Guide> listGuide = new ArrayList<Guide>();
        for (int i = 0; i < 30; i++) {
            Guide guide = new Guide();
            listGuide.add(guide);
        }
        initData();

    }
    public void initData(){

    }

    @Override
    public int getResource() {
        return R.layout.fragment_guide;
    }
}
