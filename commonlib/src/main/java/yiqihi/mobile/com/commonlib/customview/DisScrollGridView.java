package yiqihi.mobile.com.commonlib.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class DisScrollGridView extends GridView {

    public DisScrollGridView(Context context) {
        super(context);
    }

    public DisScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DisScrollGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
