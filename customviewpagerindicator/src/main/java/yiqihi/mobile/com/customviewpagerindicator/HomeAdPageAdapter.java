package yiqihi.mobile.com.customviewpagerindicator;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;

import yiqihi.mobile.com.commonlib.MobileDeviceUtil;
import yiqihi.mobile.com.commonlib.NetUtility;
import yiqihi.mobile.com.customviewpagerindicator.R;

public class HomeAdPageAdapter extends PagerAdapter {
    private ArrayList<ImageInfo> list = new ArrayList<ImageInfo>();
    private Recylcer recylcer;
    private LinearLayout.LayoutParams lp ;
    private OnPageItemClickListener onPageItemClickListener;
    private Context mContext;

    public class Recylcer {

        private LinkedList<View> viewList = new LinkedList<View>();
        private Context context;

        public Recylcer(Context context) {
            this.context = context;
        }

        public View requestView() {
            if (viewList.size() > 0) {
                return viewList.removeFirst();
            } else {
                SimpleDraweeView imageView=new SimpleDraweeView(mContext);
                imageView.setLayoutParams(lp);

                if (!NetUtility.isNetworkAvailable(context)) {
                    imageView.setBackgroundResource(R.drawable.no_connect_img);
                } else {
//                    imageView.setBackgroundResource(R.drawable.banner_bg);//去掉默认图
                }
                return imageView;
            }
        }

        public void releaseView(View view) {
            viewList.addLast(view);
        }
    }
    public HomeAdPageAdapter(Context context, ArrayList<ImageInfo> recommends) {
        mContext = context;
        lp= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MobileDeviceUtil.dip2px(mContext,100));
        for (ImageInfo recommend : recommends) {
            list.add(recommend);
        }
        recylcer = new Recylcer(context);
    }
    public void setOnPageItemClickListener(OnPageItemClickListener onPageItemClickListener) {
        this.onPageItemClickListener = onPageItemClickListener;
    }

    public void reload(ArrayList<ImageInfo> recommends) {
        list.clear();
        for (ImageInfo recommend : recommends) {
            list.add(recommend);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // 定义count为无限大值，以循环滑动
        if (list.size() != 0) {
            return Integer.MAX_VALUE;
        } else {
            return 0;
        }
    }
    public int getItemCount() {
        return list.size();
    }

    public ImageInfo getItem(int position) {
        // 对position取余数
        return list.get(position % list.size());
    }
    /**
     * Determines whether a page View is associated with a specific key object
     * as returned by {@link #instantiateItem(ViewGroup, int)}. This method is
     * required for a PagerAdapter to function properly.
     *
     * @param view   Page View to check for association with <code>object</code>
     * @param object Object to check for association with <code>view</code>
     * @return true if <code>view</code> is associated with the key object <code>object</code>
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    public interface OnPageItemClickListener {
        public void onPageItemClick(ViewGroup parent, View item, int position);
    }
    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final SimpleDraweeView imageView = (SimpleDraweeView) recylcer.requestView();
        String url = list.get(position % list.size()).imageUrl;
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageURI(Uri.parse(url));
        ((ViewPager) container).addView(imageView, lp);
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (onPageItemClickListener != null) {
                    onPageItemClickListener.onPageItemClick(container, imageView, position);
                }
            }
        });
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object item) {
        ImageView imageView = (ImageView) item;
        ((ViewPager) container).removeView(imageView);
        imageView.setOnClickListener(null);
    }
    public ArrayList<ImageInfo> getList() {
        return list;
    }
}
