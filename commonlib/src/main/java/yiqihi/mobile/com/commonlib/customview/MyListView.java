package yiqihi.mobile.com.commonlib.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import yiqihi.mobile.com.commonlib.R;

public class MyListView extends android.widget.ListView implements OnScrollListener {
    // 下拉刷新标志
    private final static int PULL_To_REFRESH = 0;
    // 松开刷新标志
    private final static int RELEASE_To_REFRESH = 1;
    // 正在刷新标志
    public final static int REFRESHING = 2;
    // 刷新完成标志
    private final static int DONE = 3;

    private LayoutInflater inflater;
    private LinearLayout headView;
    private ImageView arrowImageView;

    // 用于保证startY的值在一个完整的touch事件中只被记录一次
    private boolean isRecored;

    private int headContentHeight;
    private int headContentOriginalTopPadding;

    private int startY;

    public int state;
    private boolean isBack;
    private boolean hasMore;
    /** 是否关闭ScrollView的滑动. */
    private boolean mEnableTouch = true;
    private SunRefreshView draw ;

    private OnListViewRefreshListener mOnListViewRefreshListener;
    
    private boolean isCanRefresh=true;//是否可以下拉刷新，默认为true;
    private  OnListViewResizeListener mOnListViewResizeListener;

    public MyListView(Context context) {
        super(context);
        init(context);
        setOnScrollListener(this);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
        setOnScrollListener(this);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        setOnScrollListener(this);
    }

    private void init(Context context) {
        final Interpolator interpolator = new LinearInterpolator();
        inflater = LayoutInflater.from(context);

        headView = (LinearLayout) inflater.inflate(R.layout.mylistview_head_new_year, null);
        arrowImageView = (ImageView) headView.findViewById(R.id.head_arrowImageView);
        headContentOriginalTopPadding = headView.getPaddingTop();
        measureView(headView);
        headContentHeight = headView.getMeasuredHeight();
        headView.getMeasuredWidth();
        headView.setPadding(headView.getPaddingLeft(), -1 * headContentHeight, headView.getPaddingRight(),
                headView.getPaddingBottom());
        headView.invalidate();
        draw = new SunRefreshView(context,headView,headContentHeight);
        arrowImageView.setImageDrawable(draw);
        addHeaderView(headView);
    }

    // 计算headView的width及height值
    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
    	if(!isCanRefresh){
    		return super.onTouchEvent(ev);
    	}
        switch (ev.getAction()) {
        case MotionEvent.ACTION_DOWN:
            if (getFirstVisiblePosition() == 0 && !isRecored) {
                startY = (int) ev.getY();
                isRecored = true;
            }
            break;
        case MotionEvent.ACTION_UP:
            if (state != REFRESHING) {
                if (state == DONE) {
                } else if (state == PULL_To_REFRESH) {
                    state = DONE;
                    changeHeaderViewByState();
                } else if (state == RELEASE_To_REFRESH) {
                    state = REFRESHING;
                    changeHeaderViewByState();
                    // 刷新数据
                    if (mOnListViewRefreshListener != null) {
                        mOnListViewRefreshListener.onRefresh();
                    }
                }
            }

            isRecored = false;
            isBack = false;

            break;
        case MotionEvent.ACTION_MOVE:
            int tempY = (int) ev.getY();
            if (!isRecored && getFirstVisiblePosition() == 0) {
                isRecored = true;
                startY = tempY;
            }
            if (state != REFRESHING && isRecored) {
                // 滑动距离
                int deltaY = (int) ((tempY - startY)*0.5);
                float deltaYf = (float)((tempY - startY)*0.5f);
                //BDebug.e("Percent", "mCurrentDragPercent:" + deltaYf / headContentHeight);
                draw.setPercent( deltaYf/headContentHeight,true);
                draw.offsetTopAndBottom((int)deltaYf);
                draw.setRefreshing(true);
                // 可以松开刷新了
                if (state == RELEASE_To_REFRESH) {
                    // 往上推，推到屏幕足够掩盖head的程度，但还没有全部掩盖
                    if ((deltaY < headContentHeight) && deltaY > 0) {
                        state = PULL_To_REFRESH;
                        changeHeaderViewByState();
                    }
                    // 一下子推到顶
                    else if (deltaY <= 0) {
                        state = DONE;
                        changeHeaderViewByState();
                    }
                    // 往下拉，或者还没有上推到屏幕顶部掩盖head
                    else {
                        
                    }
                }
                // 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
                else if (state == PULL_To_REFRESH) {
                    // 下拉到可以进入RELEASE_TO_REFRESH的状态
                    if (deltaY >= headContentHeight) {
                        state = RELEASE_To_REFRESH;
                        isBack = true;
                        changeHeaderViewByState();
                    }
                    // 上推到顶了
                    else if (deltaY <= 0) {
                        state = DONE;
                        changeHeaderViewByState();
                    } 
                }
                // done状态下
                else if (state == DONE) {
                    if (deltaY > 0) {
                        state = PULL_To_REFRESH;
                        changeHeaderViewByState();
                    }
                }

                int topPadding = (int) ((-1 * headContentHeight + deltaY));
                // 更新headView的paddingTop
                if (state == PULL_To_REFRESH || state == RELEASE_To_REFRESH) {
                    headView.setPadding(headView.getPaddingLeft(), topPadding, headView.getPaddingRight(),
                            headView.getPaddingBottom());
                   // headView.invalidate();
                }
            }

            break;
        default:
            break;
        }

        // 控件本身是否滑动
        return !mEnableTouch || super.onTouchEvent(ev);

    }

    // 点击刷新
    public void clickRefresh() {
        setSelection(0);
        state = REFRESHING;
        changeHeaderViewByState();
        // 刷新数据
        if (mOnListViewRefreshListener != null) {
            mOnListViewRefreshListener.onRefresh();
        }
    }

    public void onRefreshComplete(String update) {
        onRefreshComplete();
    }

    public void onRefreshComplete() {
        state = DONE;
        changeHeaderViewByState();
    }

    // 当状态改变时候，调用该方法，以更新界面
    private void changeHeaderViewByState() {
        switch (state) {
        case RELEASE_To_REFRESH:
            arrowImageView.setVisibility(View.VISIBLE);
            // 列表不可滑动
            mEnableTouch = false;
            break;
        case PULL_To_REFRESH:
            arrowImageView.setVisibility(View.VISIBLE);
            if (isBack) {
                isBack = false;
            }
            // 列表不可滑动
            mEnableTouch = false;
            break;
        case REFRESHING:
            if(draw!=null){
                draw.start();
            }
            headView.setPadding(headView.getPaddingLeft(), headContentOriginalTopPadding, headView.getPaddingRight(),
                    headView.getPaddingBottom());
            headView.invalidate();
            // 列表不可滑动
            mEnableTouch = false;
            break;
        case DONE:
            arrowImageView.setVisibility(View.VISIBLE);
            headView.setPadding(0, -1 * headContentHeight, 0, 0);
            // 声明动画信息
            if(draw!=null&&draw.isRunning()){
                draw.stop();
            };
            // 列表可以滑动
            mEnableTouch = true;
            break;
        }
    }

    public void setScrollState(int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        setScrollState(scrollState);
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            if(mOnListViewResizeListener!=null)
                mOnListViewResizeListener.resize();
            // 判断是否滚动到底部
            if ((view.getLastVisiblePosition() == view.getCount() - 1) && isHasMore()) {
                // 加载更多
                if (mOnListViewRefreshListener != null) {
                    mOnListViewRefreshListener.onLoadMore();
                }
            }

        }
    }

    public interface OnListViewRefreshListener {
        // 刷新
        public void onRefresh();

        // 加载更多
        public void onLoadMore();
    }
   public interface  OnListViewResizeListener{
       public void resize();
   }
    public void setOnListViewRefreshListener(OnListViewRefreshListener listener) {
        mOnListViewRefreshListener = listener;
    }
    public void setOnListViewResizeListener(OnListViewResizeListener resizeListener){
        this.mOnListViewResizeListener=resizeListener;
    };
    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    /**
     * 图片还原动画
     * 
     * @author ZhaoFangyi
     * 
     *         Create at 2014-6-5 下午5:53:43
     */
    public class ResetAnimimation extends Animation {
        int targetHeight;
        int originalHeight;
        int extraHeight;
        View mView;

        protected ResetAnimimation(View view, int targetHeight) {
            this.mView = view;
            this.targetHeight = -1 * targetHeight;
            originalHeight = view.getPaddingTop();
            extraHeight = this.targetHeight - originalHeight;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            int newHeight = (int) (targetHeight - extraHeight * (1 - interpolatedTime));
            mView.setPadding(mView.getPaddingLeft(), newHeight, mView.getPaddingRight(), mView.getPaddingBottom());
            mView.invalidate();
        }
    }
    /**
     * @Description: 是否可以下拉刷新，默认为true
     * @author mahaifeng 
     * @param isCanRefresh
     * @date 2014-12-29 上午11:06:05
     */
    public void setIsCanRefresh(boolean isCanRefresh){
    	this.isCanRefresh=isCanRefresh;
    }
}