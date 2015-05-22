package com.myapplication.yiqihi.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.myapplication.yiqihi.R;
import com.myapplication.yiqihi.bean.Floor;
import com.myapplication.yiqihi.bean.Guide;
import com.myapplication.yiqihi.bean.Product;
import com.myapplication.yiqihi.constant.Constants;

import org.x.mobile.Const;

import yiqihi.mobile.com.commonlib.AdapterBase;

public class SearchpageAdapter extends AdapterBase<Product> {
    private Context mContext;
    private Const mConst;
    public SearchpageAdapter(Context context) {
        this.mContext = context;
        mConst = new Const();
    }

    public class ViewHolder {
        LinearLayout leftParent;
        SimpleDraweeView leftPicture, leftFace;
        TextView leftTitle, leftCurrency, leftPrice,tv_continent,tv_country;
        ImageView leftCategory;
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
        ViewHolder viewHolder;
        if (convertView == null) {
            // 左侧布局
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.search_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.leftParent = (LinearLayout) convertView.findViewById(R.id.ll_parent);
            viewHolder.leftPicture = (SimpleDraweeView) convertView.findViewById(R.id.iv_picture);
            viewHolder.leftTitle = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.leftCategory = (ImageView) convertView.findViewById(R.id.iv_category);
            viewHolder.leftCurrency = (TextView) convertView.findViewById(R.id.tv_currency);
            viewHolder.leftPrice = (TextView) convertView.findViewById(R.id.tv_price);
            viewHolder.leftFace = (SimpleDraweeView) convertView.findViewById(R.id.iv_face);
            viewHolder.tv_continent= (TextView) convertView.findViewById(R.id.tv_continent);
            viewHolder.tv_country= (TextView) convertView.findViewById(R.id.tv_country);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        initData(viewHolder, getList().get(position));
        return convertView;
    }

    /**
     * 把左边的商品和右边的商品都更新到viewHolder上面
     *
     * @param viewHolder
     * @param leftGood
     */
    private void initData(ViewHolder viewHolder, Product leftGood) {
        try {

            JSONArray photos= (JSONArray)JSON.parse(leftGood.photos);

            String floorKey=leftGood.categories.get(0).id;
            Uri uri=Uri.parse(Constants.BIGIMAGEURL +  ((JSONObject)photos.get(0)).getString("picture"));
            viewHolder.leftPicture.setImageURI(uri);
            if (Floor.PRODUCT_KEY.equals(floorKey)) {

                viewHolder.leftCategory.setBackgroundResource(R.drawable.icon_yiriyou);
            } else if (Floor.CAR_KEY.equals(floorKey)) {
                viewHolder.leftCategory.setImageResource(R.drawable.icon_zuche);
            } else if (Floor.GUIDE_KEY.equals(floorKey)) {
                viewHolder.leftCategory.setImageResource(R.drawable.icon_daoyou);
            } else if (Floor.GROUP_KEY.equals(floorKey)) {
                viewHolder.leftCategory.setImageResource(R.drawable.icon_pintuan);
            }
            viewHolder.tv_country.setText(leftGood.belongCity);
            viewHolder.tv_continent.setText(leftGood.country);
            viewHolder.leftTitle.setText(leftGood.title);
            viewHolder.leftCurrency.setText(mConst.currencyNameBy(leftGood.currency));
            viewHolder.leftPrice.setText("" + leftGood.price);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
