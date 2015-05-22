package yiqihi.mobile.com.commonlib.customview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager.LayoutParams;

import yiqihi.mobile.com.commonlib.R;


public class LoadingDialog extends Dialog {

    public LoadingDialog(Context context) {
        super(context, R.style.Style_Dialog_Loading);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, R.style.Style_Dialog_Loading);
    }

    // public void setMessage(String msg) {
    // if (textView != null) {
    // this.textView.setText(msg);
    // }
    // }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutParams lp = getWindow().getAttributes();
        lp.windowAnimations = R.style.Anim_Loading_Dialog;
        lp.flags = LayoutParams.FLAG_DIM_BEHIND;
        lp.dimAmount = 0.3f;
        setContentView(R.layout.common_loading_dialog);
        // textView = (TextView) findViewById(R.id.dialog_text);
    }

    public static LoadingDialog show(Context context, String message, boolean cancelable,
            OnCancelListener cancelListener) {
        if(context == null){
            return null;
        }
        if((context instanceof Activity) && ((Activity)context).isFinishing()){
            return null;
        }
        LoadingDialog loadingDialog = new LoadingDialog(context);
        loadingDialog.setCancelable(cancelable);
        loadingDialog.setOnCancelListener(cancelListener);
        loadingDialog.show();
        // loadingDialog.setMessage(message);
        return loadingDialog;
    }

    // 解决4.0以上系统加载时点击屏幕会取消情况
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}
