package yiqihi.mobile.com.commonlib.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import yiqihi.mobile.com.commonlib.R;


public class ElasticScrollViewOfNewYear extends ScrollView implements  refreshInterface{
    private static final String TAG = "ElasticScrollView";
    private final static int RELEASE_To_REFRESH = 0;
    private final static int PULL_To_REFRESH = 1;
    private final static int REFRESHING = 2;
    private final static int DONE = 3;
    private final static int LOADING = 4;
    // 实际的padding的距离与界面上偏移距离的比例
    private final static int RATIO = 3;
    private int headContentHeight;
    private int refrshContentHeight;

    private LinearLayout innerLayout;
    private LinearLayout headView;
    private ImageView arrowImageView;
    private OnRefreshListener refreshListener;
    private boolean isRefreshable;
    private int state;
    private boolean isBack;
    private SunRefreshView draw ;

    private boolean canReturn;
    private boolean isRecored;
    private int startY;

    public ElasticScrollViewOfNewYear(Context context) {
        super(context);
        init(context);
    }

    public ElasticScrollViewOfNewYear(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        innerLayout = new LinearLayout(context);
        innerLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        innerLayout.setOrientation(LinearLayout.VERTICAL);

        headView = (LinearLayout) inflater.inflate(R.layout.mylistview_head_new_year, null);
        arrowImageView = (ImageView) headView.findViewById(R.id.head_arrowImageView);
        measureView(headView);
        headContentHeight = headView.getMeasuredHeight();
        refrshContentHeight = headContentHeight/2;
        headView.setPadding(0, -1 * headContentHeight, 0, 0);
        headView.invalidate();
        innerLayout.addView(headView);
        draw = new SunRefreshView(context,headView,headContentHeight);
        arrowImageView.setImageDrawable(draw);
        addView(innerLayout);
        state = DONE;
        isRefreshable = false;
        canReturn = false;
    }
    @Override
    public int getTotalDragDistance(){
        return innerLayout.getMeasuredHeight();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isRefreshable) {
            switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (getScrollY() == 0 && !isRecored) {
                    isRecored = true;
                    startY = (int) event.getY();
                    Log.i(TAG, "在down时候记录当前位置‘");
                }
                break;
            case MotionEvent.ACTION_UP:
                if (state != REFRESHING && state != LOADING) {
                    if (state == DONE) {
                        // 什么都不做
                    }
                    if (state == PULL_To_REFRESH) {
                        state = DONE;
                        changeHeaderViewByState();
                        Log.i(TAG, "由下拉刷新状态，到done状态");
                    }
                    if (state == RELEASE_To_REFRESH) {
                        state = REFRESHING;
                        changeHeaderViewByState();
                        onRefresh();
                        Log.i(TAG, "由松开刷新状态，到done状态");
                    }
                }
                isRecored = false;
                isBack = false;

                break;
            case MotionEvent.ACTION_MOVE:
                int tempY = (int) event.getY();
                if (!isRecored && getScrollY() == 0) {
                    Log.i(TAG, "在move时候记录下位置");
                    isRecored = true;
                    startY = tempY;
                }

                if (state != REFRESHING && isRecored && state != LOADING) {
                    float deltaYf = (float)((tempY - startY)*0.5);
                    draw.setPercent( deltaYf/headContentHeight,true);
                    draw.offsetTopAndBottom((int)deltaYf);
                    draw.setRefreshing(true);
                    // 可以松手去刷新了
                    if (state == RELEASE_To_REFRESH) {
                        canReturn = true;

                        if (((tempY - startY) / RATIO < refrshContentHeight) && (tempY - startY) > 0) {
                            state = PULL_To_REFRESH;
                            changeHeaderViewByState();
                            Log.i(TAG, "由松开刷新状态转变到下拉刷新状态");
                        }
                        // 一下子推到顶了
                        else if (tempY - startY <= 0) {
                            state = DONE;
                            changeHeaderViewByState();
                          
                        } else {
                            // 不用进行特别的操作，只用更新paddingTop的值就行了
                        }
                    }
                    // 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
                    if (state == PULL_To_REFRESH) {
                        canReturn = true;

                        // 下拉到可以进入RELEASE_TO_REFRESH的状态
                        if ((tempY - startY) / RATIO >= refrshContentHeight) {
                            state = RELEASE_To_REFRESH;
                            isBack = true;
                            changeHeaderViewByState();
                            
                        }
                        // 上推到顶了
                        else if (tempY - startY <= 0) {
                            state = DONE;
                            changeHeaderViewByState();
                         
                        }
                    }

                    // done状态下
                    if (state == DONE) {
                        if (tempY - startY > 0) {
                            state = PULL_To_REFRESH;
                            changeHeaderViewByState();
                        }
                    }

                    // 更新headView的size
                    if (state == PULL_To_REFRESH) {
                        headView.setPadding(0, -1 * headContentHeight + (tempY - startY) / RATIO, 0, 0);

                    }

                    // 更新headView的paddingTop
                    if (state == RELEASE_To_REFRESH) {
                        headView.setPadding(0, (tempY - startY) / RATIO - headContentHeight, 0, 0);
                    }
                    if (canReturn) {
                        canReturn = false;
                        return true;
                    }

                }
                break;
            }
        }
        return super.onTouchEvent(event);
    }

    // 当状态改变时候，调用该方法，以更新界面
    private void changeHeaderViewByState() {
        switch (state) {
        case RELEASE_To_REFRESH:
            arrowImageView.setVisibility(View.VISIBLE);
            Log.i(TAG, "当前状态，松开刷新");
            break;
        case PULL_To_REFRESH:
            arrowImageView.setVisibility(View.VISIBLE);
            // 是由RELEASE_To_REFRESH状态转变来的
            if (isBack) {
                isBack = false;
            }  
            Log.i(TAG, "当前状态，下拉刷新");
            break;
        case REFRESHING:
            headView.setPadding(0, 0, 0, 0);
            if(draw!=null){
                draw.start();
            }
            Log.i(TAG, "当前状态,正在刷新...");
            break;
        case DONE:
            headView.setPadding(0, -1 * headContentHeight, 0, 0);
            if(draw!=null){
                draw.stop();
            }
           // draw.selectDrawable(0);
            break;
        }
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    public void setonRefreshListener(OnRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
        isRefreshable = true;
    }

    public interface OnRefreshListener {
        public void onRefresh();
    }

    public void onRefreshComplete() {
        state = DONE;
        changeHeaderViewByState();
        invalidate();
        scrollTo(0, 0);
    }

    public void onRefresh() {
        if (refreshListener != null) {
            refreshListener.onRefresh();
        }
    }

    public void ResetState(){
        changeHeaderViewByState();
    }
    public void addChild(View child) {
        innerLayout.addView(child);
    }

    public void addChild(View child, int position) {
        innerLayout.addView(child, position);
    }
}
