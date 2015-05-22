package yiqihi.mobile.com.commonlib.customview;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

import yiqihi.mobile.com.commonlib.CommonUtility;
import yiqihi.mobile.com.commonlib.MobileDeviceUtil;

/**
 * 实现监听左右滑动的事件，哪个view需要的时候直接setOnTouchListener就可以用了
 *
 * @author LinZhiquan
 */
public class GestureListener extends SimpleOnGestureListener implements OnTouchListener {
    private Context mContext;
    /**
     * 左右滑动的最短距离
     */
    private int distanceX = 200;
    private int distanceY = 10;
    /**
     * 左右滑动的最大速度
     */
    private int mVelocity = 200;

    private GestureDetector gestureDetector;

    public GestureListener(Context context) {
        super();
        mContext = context;
        gestureDetector = new GestureDetector(context, this);
        distanceX = MobileDeviceUtil.getInstance(context).getScreenWidth() / 3;
    }

    /**
     * 向上滑动
     *
     * @return
     */
    public boolean top(float distanceY) {
        return false;
    }

    /**
     * 向下滑动
     *
     * @return
     */
    public boolean bottom(float distanceY) {
        return false;
    }

    /**
     * 向左滑的时候调用的方法，子类应该重写
     *
     * @return
     */
    public boolean left() {
        return false;
    }

    /**
     * 向右滑的时候调用的方法，子类应该重写
     *
     * @return
     */
    public boolean right() {
        return false;
    }

    public boolean topStop() {
        return false;
    }

    public boolean bottomStop() {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        Log.e("====", "velocity==" + velocityY);
        // TODO Auto-generated method stub
        // e1：第1个ACTION_DOWN MotionEvent
        // e2：最后一个ACTION_MOVE MotionEvent
        // velocityX：X轴上的移动速度（像素/秒）
        // velocityY：Y轴上的移动速度（像素/秒）
        if (e1 == null || e2 == null) {
            return false;
        }
        // 向左滑
        if (e1.getX() - e2.getX() > distanceX
                && Math.abs(velocityX) > mVelocity) {
            left();
        }
        // 向右滑
        if (e2.getX() - e1.getX() > distanceX
                && Math.abs(velocityX) > mVelocity) {
            right();
        }
        if (e1.getY() > e2.getY() && Math.abs(velocityY) > mVelocity && velocityY < 0) {//向上快速滑动
            Log.e("====", "velocity==topStop==" + velocityY);
            topStop();
        } else if (e2.getY() > e1.getY() && Math.abs(velocityY) > mVelocity && velocityY > 0) {//向下快速滑动
            Log.e("====", "velocity==bottomStop==" + velocityY);
            bottomStop();
        } else if (e1.getY() > e2.getY() && Math.abs(velocityY) > mVelocity && velocityY > 0) {//向上缓慢滑动之后快速下滑
            Log.e("====", "velocity==bottomStop==" + velocityY);
            bottomStop();
        } else {
//            CommonUtility.showToast(mContext, "哇咔咔");
        }
//        else if(Math.abs(velocityY) > velocity&&velocity>0){//有一种情况，手指按着往下慢慢滑动然后猛地往下滑动，这时e1.getY()>e2.getY但是velocity大于0,此时强制显示
//            bottomStop();
//        }
//        Log.v("ss","velocityX==="+velocityX);
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//        Log.e("====","distanceY"+distanceY);
        if (e1.getY() > e2.getY() && distanceY >= 0) {//向上滑动
            top(distanceY);
        } else if (e2.getY() > e1.getY() && distanceY < 0) {//乡下滑动
            bottom(distanceY);
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        gestureDetector.onTouchEvent(event);
        return false;
    }

    public int getDistanceX() {
        return distanceX;
    }

    public void setDistanceX(int distance) {
        this.distanceX = distance;
    }

    public int getVelocity() {
        return mVelocity;
    }

    public void setVelocity(int velocity) {
        this.mVelocity = velocity;
    }

    public GestureDetector getGestureDetector() {
        return gestureDetector;
    }

    public void setGestureDetector(GestureDetector gestureDetector) {
        this.gestureDetector = gestureDetector;
    }
}

