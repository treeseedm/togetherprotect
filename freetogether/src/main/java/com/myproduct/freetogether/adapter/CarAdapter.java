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
import com.myproduct.freetogether.bean.Picture;

import org.apmem.tools.layouts.FlowLayout;

import java.util.List;

import yiqihi.mobile.com.commonlib.AdapterBase;

public class CarAdapter extends AdapterBase<Item> {
    private static final String TAG = "GuideAdapter";
    private Context mContext;

    public CarAdapter(Context context) {
        this.mContext = context;
    }

    private class ViewHolder {
        TextView tv_publishtime, title, tv_cartype, tv_time, tv_money, tv_des, tv_location, tv_remainday;
        SimpleDraweeView sdv_face,sdv_car;
        Button btn_add;
        FlowLayout fl_container;
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
            convertView = View.inflate(mContext, R.layout.car_item, null);
            vh.sdv_face = (SimpleDraweeView) convertView.findViewById(R.id.sdv_face);
            vh.sdv_car= (SimpleDraweeView) convertView.findViewById(R.id.sdv_car);
            vh.tv_publishtime = (TextView) convertView.findViewById(R.id.tv_publishtime);
            vh.title = (TextView) convertView.findViewById(R.id.title);
            vh.tv_cartype = (TextView) convertView.findViewById(R.id.tv_cartype);
            vh.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            vh.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            vh.tv_des = (TextView) convertView.findViewById(R.id.tv_des);
            vh.tv_location = (TextView) convertView.findViewById(R.id.tv_location);
            vh.tv_remainday = (TextView) convertView.findViewById(R.id.tv_remainday);
            vh.fl_container = (FlowLayout) convertView.findViewById(R.id.fl_container);
            vh.btn_add = (Button) convertView.findViewById(R.id.btn_add);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        try {
            Item item = getList().get(position);
            vh.sdv_face.setImageURI(Uri.parse(Constants.FACEIMAGEURL + item.userId));
            List<Picture> listPic=item.pictures;
            if(listPic==null||listPic.size()==0)
                vh.sdv_car.setVisibility(View.GONE);
            else {
                vh.sdv_car.setVisibility(View.VISIBLE);
                vh.sdv_car.setImageURI(Uri.parse(item.pictures.get(0).shotUrl));
            }
            vh.tv_publishtime.setText(item.ago);
            vh.title.setText(item.title);
            vh.tv_cartype.setText(item.car);
            vh.tv_time.setText(item.deptDate);
            vh.tv_money.setText("￥" + item.personPrice);
            vh.tv_des.setText(item.description);
            vh.tv_location.setText(item.location);
            vh.tv_remainday.setText("剩" + item.expireTime + "天");
            int catalog = item.catalog;
            String[] tags = null;
            switch (catalog) {
                case 7:
                    tags = new String[]{"AA拼车", "还差" + item.recruiteCount + "人"};
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
            if (item.isOwner) {
                vh.btn_add.setText("查看状态");
            }else if (item.isDeal && item.joinStatus != 0) {
                vh.btn_add.setText("查看状态");
            }else if(item.isFull&&item.joinStatus!=0){
                vh.btn_add.setText("查看状态");
            }else{
                vh.btn_add.setText("立即拼车");
            }
            vh.btn_add.setBackgroundResource(R.drawable.bg_car_add);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
