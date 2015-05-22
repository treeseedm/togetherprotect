package yiqihi.mobile.com.commonlib;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.EventListener;

import de.greenrobot.event.EventBus;

public abstract  class BaseActivity extends FragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResourceView());
        EventBus.getDefault().register(this);
        initParams();
        initView();
        setListener();
        setData();
    }


    public abstract int getResourceView();
    public abstract  void initParams();
    protected abstract void initView();
    protected abstract void setListener();
    protected abstract void setData();
    /**
     * 通过Id获取view
     *
     * @param id
     * @param <T>
     * @return
     */
    protected <T extends View> T findViewByIdHelper(int id) {
        View targetView =findViewById(id);
        return targetView == null ? null : (T) targetView;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    public void onEvent(EventListener eventListener) {

    }
}
