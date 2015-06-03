package com.myproduct.freetogether;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.facebook.drawee.view.SimpleDraweeView;
import com.myapplication.yiqihi.http.MethodHelper;
import com.myapplication.yiqihi.task.YQHBaseTaskByListeners;
import com.myproduct.freetogether.bean.ActivitiesResponse;
import com.myproduct.freetogether.bean.BaseResponse;
import com.myproduct.freetogether.bean.Car;
import com.myproduct.freetogether.bean.Detail;
import com.myproduct.freetogether.bean.GuideRequest;
import com.myproduct.freetogether.bean.Me;
import com.myproduct.freetogether.bean.People;

import org.x.net.Msg;
import org.x.net.MsgEvent;

import java.io.File;

import http.HttpImageUtils;
import yiqihi.mobile.com.commonlib.BaseActivity;
import yiqihi.mobile.com.commonlib.CommonUtility;
import yiqihi.mobile.com.commonlib.PickerSelectListener;
import yiqihi.mobile.com.commonlib.UploadImage;

public class PublishGuideActivity extends BaseActivity implements View.OnClickListener, PickerSelectListener, MsgEvent {
    private static final String TAG="PublishGuideActivity";
    private static final String ID="ID";
    private static final String ADDPHOTOTAG="add";
    private static final String DELETEPHOTOTAG="delete";
    private TextView tv_title, et_date, tv_car, tv_pingtan, tv_myinfo_text, tv_myfino, tv_yaoqiu, tv_savetime;
    private ImageView iv_phone, iv_more;
    private ImageView iv_go_data, iv_go_car, iv_go_pingtan, iv_myinfo_go, iv_go_savetime, iv_add1, iv_add2, iv_add3;
    private EditText et_title, et_location, et_datanum, et_totalpeople, et_zhaomupeople, et_money, et_des;
    private RelativeLayout rl_myinfo, rl_yaoqiu;
    private CheckBox cb_agree;
    private SimpleDraweeView sdv_image1, sdv_image2, sdv_image3;
    private LinearLayout ll_canclepublish, ll_publish;
    private Me me;
    private Detail mDetail;
    private Button start_camera, select_from_gallery, btn_takecancle;

    private File photoFile;
    private String mId;
    private int mSelectPicPosition;

    @Override
    public int getResourceView() {
        return R.layout.activity_publish_guide;
    }

    @Override
    public void initParams() {
       mId=getIntent().getStringExtra(ID);
    }

    @Override
    protected void initView() {
        iv_phone = findViewByIdHelper(R.id.iv_phone);
        iv_phone.setVisibility(View.VISIBLE);
        iv_more = findViewByIdHelper(R.id.iv_more);
        iv_more.setImageResource(R.drawable.icon_more);
        tv_title = findViewByIdHelper(R.id.tv_title);
        tv_title.setText("发布<拼导游>");

        iv_go_data = findViewByIdHelper(R.id.iv_go_data);
        iv_go_car = findViewByIdHelper(R.id.iv_go_car);
        iv_go_pingtan = findViewByIdHelper(R.id.iv_go_pingtan);
        iv_go_savetime = findViewByIdHelper(R.id.iv_go_savetime);
        iv_add1 = findViewByIdHelper(R.id.iv_add1);
        iv_add2 = findViewByIdHelper(R.id.iv_add2);
        iv_add3 = findViewByIdHelper(R.id.iv_add3);

        et_title = findViewByIdHelper(R.id.et_title);
        et_location = findViewByIdHelper(R.id.et_location);
        et_date = findViewByIdHelper(R.id.et_date);
        et_datanum = findViewByIdHelper(R.id.et_datanum);
        et_totalpeople = findViewByIdHelper(R.id.et_totalpeople);
        et_zhaomupeople = findViewByIdHelper(R.id.et_zhaomupeople);
        et_money = findViewByIdHelper(R.id.et_money);
        et_des = findViewByIdHelper(R.id.et_des);
        tv_savetime = findViewByIdHelper(R.id.tv_savetime);

        rl_myinfo = findViewByIdHelper(R.id.rl_myinfo);
        rl_yaoqiu = findViewByIdHelper(R.id.rl_yaoqiu);

        cb_agree = findViewByIdHelper(R.id.cb_agree);

        sdv_image1 = findViewByIdHelper(R.id.sdv_image1);
        sdv_image2 = findViewByIdHelper(R.id.sdv_image2);
        sdv_image3 = findViewByIdHelper(R.id.sdv_image3);

        ll_canclepublish = findViewByIdHelper(R.id.ll_canclepublish);
        ll_publish = findViewByIdHelper(R.id.ll_publish);

        tv_car = findViewByIdHelper(R.id.tv_car);
        tv_pingtan = findViewByIdHelper(R.id.tv_pingtan);

        tv_myinfo_text = findViewByIdHelper(R.id.tv_myinfo_text);
        iv_myinfo_go = findViewByIdHelper(R.id.iv_myinfo_go);

        tv_yaoqiu = findViewByIdHelper(R.id.tv_yaoqiu);

        tv_myfino=findViewByIdHelper(R.id.tv_myfino);
    }

    @Override
    protected void setListener() {
        iv_go_data.setOnClickListener(this);
        et_date.setOnClickListener(this);

        iv_go_car.setOnClickListener(this);
        tv_car.setOnClickListener(this);

        iv_go_pingtan.setOnClickListener(this);
        tv_pingtan.setOnClickListener(this);

        tv_myinfo_text.setOnClickListener(this);
        iv_myinfo_go.setOnClickListener(this);

        iv_go_savetime.setOnClickListener(this);
        tv_savetime.setOnClickListener(this);
        iv_add1.setOnClickListener(this);
        iv_add2.setOnClickListener(this);
        iv_add3.setOnClickListener(this);
        iv_add1.setTag(ADDPHOTOTAG);
        iv_add2.setTag(ADDPHOTOTAG);
        iv_add3.setTag(ADDPHOTOTAG);

        ll_canclepublish.setOnClickListener(this);
        ll_publish.setOnClickListener(this);

        rl_yaoqiu.setOnClickListener(this);
    }

    @Override
    protected void setData() {

    }

    private PopupWindow mDataPickerPop;
    private PopupWindow mCarPickerPop;
    private PopupWindow mPingtanPop;
    private PopupWindow mExpirePop;

    private void initDataPopView(boolean isShow) {
        if (mDataPickerPop == null) {
            View dataPopView = CommonUtility.initDateTimePicker(PublishGuideActivity.this, PublishGuideActivity.this, 0, 2100);
            mDataPickerPop = new PopupWindow(dataPopView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mDataPickerPop.showAtLocation(findViewByIdHelper(R.id.ll_parent), Gravity.CENTER, 0, 0);
        } else {
            if (isShow)
                mDataPickerPop.showAtLocation(findViewByIdHelper(R.id.ll_parent), Gravity.CENTER, 0, 0);
            else
                mDataPickerPop.dismiss();
        }
    }

    private void initCarPickerPop(boolean isShow, PopupWindow popupWindow, String[] values, final TextView textView) {
        if (popupWindow == null) {

            popupWindow = new PopupWindow( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            final PopupWindow finalPopupWindow = popupWindow;
            View dataPopView = CommonUtility.InitTextPicker(PublishGuideActivity.this, new PickerSelectListener() {
                @Override
                public void onOKClick(String year, String month, String day) {

                }

                @Override
                public void onCancleClick() {
                    finalPopupWindow.dismiss();
                }

                @Override
                public void onOKClick(int index, String value) {
                    finalPopupWindow.dismiss();
                    textView.setText(value);
                    textView.setTag(index);
                }
            }, values);
            popupWindow.setContentView(dataPopView);

            popupWindow.showAtLocation(findViewByIdHelper(R.id.ll_parent), Gravity.CENTER, 0, 0);

        } else {
            if (isShow)
                popupWindow.showAtLocation(findViewByIdHelper(R.id.ll_parent), Gravity.CENTER, 0, 0);
            else
                popupWindow.dismiss();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_go_data:
            case R.id.et_date:
                initDataPopView(true);
                break;
            case R.id.iv_go_car:
            case R.id.tv_car:
                initCarPickerPop(true, mCarPickerPop, Car.carValues, tv_car);
                break;
            case R.id.iv_go_pingtan:
            case R.id.tv_pingtan:
                initCarPickerPop(true, mPingtanPop, GuideRequest.CARGEWAY, tv_pingtan);
                break;
            case R.id.iv_myinfo_go:
            case R.id.tv_myinfo_text:
            case R.id.tv_myfino:
                startActivityForResult(new Intent(PublishGuideActivity.this, MyInfoActivity.class), 1);
                break;
            case R.id.rl_yaoqiu:
                startActivityForResult(new Intent(PublishGuideActivity.this,DetailActivity.class),2);
                break;
            case R.id.tv_savetime:
            case R.id.iv_go_savetime:
                initCarPickerPop(true, mExpirePop, GuideRequest.getExpire(), tv_savetime);
                break;
            case R.id.iv_add1:
                if(DELETEPHOTOTAG.equals(view.getTag())){
                    sdv_image1.setImageResource(R.drawable.bg_takephone);
                    changeAddImageState(view,ADDPHOTOTAG,R.drawable.icon_addphone);
                    break;
                }
            case R.id.iv_add2:
                if(DELETEPHOTOTAG.equals(view.getTag())){
                    sdv_image2.setImageResource(R.drawable.bg_takephone);
                    changeAddImageState(view, ADDPHOTOTAG, R.drawable.icon_addphone);
                    break;
                }
            case R.id.iv_add3:
                if(DELETEPHOTOTAG.equals(view.getTag())){
                    sdv_image2.setImageResource(R.drawable.bg_takephone);
                    changeAddImageState(view, ADDPHOTOTAG, R.drawable.icon_addphone);
                    break;
                }
                if(ADDPHOTOTAG.equals(view.getTag())){
                    Intent intent=new Intent(PublishGuideActivity.this, TakePhotoActivity.class);
                    mSelectPicPosition=view.getId();
                    startActivityForResult(intent, 1);
                }
                break;
            case R.id.ll_publish:
                save();
                break;
            case R.id.ll_canclepublish:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onOKClick(String year, String month, String day) {
        String dateStr = year + "-" + month + "-" + day;
        et_date.setText(dateStr);
        onCancleClick();
    }
    private void changeAddImageState(View view,String tag,int resid) {
        view.setTag(tag);
        ((ImageView) view).setImageResource(resid);
    }
    @Override
    public void onCancleClick() {
        if (mDataPickerPop != null)
            mDataPickerPop.dismiss();
    }

    @Override
    public void onOKClick(int index, String value) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:
                if (data != null) {
                    me = (Me) data.getSerializableExtra("me");
                    if (me != null) {
                        tv_myfino.setText(me.myInfo);
                    }
                }
                break;
            case 2:
                if (data != null) {
                    mDetail = (Detail) data.getSerializableExtra("detail");
                    if (mDetail != null) {
                        tv_yaoqiu.setText(mDetail.detail);
                    }
                }
                break;
            case Activity.RESULT_OK:
                if (null != data) {
                    final ContentResolver cr = this.getContentResolver();
                    final Uri uri = data.getData();
                    switch (mSelectPicPosition){
                        case R.id.iv_add1:
                            sdv_image1.setImageURI(uri);
                            iv_add1.setTag(DELETEPHOTOTAG);
                            iv_add1.setImageResource(R.drawable.icon_deletepic);
                            break;
                        case R.id.iv_add2:
                            sdv_image2.setImageURI(uri);
                            iv_add2.setTag(DELETEPHOTOTAG);
                            iv_add2.setImageResource(R.drawable.icon_deletepic);
                            break;
                        case R.id.iv_add3:
                            sdv_image3.setImageURI(uri);
                            iv_add3.setTag(DELETEPHOTOTAG);
                            iv_add3.setImageResource(R.drawable.icon_deletepic);
                            break;
                        default:
                            break;
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            String filepath = HttpImageUtils.getFilePath(uri, getApplicationContext());
                            Log.i(TAG,"filepath=="+filepath);
                            Bitmap mBitmap = HttpImageUtils.getSmallBitmap(HttpImageUtils.fixSlashes(filepath));
                            // //发送到服务器
                            if (mBitmap == null) {
                                return;
                            }
                            UploadImage.uploadFile(mBitmap, PublishGuideActivity.this);

                            // pic.setImageBitmap(comp(bitmap));

                            // 通知Handler扫描图片完成
//                            Message msg = new Message();
//                            msg.what = 101;
//                            msg.obj = mBitmap;
//                            mHandler.sendMessage(msg);
                        }
                    }).start();
                }
                break;
            default:
                break;
        }
    }
    public void save(){
        try{
            GuideRequest request=new GuideRequest();
            if(TextUtils.isEmpty(mId)){
                request.bizType="0";
            }else
                request.bizType=mId;
            request.location=et_location.getText().toString();
            request.deptData=et_date.getText().toString();
            request.Day=et_datanum.getText().toString();
            Car car=new Car();
            car.enabled=tv_car.getTag()==null?0:Integer.valueOf(tv_car.getTag().toString());
            request.car=car;
            People people=new People();
            people.person=et_totalpeople.getText().toString().length()==0?0:Integer.valueOf(et_totalpeople.getText().toString());
            request.people=people;
            request.recruiteCount=et_zhaomupeople.getText().toString().length()==0?0:Integer.valueOf(et_zhaomupeople.getText().toString());
            request.price=et_money.getText().toString();
            request.chargeWay=tv_pingtan.getTag()==null?0:Integer.valueOf(tv_pingtan.getTag().toString());
            request.description=et_des.getText().toString();
            request.me=me;
            request.detail=mDetail;
            request.expire=tv_savetime.getTag()==null?0:Integer.valueOf(tv_savetime.getTag().toString());
            YQHBaseTaskByListeners task=new YQHBaseTaskByListeners(PublishGuideActivity.this,true,
                    YQHBaseTaskByListeners.ACTION_WRITEREQUIRE, MethodHelper.writeRequire(request.getRequestMsg()).toString(),this);
            task.exec(false);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(Msg.DataType type, String action, String key, Object data) {
        String result = data.toString();
        System.out.println("response:" + result);
        if (TextUtils.isEmpty(result)) {
            CommonUtility.showToast(this, "获取数据失败!");
        } else {
            BaseResponse mResponse = JSON.parseObject(result, BaseResponse.class);
            if (!mResponse.xeach) {
                CommonUtility.showToast(this, "获取数据失败!");
            } else {
                CommonUtility.showToast(this, "发布成功!");
                finish();
            }
        }
    }
}
