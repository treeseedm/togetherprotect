package com.myproduct.freetogether.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.myproduct.freetogether.R;
import com.myproduct.freetogether.fragment.GuideFragment;

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
        GuideFragment fragment = new GuideFragment();
        Bundle params = new Bundle();
        params.putString(GuideFragment.TAG, tag);
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
        simpleDraweeView.setImageURI(Uri.parse("http://i8.baidu.com/it/u=1421369476,949135805&fm=73"));
        mTitle.setText("哇咔咔" + position);

        return view;
    }

    @Override
    public void updateViewState(View view, int position) {
        TextView mTitle = (TextView) view.findViewById(R.id.tv_tab);
        View mIndicatoreColor = view.findViewById(R.id.indicator_color);
        Log.v("position===","=="+position);
        switch (position) {
            case 0:
                mTitle.setTextColor(mContext.getResources().getColor(R.color.red));
                mIndicatoreColor.setBackgroundColor(mContext.getResources().getColor(R.color.red));
                mIndicatoreColor.setVisibility(View.VISIBLE);
            break;
            case 1:
                mTitle.setTextColor(mContext.getResources().getColor(R.color.green));
                mIndicatoreColor.setBackgroundColor(mContext.getResources().getColor(R.color.green));
                mIndicatoreColor.setVisibility(View.VISIBLE);
                break;
            case 2:
                mTitle.setTextColor(mContext.getResources().getColor(R.color.blue));
                mIndicatoreColor.setBackgroundColor(mContext.getResources().getColor(R.color.blue));
                mIndicatoreColor.setVisibility(View.VISIBLE);
                break;
            case 3:
                mTitle.setTextColor(mContext.getResources().getColor(R.color.dark_light));
                mIndicatoreColor.setBackgroundColor(mContext.getResources().getColor(R.color.dark_light));
                mIndicatoreColor.setVisibility(View.VISIBLE);
                break;
            case 4:
                mTitle.setTextColor(mContext.getResources().getColor(R.color.red));
                mIndicatoreColor.setBackgroundColor(mContext.getResources().getColor(R.color.red));
                mIndicatoreColor.setVisibility(View.VISIBLE);
                break;
            default:
                mTitle.setTextColor(mContext.getResources().getColor(R.color.dark));
                mIndicatoreColor.setVisibility(View.GONE);
                break;
        }
    }
}
