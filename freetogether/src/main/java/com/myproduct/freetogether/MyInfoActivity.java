package com.myproduct.freetogether;

import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.myproduct.freetogether.bean.Detail;
import com.myproduct.freetogether.bean.Me;

import yiqihi.mobile.com.commonlib.BaseActivity;
import yiqihi.mobile.com.commonlib.CommonUtility;
import yiqihi.mobile.com.commonlib.PickerSelectListener;

public class MyInfoActivity extends BaseActivity implements View.OnClickListener, PickerSelectListener {
    private static final String TAG = "MyInfoActivity";


    private TextView tv_title;
    private ImageView iv_phone, iv_more;

    private EditText et_contactPerson, et_contactMobile, et_contactMSN, et_contactQQ;
    private TextView tv_sex, tv_birthday, tv_height, tv_xuexing, tv_weight, tv_language;
    private ImageView iv_go_sex, iv_go_birthday, iv_go_height, iv_go_xuexing, iv_go_weight, iv_go_language;
    private Button btn_ok;

    @Override
    public int getResourceView() {
        return R.layout.activity_myinfo;
    }

    @Override
    public void initParams() {

    }

    @Override
    protected void initView() {
        iv_phone = findViewByIdHelper(R.id.iv_phone);
        iv_phone.setVisibility(View.VISIBLE);
        iv_more = findViewByIdHelper(R.id.iv_more);
        iv_more.setImageResource(R.drawable.icon_more);
        tv_title = findViewByIdHelper(R.id.tv_title);
        tv_title.setText("我的信息");

        et_contactPerson = findViewByIdHelper(R.id.et_contactPerson);
        et_contactMobile = findViewByIdHelper(R.id.et_contactMobile);
        et_contactMSN = findViewByIdHelper(R.id.et_contactMSN);
        et_contactQQ = findViewByIdHelper(R.id.et_contactQQ);

        tv_sex = findViewByIdHelper(R.id.tv_sex);
        tv_birthday = findViewByIdHelper(R.id.tv_birthday);
        tv_height = findViewByIdHelper(R.id.tv_height);
        tv_xuexing = findViewByIdHelper(R.id.tv_xuexing);
        tv_weight = findViewByIdHelper(R.id.tv_weight);
        tv_language = findViewByIdHelper(R.id.tv_language);

        iv_go_sex = findViewByIdHelper(R.id.iv_go_sex);
        iv_go_birthday = findViewByIdHelper(R.id.iv_go_birthday);
        iv_go_height = findViewByIdHelper(R.id.iv_go_height);
        iv_go_xuexing = findViewByIdHelper(R.id.iv_go_xuexing);
        iv_go_weight = findViewByIdHelper(R.id.iv_go_weight);
        iv_go_language = findViewByIdHelper(R.id.iv_go_language);
        btn_ok = findViewByIdHelper(R.id.btn_ok);
    }

    @Override
    protected void setListener() {
        tv_sex.setOnClickListener(this);
        tv_birthday.setOnClickListener(this);
        tv_height.setOnClickListener(this);
        tv_xuexing.setOnClickListener(this);
        tv_weight.setOnClickListener(this);
        tv_language.setOnClickListener(this);

        iv_go_sex.setOnClickListener(this);
        iv_go_birthday.setOnClickListener(this);
        iv_go_height.setOnClickListener(this);
        iv_go_xuexing.setOnClickListener(this);
        iv_go_weight.setOnClickListener(this);
        iv_go_language.setOnClickListener(this);

        btn_ok.setOnClickListener(this);
    }

    @Override
    protected void setData() {

    }

    private PopupWindow mDataPickerPop;

    private void initDataPopView() {
        if (mDataPickerPop == null) {
            View dataPopView = CommonUtility.initDateTimePicker(MyInfoActivity.this, MyInfoActivity.this, 1900, 0);
            mDataPickerPop = new PopupWindow(dataPopView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mDataPickerPop.showAtLocation(findViewByIdHelper(R.id.ll_parent), Gravity.CENTER, 0, 0);
        } else {
            mDataPickerPop.showAtLocation(findViewByIdHelper(R.id.ll_parent), Gravity.CENTER, 0, 0);
        }
    }

    private void initCarPickerPop(PopupWindow popupWindow, String[] values, final TextView textView) {
        if (popupWindow == null) {
            popupWindow = new PopupWindow( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            final PopupWindow finalPopupWindow = popupWindow;
            View dataPopView = CommonUtility.InitTextPicker(MyInfoActivity.this, new PickerSelectListener() {
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
            popupWindow.showAtLocation(findViewByIdHelper(R.id.ll_parent), Gravity.CENTER, 0, 0);
        }
    }

    private PopupWindow mSexPickerPop;
    private PopupWindow mHeightPickerPop;
    private PopupWindow mXueXingPickerPop;
    private PopupWindow mWeightPickerPop;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_sex:
            case R.id.iv_go_sex:
                initCarPickerPop(mSexPickerPop, Me.sexValues, tv_sex);
                break;
            case R.id.tv_birthday:
            case R.id.iv_go_birthday:
                initDataPopView();
                break;
            case R.id.tv_height:
            case R.id.iv_go_height:
                initCarPickerPop(mHeightPickerPop, Detail.heightValues, tv_height);
                break;
            case R.id.tv_xuexing:
            case R.id.iv_go_xuexing:
                initCarPickerPop(mXueXingPickerPop, Me.bloodValues, tv_xuexing);
                break;
            case R.id.tv_weight:
            case R.id.iv_go_weight:
                initCarPickerPop(mWeightPickerPop, Me.weightValues, tv_weight);
                break;
            case R.id.tv_language:
            case R.id.iv_go_language:
                initCarPickerPop(mWeightPickerPop, Detail.langValues, tv_language);
                break;
            case R.id.btn_ok:
                save();
                break;
            default:
                break;
        }
    }

    private void save() {
        try {
            Me me = new Me();
            me.birthday = String.valueOf(tv_birthday.getText());
            me.blood = String.valueOf(tv_xuexing.getTag());
            me.contactMobile = et_contactMobile.getText().toString();
            me.contactMSN = et_contactMSN.getText().toString();
            me.contactPerson = et_contactPerson.getText().toString();
            me.contactQQ = et_contactQQ.getText().toString();
            me.height = String.valueOf(tv_height.getTag());
            me.weight = String.valueOf(tv_weight.getTag());
            me.lang = String.valueOf(tv_language.getTag());
            me.sex = String.valueOf(tv_sex.getTag());
            me.myInfo=me.sex+"  "+tv_height.getText().toString()+"  "+tv_weight.getText().toString()+"  "+tv_xuexing.getText().toString()
                    +"  "+tv_language.getText().toString()+"QQ:"+et_contactQQ.getText().toString()+","+"微信:"+et_contactMSN.getText().toString();
            Intent intent=getIntent();
            intent.putExtra("me",me);
            setResult(1,intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOKClick(String year, String month, String day) {
        String dateStr = year + "-" + month + "-" + day;
        tv_birthday.setText(dateStr);
        onCancleClick();
    }

    @Override
    public void onCancleClick() {
        mDataPickerPop.dismiss();
    }

    @Override
    public void onOKClick(int index, String value) {

    }
}
