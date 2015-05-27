package com.myproduct.freetogether.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.myapplication.yiqihi.constant.Constants;
import com.myproduct.freetogether.R;
import com.myproduct.freetogether.fragment.ActivityFragment;

import yiqihi.mobile.com.commonlib.customview.PagerSlidingTabStrip;

public class ViewPagerAdapter extends FragmentStatePagerAdapter implements PagerSlidingTabStrip.ViewTabProvider {
    private String[] mTags=new String[]
            {"拼导游", "拼租车", "求玩家", "陪你玩", "免费代购"};
    private Context mContext;
    private int[] mCatalog=new int[]{7,8,9,10,11};
    private GuideAdapter mGuideAdapter;
    private String mCountry = "";
    private String mLocation = "";
    public ViewPagerAdapter(FragmentManager fm, Context context,String country,String location) {
        super(fm);
        this.mContext = context;
        this.mCountry=country;
        this.mLocation=location;
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        ActivityFragment fragment = new ActivityFragment();
        Bundle params = new Bundle();
        params.putInt(ActivityFragment.CATALOG, mCatalog[position]);
        params.putString(Constants.LOCATION,mLocation);
        params.putString(Constants.COUNTRY,mCountry);
        fragment.setArguments(params);
        return fragment;
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return mTags.length;
    }

    @Override
    public View getPageView(int position) {
        View view = View.inflate(mContext, R.layout.tab, null);
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.sdv_image);
        TextView mTitle = (TextView) view.findViewById(R.id.tv_tab);
        View mIndicatoreColor = view.findViewById(R.id.indicator_color);
        int resid=0;
        if(position==0){
            resid=R.drawable.icon_guide_selected;
            mIndicatoreColor.setBackgroundColor(mContext.getResources().getColor(R.color.guide_color));
            mTitle.setTextColor(mContext.getResources().getColor(R.color.guide_color));
        }else{
            resid=getDefaultResource(position);
        }
        simpleDraweeView.setImageResource(resid);
        mTitle.setText(mTags[position]);
        return view;
    }

    @Override
    public void updateViewState(View view, int position) {
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.sdv_image);
        TextView mTitle = (TextView) view.findViewById(R.id.tv_tab);
        View mIndicatoreColor = view.findViewById(R.id.indicator_color);
        Log.v("position===","=="+position);
        switch (position) {
            case 0:
                mTitle.setTextColor(mContext.getResources().getColor(R.color.guide_color));
                mIndicatoreColor.setBackgroundColor(mContext.getResources().getColor(R.color.guide_color));
                mIndicatoreColor.setVisibility(View.VISIBLE);
            break;
            case 1:
                mTitle.setTextColor(mContext.getResources().getColor(R.color.car_color));
                mIndicatoreColor.setBackgroundColor(mContext.getResources().getColor(R.color.car_color));
                mIndicatoreColor.setVisibility(View.VISIBLE);
                break;
            case 2:
                mTitle.setTextColor(mContext.getResources().getColor(R.color.play_color));
                mIndicatoreColor.setBackgroundColor(mContext.getResources().getColor(R.color.play_color));
                mIndicatoreColor.setVisibility(View.VISIBLE);
                break;
            case 3:
                mTitle.setTextColor(mContext.getResources().getColor(R.color.playwith_color));
                mIndicatoreColor.setBackgroundColor(mContext.getResources().getColor(R.color.playwith_color));
                mIndicatoreColor.setVisibility(View.VISIBLE);
                break;
            case 4:
                mTitle.setTextColor(mContext.getResources().getColor(R.color.daigou_color));
                mIndicatoreColor.setBackgroundColor(mContext.getResources().getColor(R.color.daigou_color));
                mIndicatoreColor.setVisibility(View.VISIBLE);
                break;
            default:
                mTitle.setTextColor(mContext.getResources().getColor(R.color.dark));
                mIndicatoreColor.setVisibility(View.GONE);
                break;
        }
        simpleDraweeView.setImageResource(getSelectResource(position));
    }

    @Override
    public void setDefaultView(View view, int position) {
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.sdv_image);
        TextView mTitle = (TextView) view.findViewById(R.id.tv_tab);
        View mIndicatoreColor = view.findViewById(R.id.indicator_color);
        Log.v("position===","=="+position);
        mTitle.setTextColor(mContext.getResources().getColor(R.color.dark));
        mIndicatoreColor.setVisibility(View.GONE);
        simpleDraweeView.setImageResource(getDefaultResource(position));
    }
    private int getSelectResource(int position){
        int resid=0;
        switch (position) {
            case 0:
                resid=R.drawable.icon_guide_selected;
                break;
            case 1:
                resid=R.drawable.icon_car_selected;
                break;
            case 2:
                resid=R.drawable.icon_play_selected;
                break;
            case 3:
                resid=R.drawable.icon_playwithyou_selected;
                break;
            case 4:
                resid=R.drawable.icon_daigou_selected;
                break;
            default:
                resid=R.drawable.icon_guide_selected;
                break;
        }
        return resid;
    }
    private int getDefaultResource(int position){
        int resid=0;
        switch (position) {
            case 0:
                resid=R.drawable.icon_guide_default;
                break;
            case 1:
                resid=R.drawable.icon_car_default;
                break;
            case 2:
                resid=R.drawable.icon_play_default;
                break;
            case 3:
                resid=R.drawable.icon_playwithyou_default;
                break;
            case 4:
                resid=R.drawable.icon_daigou_default;
                break;
            default:
                resid=R.drawable.icon_guide_default;
                break;
        }
        return resid;
    }
}
