package com.myproduct.freetogether.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.myapplication.yiqihi.constant.Constants;
import com.myproduct.freetogether.R;
import com.myproduct.freetogether.bean.Item;

import org.apmem.tools.layouts.FlowLayout;

import java.net.URI;

import yiqihi.mobile.com.commonlib.AdapterBase;

public class GuideAdapter extends AdapterBase<Item> {
    private static final String TAG = "GuideAdapter";
    private Context mContext;

    public GuideAdapter(Context context) {
        this.mContext = context;
    }

    private class ViewHolder {
        TextView tv_publishtime, title, tv_address, tv_time, tv_money, tv_des, tv_peoplenum, tv_remainday;
        SimpleDraweeView sdv_face;
        Button btn_add;
        FlowLayout fl_container;
        ImageView iv_joinStatus;
    }

    /**
     * 【getView】
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    protected View getExView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.guide_item, null);
            vh.sdv_face = (SimpleDraweeView) convertView.findViewById(R.id.sdv_face);
            vh.tv_publishtime = (TextView) convertView.findViewById(R.id.tv_publishtime);
            vh.title = (TextView) convertView.findViewById(R.id.title);
            vh.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
            vh.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            vh.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            vh.tv_des = (TextView) convertView.findViewById(R.id.tv_des);
            vh.tv_peoplenum = (TextView) convertView.findViewById(R.id.tv_peoplenum);
            vh.tv_remainday = (TextView) convertView.findViewById(R.id.tv_remainday);
            vh.fl_container = (FlowLayout) convertView.findViewById(R.id.fl_container);
            vh.iv_joinStatus = (ImageView) convertView.findViewById(R.id.iv_joinStatus);
            vh.btn_add = (Button) convertView.findViewById(R.id.btn_add);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        try {
            Item item = getList().get(position);
            vh.sdv_face.setImageURI(Uri.parse(Constants.FACEIMAGEURL + item.userId));
            vh.tv_publishtime.setText(item.ago);
            vh.title.setText(item.title);
            vh.tv_address.setText(item.location);
            vh.tv_time.setText(item.deptDate);
            vh.tv_money.setText("￥" + item.personPrice);
            vh.tv_des.setText(item.description);
            vh.tv_peoplenum.setText(item.person + "人");
            vh.tv_remainday.setText("剩" + item.day + "天");
            int catalog = item.catalog;
            String[] tags = null;
            switch (catalog) {
                case 7:
                    tags = new String[]{"AA拼导游", "还差" + item.recruiteCount + "人", item.car, item.sex};
                    break;
                default:
                    break;
            }
            vh.fl_container.removeAllViews();
            for (int i = 0; tags != null && i < tags.length; i++) {
                Button view = (Button) View.inflate(mContext, R.layout.flow_button, null);
                view.setText(tags[i]);

                switch (catalog) {
                    case 7:
                        if (i == 0) {
                            view.setBackgroundResource(R.drawable.bg_guide);
                            view.setTextColor(mContext.getResources().getColor(R.color.white));
                        } else {
                            view.setBackgroundResource(R.drawable.bg_guide_recruitecount);
                            view.setTextColor(mContext.getResources().getColor(R.color.guide_color));
                        }
                        break;
                    default:
                        break;
                }
                vh.fl_container.addView(view, i);
            }
            int joinStatus = item.joinStatus;
            vh.iv_joinStatus.setVisibility(View.VISIBLE);
            switch (joinStatus) {
                case 0:
                    vh.iv_joinStatus.setVisibility(View.GONE);
                    break;
                case 1:
                    vh.iv_joinStatus.setImageResource(R.drawable.bg_shenhezhong);
                    break;
                case 2:
                    vh.iv_joinStatus.setImageResource(R.drawable.bg_yijujue);
                    break;
                case 3:
                    vh.iv_joinStatus.setImageResource(R.drawable.bg_yijiaru);
                    break;
                default:
                    vh.iv_joinStatus.setVisibility(View.GONE);
                    break;
            }
            if (item.isOwner) {
                vh.btn_add.setText("查看状态");
            }else if (item.isDeal && item.joinStatus != 0) {
                vh.btn_add.setText("查看状态");
            }else if(item.isFull&&item.joinStatus!=0){
                vh.btn_add.setText("查看状态");
            }else{
                vh.btn_add.setText("立即加入");
            }

            vh.btn_add.setBackgroundResource(R.drawable.bg_guide_add);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
