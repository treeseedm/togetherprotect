package yiqihi.mobile.com.commonlib.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import yiqihi.mobile.com.commonlib.MobileDeviceUtil;
import yiqihi.mobile.com.commonlib.R;

public class SunRefreshView extends BaseRefreshView implements Animatable {

    private static final float SCALE_START_PERCENT = 0.5f;
    private static final int ANIMATION_DURATION = 1000;

    private final static float SKY_RATIO = 0.65f;
    private static final float SKY_INITIAL_SCALE = 1.05f;

    private final static float TOWN_RATIO = 0.22f;
    private static final float TOWN_INITIAL_SCALE = 1.20f;
    private static final float TOWN_FINAL_SCALE = 1.30f;

    private static final float SUN_FINAL_SCALE = 0.75f;
    private static final float SUN_INITIAL_ROTATE_GROWTH = 1.2f;
    private static final float SUN_FINAL_ROTATE_GROWTH = 1.5f;

    private static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();

    private View mParent;
    private Matrix mMatrix;
    private Animation mAnimation;

    private int mTop;
    private int mScreenWidth;

    private int mSkyHeight;
    private float mSkyTopOffset;
    private float mSkyMoveOffset;

    private int mTownHeight;
    private float mTownInitialTopOffset;
    private float mTownFinalTopOffset;
    private float mTownMoveOffset;

    private int mSunSize = 100;
    private float mSunLeftOffset;
    private float mSunTopOffset;

    private float mPercent = 0.0f;
    private float mRotate = 0.0f;

    private Bitmap mSky;
    private Bitmap mSun;
    private Bitmap mTown;
    private Bitmap mSheepFace;
    private boolean isRefreshing = true;

    private int mtotalDragDistance = -1;

    private int type = 0;

    public SunRefreshView(Context context, View parent, int TotalDragDistance) {
        super(context, parent);
        mParent = parent;
        mMatrix = new Matrix();
        mtotalDragDistance = TotalDragDistance;
        initiateDimens();
        createBitmaps();
        setupAnimations();
    }

    private void initiateDimens() {
        mScreenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        mSkyHeight = (int) (SKY_RATIO * mScreenWidth);
        mSkyTopOffset = (mSkyHeight * 0.38f);
        mSunSize = mScreenWidth/6;
        mSkyMoveOffset = MobileDeviceUtil.dip2px(getContext(), 15);
        mTownHeight = (int) (TOWN_RATIO * mScreenWidth);
        mTownInitialTopOffset = (mtotalDragDistance - mTownHeight * TOWN_INITIAL_SCALE);
        mTownFinalTopOffset = (mtotalDragDistance - mTownHeight * TOWN_FINAL_SCALE);
        mTownMoveOffset = MobileDeviceUtil.dip2px(getContext(), 10);

        mSunLeftOffset = mScreenWidth / 2.0f;
        mSunTopOffset = (mtotalDragDistance * 0.33f);

    }

    private void createBitmaps() {

        mTown = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.refresh_buildings);
        mTown = Bitmap.createScaledBitmap(mTown, mScreenWidth, (int) (mScreenWidth * TOWN_RATIO), true);
        mSun = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.refresh_sheep_body);
        mSun = Bitmap.createScaledBitmap(mSun, mSunSize, mSunSize, true);
        mSheepFace = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.refresh_sheep_face);
        mSheepFace = Bitmap.createScaledBitmap(mSheepFace, mSunSize, mSunSize, true);

    }

    @Override
    public void offsetTopAndBottom(int offset) {
        mTop = offset;
        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        final int saveCount = canvas.save();
        if(isRefreshing){
            drawSun(canvas);
            drawSheepFace(canvas);
        }
        canvas.restoreToCount(saveCount);
    }

    private void drawSun(Canvas canvas) {
        Matrix matrix = mMatrix;
        matrix.reset();
        float dragPercent = mPercent;
        if (dragPercent < 0.2f) {
            return;
        }
        if (dragPercent > 1.0f) {
            dragPercent = (dragPercent + 9.5f) / 10;

        }
        if (dragPercent > 1.0) {
            dragPercent = 1.0f;
        }
        float sunRadius = (float) mSunSize / 2.0f;
        float offsetX = mSunLeftOffset - sunRadius;
        float offsetY = mSunTopOffset
                + (mtotalDragDistance / 2f) * Math.abs((1.0f - dragPercent) * 1.55f); // Move the sun up
        float scalePercentDelta = dragPercent - SCALE_START_PERCENT;
//        BDebug.e("sun","dragPercent:"+dragPercent);
//        BDebug.e("sun","mSunTopOffset:"+mSunTopOffset);
//        BDebug.e("sun","mtotalDragDistance:"+mtotalDragDistance);
//        BDebug.e("sun","SCALE_START_PERCENT:"+SCALE_START_PERCENT);
//        BDebug.e("sun","offsetY:"+offsetX);
//        BDebug.e("sun","offsetX:"+offsetY);
        if (scalePercentDelta > 0) {
            matrix.preTranslate(offsetX, offsetY );
           // matrix.preScale(sunScale, sunScale);
            offsetX += sunRadius;
            offsetY = offsetY + sunRadius;
        } else {
            matrix.postTranslate(offsetX, offsetY);
            offsetX += sunRadius;
            offsetY += sunRadius;
        }
        matrix.postRotate(-120 * mRotate, offsetX, offsetY);
        canvas.drawBitmap(mSun, matrix, null);
    }

    private void drawSheepFace(Canvas canvas) {
        Matrix matrix = mMatrix;
        matrix.reset();
        float dragPercent = mPercent;
        if (dragPercent < 0.2f) {
            return;
        }
        if (dragPercent > 1.0f) {
            dragPercent = (dragPercent + 9.5f) / 10;

        }
        if (dragPercent > 1.0f) {
            dragPercent = 1.0f;

        }
        float sunRadius = (float) mSunSize / 2.0f;
        float offsetX = mSunLeftOffset - sunRadius;
        float offsetY = mSunTopOffset
                + (mtotalDragDistance / 2f) * Math.abs((1.0f - dragPercent) * 1.55f); // Move the sun up
        float scalePercentDelta = dragPercent - SCALE_START_PERCENT;
        if (scalePercentDelta > 0) {
            matrix.preTranslate(offsetX, offsetY );
        } else {
            matrix.postTranslate(offsetX, offsetY);
        }
        canvas.drawBitmap(mSheepFace, matrix, null);
    }

    @Override
    public void setPercent(float percent, boolean invalidate) {
        setPercent(percent);
       // BDebug.e("refresh","refresh percent:"+percent);
        if (invalidate) setRotate(percent);
    }

    public void setDistance(int distance){
        mtotalDragDistance = distance;
        mSunTopOffset = (mtotalDragDistance * 0.33f);
    }
    public void setPercent(float percent) {
        mPercent = percent;
    }

    public void setRotate(float rotate) {
        mRotate = rotate;
        invalidateSelf();
    }

    public void resetOriginals() {
        setPercent(0);
        setRotate(0);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, mSkyHeight + top);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public void start() {
        mAnimation.reset();
        isRefreshing = true;
        mParent.startAnimation(mAnimation);
    }

    public void setRefreshing(boolean isRefreshing){
        this.isRefreshing = isRefreshing;
    }
    @Override
    public void stop() {
        mParent.clearAnimation();
        isRefreshing = false;
        resetOriginals();
    }

    private void setupAnimations() {
        mAnimation = new Animation() {
            @Override
            public void applyTransformation(float interpolatedTime, Transformation t) {
                setRotate(interpolatedTime);
            }
        };
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setRepeatMode(Animation.RESTART);
        mAnimation.setInterpolator(LINEAR_INTERPOLATOR);
        mAnimation.setDuration(ANIMATION_DURATION);
        AlphaAnimation alphaAnimation1 = new AlphaAnimation(0.1f, 1.0f);
        alphaAnimation1.setDuration(790);
        alphaAnimation1.setRepeatCount(Animation.INFINITE);
        alphaAnimation1.setRepeatMode(Animation.REVERSE);
        mParent.findViewById(R.id.left_cloud).setAnimation(alphaAnimation1);
        alphaAnimation1.start();

        AlphaAnimation alphaAnimation2 = new AlphaAnimation(0.1f, 1.0f);
        alphaAnimation2.setDuration(870);
        alphaAnimation2.setRepeatCount(Animation.INFINITE);
        alphaAnimation2.setRepeatMode(Animation.REVERSE);
        mParent.findViewById(R.id.right_cloud).setAnimation(alphaAnimation2);
        alphaAnimation2.start();


        AlphaAnimation alphaAnimation3 = new AlphaAnimation(0.1f, 1.0f);
        alphaAnimation3.setDuration(920);
        alphaAnimation3.setRepeatCount(Animation.INFINITE);
        alphaAnimation3.setRepeatMode(Animation.REVERSE);
        mParent.findViewById(R.id.bottom_cloud).setAnimation(alphaAnimation3);
        alphaAnimation3.start();
    }


}
