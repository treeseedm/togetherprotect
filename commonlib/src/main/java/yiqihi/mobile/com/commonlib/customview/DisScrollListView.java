package yiqihi.mobile.com.commonlib.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class DisScrollListView extends ListView {

    public DisScrollListView(Context context) {
        super(context);
    }

    public DisScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DisScrollListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
