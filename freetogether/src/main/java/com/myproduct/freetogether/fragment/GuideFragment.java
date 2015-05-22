package com.myproduct.freetogether.fragment;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.myproduct.freetogether.R;

import java.util.ArrayList;
import java.util.List;

import yiqihi.mobile.com.commonlib.BaseFragment;
import yiqihi.mobile.com.commonlib.customview.MyListView;

public class GuideFragment extends BaseFragment {
    private MyListView myListView;
    public static final String TAG="tag";
    private String tag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tag=getArguments().getString(TAG);
    }

    @Override
    public void initView() {
        myListView=findViewByIdHelper(R.id.mlv_listview);
        List<String> testString=new ArrayList<String>();
        for(int i=0;i<30;i++){
             testString.add("tag=="+i);
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,testString);
        myListView.setAdapter(adapter);
    }

    @Override
    public int getResource() {
        return R.layout.fragment_guide;
    }
}
