package com.custom.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.myapplication.yiqihi.R;
import com.myapplication.yiqihi.adapter.GridViewAdapter;
import com.myapplication.yiqihi.constant.Constants;

import yiqihi.mobile.com.commonlib.customview.DisScrollGridView;

public class FilterItemView extends LinearLayout {
    private Animation animationDown ;
    private Animation animationUp;
    private Context mContext;
    private LinearLayout ll_container;
    public DisScrollGridView gv_data;
    private SimpleDraweeView leftView;
    private TextView tvTitle;
    private ImageView rightView;
    private TextView tv_selectNum;
    public TextView tv_saveorupdate;
    public FilterItemView(Context context) {
        super(context);
        this.mContext=context;
        init();
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public FilterItemView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        this.mContext=context;

        init();
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.ToolBar);
        String title=  a.getString(R.styleable.ToolBar_filtertitle);
        int resid=a.getResourceId(R.styleable.ToolBar_leftimageRes,R.drawable.icon_dazhou);
        leftView.setImageResource(resid);
        tvTitle.setText(title==null?"":title);
        a.recycle();

    }
    private void init(){
        LinearLayout.LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        setLayoutParams(params);
        View view=View.inflate(mContext, R.layout.filter_item,null);
        leftView= (SimpleDraweeView) view.findViewById(R.id.left_icon);
        tvTitle= (TextView) view.findViewById(R.id.tv_title);
        rightView= (ImageView) view.findViewById(R.id.iv_rightView);
        ll_container= (LinearLayout) view.findViewById(R.id.ll_container);
        gv_data= (DisScrollGridView) view.findViewById(R.id.gv_data);
        tv_selectNum= (TextView) view.findViewById(R.id.tv_selectNum);
        tv_saveorupdate= (TextView) view.findViewById(R.id.tv_saveorupdate);
        animationUp= AnimationUtils.loadAnimation(mContext, R.anim.view_rotate_up);
        animationDown= AnimationUtils.loadAnimation(mContext, R.anim.view_rotate_down);
        addView(view);
    }
    public void setGridVisiable(){
        if(ll_container.isShown()){

            rightView.startAnimation(animationUp);
            ll_container.setVisibility(View.GONE);
        }else {
            rightView.startAnimation(animationDown);
            ll_container.setVisibility(View.VISIBLE);
        }
    }
   public void setSelectNumVisiable(int visiable,String text){
       tv_selectNum.setVisibility(visiable);
       tv_selectNum.setText(text);
   }
    public void setSaveOrUpdateVisiable(int visiable,String text){
        tv_saveorupdate.setVisibility(visiable);
        tv_saveorupdate.setText(text);
    }
    public void setLeftIcon(String url){
        leftView.setImageURI(Uri.parse(Constants.ADDRESSD+url));
    }
}
