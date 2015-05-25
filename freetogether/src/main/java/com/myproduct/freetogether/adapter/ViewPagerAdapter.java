package com.myproduct.freetogether.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.myproduct.freetogether.R;
import com.myproduct.freetogether.fragment.ActivityFragment;

import yiqihi.mobile.com.commonlib.customview.PagerSlidingTabStrip;

public class ViewPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.ViewTabProvider {
    private String[] mTags;
    private Context mContext;

    public ViewPagerAdapter(FragmentManager fm, Context context, String[] tags) {
        super(fm);
        mTags = tags;
        this.mContext = context;
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        String tag = mTags[position];
        ActivityFragment fragment = new ActivityFragment();
        Bundle params = new Bundle();
        params.putString(ActivityFragment.CATALOG, tag);
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
        switch (position){
            case 0:
               resid=R.drawable.icon_guide_selected;
                mIndicatoreColor.setBackgroundColor(mContext.getResources().getColor(R.color.guide_color));
                mTitle.setTextColor(mContext.getResources().getColor(R.color.guide_color));
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
                resid=R.drawable.icon_guide_selected;
                break;
        }
        simpleDraweeView.setImageResource(resid);
        mTitle.setText(mTags[position]);

        return view;
    }

    @Override
    public void updateViewState(View view, int position) {
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
    }
}
