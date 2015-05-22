package yiqihi.mobile.com.commonlib;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.greenrobot.event.EventBus;


/**
 * fragment基类
 *
 * @author mahaifeng
 */
public abstract class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";
    private View mRootView;// 布局
    public int mResource;// 布局id

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(getResource(), container, false);
        initView();
        setListeners();

        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public abstract void initView();

    public void setListeners() {
    }

    ;

    public abstract int getResource();

    /**
     * 通过Id获取view
     *
     * @param id
     * @param <T>
     * @return
     */
    protected <T extends View> T findViewByIdHelper(int id) {
        View targetView = mRootView.findViewById(id);
        return targetView == null ? null : (T) targetView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
