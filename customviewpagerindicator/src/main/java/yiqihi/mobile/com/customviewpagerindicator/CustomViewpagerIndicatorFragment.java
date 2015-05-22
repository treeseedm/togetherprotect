package yiqihi.mobile.com.customviewpagerindicator;


import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;
import yiqihi.mobile.com.commonlib.BaseFragment;
import yiqihi.mobile.com.commonlib.CommonConstant;


public class CustomViewpagerIndicatorFragment extends BaseFragment {

    private ViewPager mHomeViewpager;// 首页焦点图Viewpager
    private PageIndicator mHomeViewpagerIndictor;// 首页焦点图滑动标记
    private int currentPageState = ViewPager.SCROLL_STATE_IDLE;// Viewpager状态
    private HomeAdPageAdapter mAdpagerAdapter;// 首页焦点图adapter
    private Timer mTimer;
    private TimerTask mTimerTask;
    private Handler myHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(currentPageState==ViewPager.SCROLL_STATE_IDLE){
                   if(mHomeViewpager.getAdapter()!=null){
                       int currentPosition=mHomeViewpager.getCurrentItem();
//                       mHomeViewpagerIndictor.setCurrentPage(currentPosition+1);
                       mHomeViewpager.setCurrentItem(currentPosition+1,true);
                   }
            }
        }
    };
    @Override
    public void initView() {
        mHomeViewpager=findViewByIdHelper(R.id.vp_home_viewpager);
        mHomeViewpagerIndictor=findViewByIdHelper(R.id.pi_home_page_indictor);
        mHomeViewpagerIndictor.setPageOrginal(false);
        mTimer=new Timer(false);
        mTimerTask=new TimerTask() {
            @Override
            public void run() {
                myHandler.sendEmptyMessage(0);
            }
        };

    }

    @Override
    public void setListeners() {
        mHomeViewpager.setOnPageChangeListener(new MyOnPageChangeListener());
        super.setListeners();
    }

    @Override
    public int getResource() {
        return R.layout.costom_viewpagerindicator;
    }
    public void setData(final  ArrayList<ImageInfo> imageInfos){
        mHomeViewpagerIndictor.setTotalPageSize(imageInfos.size());
        mAdpagerAdapter = new HomeAdPageAdapter(getActivity(), imageInfos);
        mHomeViewpager.setAdapter(mAdpagerAdapter);
        mHomeViewpagerIndictor.setCurrentPage(0);
//        if (imageInfos.size() != 0) {
//            int maxSize = 65535;
//            int pos = (maxSize / 2) - (maxSize / 2) % imageInfos.size(); // 计算初始位置
//            mHomeViewpager.setCurrentItem(pos, true);
//        }
        mAdpagerAdapter.setOnPageItemClickListener(new HomeAdPageAdapter.OnPageItemClickListener() {
            @Override
            public void onPageItemClick(ViewGroup parent, View item, int position) {
                EventBus.getDefault().post(new ViewpagerIndicatorEvent(position % imageInfos.size()));
            }
        });
        mTimer.schedule(mTimerTask,5000,5000);
    }
    /**
     * 首页焦点图滑动监听
     *
     * @author liuyang-ds
     */
    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int state) {
            currentPageState = state;
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int index) {
            mHomeViewpagerIndictor.setCurrentPage(index % mAdpagerAdapter.getItemCount());
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimerTask.cancel();
        mTimer.cancel();
    }
}
