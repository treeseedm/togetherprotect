package yiqihi.mobile.com.commonlib.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

public class DisScrollExpandableListView extends ExpandableListView {

    public DisScrollExpandableListView(Context context) {
        super(context);
    }

    public DisScrollExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DisScrollExpandableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
