package yiqihi.mobile.com.commonlib.customview;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import yiqihi.mobile.com.commonlib.MobileDeviceUtil;

/**
 * 无数据显示视图
 */
public class NoResultView extends LinearLayout {


    public static final int SOSUO =0;// R.drawable.null_sosuo;

    public static final String DINGWEI_MESS = "十分抱歉，暂时无法定位";
    public static final String HONGQUAN_MESS = "暂无可用红券";
    public static final String LANQUAN_MESS = "暂无可用蓝券";
    public static final String DIANQUAN_MESS = "暂无可用店铺券";
    public static final String PINQUAN_MESS = "暂无可用品牌券";
    public static final String LIANJIESHIBAI_MESS = "十分抱歉，数据连接失败";
    public static final String PINGLUN_MESS = "当前商品暂无评论";
    public static final String SOSUO_MESS = "十分抱歉，暂时没有找到符合条件的商品";
    //public static final String SOSUO_MESS = "抱歉，没有找到符合的商品，换个条件试试吧~";
    public static final String TONGZHI_MESS = "您没有到货通知商品";

    private static final String RED = "#CC0000";
    private static final String HINT = "#999999";
    private static final float TOP1 = 15.0f;
    private static final float TOP2 = 10.0f;
    private static final float MAR_TOP2 = 10.0f;
    private static final float FONT1 = 18.0f;
    private static final float FONT2 = 14.0f;

    private ImageView ivLogo;
    private TextView tvName;
    public TextView tvMessage;
    private Context mContext;

    private float mDensity;
    private int mWidth;
    private int mPicWidth;
    private int mPicHeight;

    private String mMessage;
    private int mCurrentIco;
    private boolean mIsSearch;

    private void initParam() {
        mDensity = MobileDeviceUtil.getInstance(mContext.getApplicationContext()).getScreenDensity();
        mWidth = MobileDeviceUtil.getInstance(mContext.getApplicationContext()).getScreenWidth();
        mPicWidth = (mWidth / 4)*1;
        mPicHeight = mPicWidth;
    }

    /**
     * @param context
     * @param icon
     *            默认图标 为NoResultView静态变量
     * @param message
     *            默认为空，只搜索列表使用
     * @param isSearch
     *            是否为搜索
     */
    public NoResultView(Context context, int icon, String message, boolean isSearch) {
        super(context);
        mContext = context;
        this.mMessage = message;
        this.mCurrentIco = icon;
        this.mIsSearch = isSearch;
        initParam();
        initView();
    }

    /**
     * @param context
     * @param icon
     *            默认图标 为NoResultView静态变量
     * @param message
     *            默认为空，只搜索列表使用
     */
    public NoResultView(Context context, int icon, String message) {
        super(context);
        mContext = context;
        this.mMessage = message;
        this.mCurrentIco = icon;
        initParam();
        initViewByIconOrmessage();
    }

    /**
     * 初始化页面视图
     */
    private void initView() {
        this.setOrientation(VERTICAL);
        this.setGravity(Gravity.CENTER);

        ivLogo = new ImageView(mContext);
        ivLogo.setScaleType(ScaleType.FIT_XY);
        if (mCurrentIco == 100 || mCurrentIco == 101 || mCurrentIco == 102) {
            ivLogo.setImageResource(SOSUO);
        } else {
            ivLogo.setImageResource(mCurrentIco != 0 ? mCurrentIco : SOSUO);
        }
        LayoutParams ivLogoParams = new LayoutParams(mPicWidth, mPicHeight);
        ivLogoParams.gravity = Gravity.CENTER_HORIZONTAL;
        ivLogoParams.topMargin = dip2px(MAR_TOP2);
        this.addView(ivLogo, ivLogoParams);

        // 不是搜索，隐藏红色文字
        if (mIsSearch) {
            tvName = new TextView(mContext);
            tvName.setTextColor(Color.parseColor(RED));
            tvName.setTextSize(FONT1);
            tvName.setText(TextUtils.isEmpty(mMessage) ? "“商品”" : String.format("“%s”", mMessage));
            LayoutParams tvNameParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            tvNameParams.setMargins(0, dip2px(TOP1), 0, 0);
            this.addView(tvName, tvNameParams);
        }

        tvMessage = new TextView(mContext);
        tvMessage.setTextColor(Color.parseColor(HINT));
        tvMessage.setTextSize(FONT2);
        LayoutParams tvMessageParams = null;
        if (mIsSearch) {
            tvMessage.setText(SOSUO_MESS);
            tvMessageParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            tvMessageParams.setMargins(0, dip2px(TOP2), 0, 0);
        } else {
            tvMessage.setText(getMessage(mCurrentIco));
            tvMessageParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            tvMessageParams.setMargins(0, dip2px(TOP1), 0, 0);
        }

        this.addView(tvMessage, tvMessageParams);
    }

    /**
     * 
     */
    private void initViewByIconOrmessage() {
        this.setOrientation(VERTICAL);
        this.setGravity(Gravity.CENTER);
        ivLogo = new ImageView(mContext);
        ivLogo.setScaleType(ScaleType.FIT_XY);
        ivLogo.setImageResource(mCurrentIco);
        LayoutParams ivLogoParams = new LayoutParams(mPicWidth, mPicHeight);
        ivLogoParams.gravity = Gravity.CENTER_HORIZONTAL;
        ivLogoParams.topMargin = dip2px(MAR_TOP2);
        this.addView(ivLogo, ivLogoParams);
        tvMessage = new TextView(mContext);
        tvMessage.setTextColor(Color.parseColor(HINT));
        tvMessage.setTextSize(FONT2);
        LayoutParams tvMessageParams = null;
        tvMessage.setText(mMessage);
        tvMessageParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        tvMessageParams.setMargins(0, dip2px(TOP1), 0, 0);

        this.addView(tvMessage, tvMessageParams);
    }

    /**
     * 根据图标获取提示语
     * 
     * @param icon
     * @return
     */
    private String getMessage(int icon) {
        String message = "";
        switch (icon) {
        default:
            message = SOSUO_MESS;
            break;
        }
        return message;
    }

    /**
     * 设置商品名称
     * 
     * @param name
     */
    public void setProductName(String name) {
        tvName.setText(String.format("“%s”", name));
    }

    private int dip2px(float dipValue) {
        return (int) (dipValue * mDensity + 0.5f);
    }

    // private float sp2px(float spValue) {
    // return (spValue * mFontScale + 0.5f);
    // }
}
