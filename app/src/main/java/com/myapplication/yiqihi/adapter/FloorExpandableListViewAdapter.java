package com.myapplication.yiqihi.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.myapplication.yiqihi.R;
import com.myapplication.yiqihi.SearchListActivity;
import com.myapplication.yiqihi.bean.Floor;
import com.myapplication.yiqihi.bean.Guide;
import com.myapplication.yiqihi.constant.Constants;


import org.x.mobile.Const;

import java.util.ArrayList;

public class FloorExpandableListViewAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "adapter";
    private Context mContext;
    private ArrayList<Floor> mAllFloorsGood = new ArrayList<Floor>();
    private Const mConst;

    public FloorExpandableListViewAdapter(Context mContext, ArrayList<Floor> mAllFloorsGood) {
        super();
        this.mContext = mContext;
        this.mAllFloorsGood = mAllFloorsGood;
        mConst = new Const();
    }

    @Override
    public int getGroupCount() {
        if (mAllFloorsGood != null) {
            return mAllFloorsGood.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (mAllFloorsGood.get(groupPosition).goodsList == null) {
            return 0;
        }
        return (int) mAllFloorsGood.get(groupPosition).goodsList.size() / 2
                + mAllFloorsGood.get(groupPosition).goodsList.size() % 2;
    }

    @Override
    public Object getGroup(int groupPosition) {

        return mAllFloorsGood.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mAllFloorsGood.get(groupPosition).goodsList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {

        return false;
    }

    public void refresh(ArrayList<Floor> allFloorsGood) {
        mAllFloorsGood.clear();
        mAllFloorsGood.addAll(allFloorsGood);
        notifyDataSetChanged();
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.floor_header, parent, false);
        TextView textView = (TextView) view.findViewById(R.id.tv_title);
        textView.setText(mAllFloorsGood.get(groupPosition).floorName);
        view.setOnClickListener(new OnGroupItemClick(groupPosition));
        return view;
    }

    private void initConvertView(View convertView, ViewHolder viewHolder, ViewGroup parent, LinearLayout layout) {


        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setFocusable(false);
        layout.setClickable(false);
        // 左侧布局
        LinearLayout leftView = (LinearLayout) LayoutInflater.from(mContext).inflate(
                R.layout.guide_itme, parent, false);
        // 右侧布局
        LinearLayout rightView = (LinearLayout) LayoutInflater.from(mContext).inflate(
                R.layout.guide_itme, parent, false);

        android.widget.LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 1);

        viewHolder.leftParent = (LinearLayout) leftView.findViewById(R.id.ll_parent);
        viewHolder.leftPicture = (SimpleDraweeView) leftView.findViewById(R.id.iv_picture);
        viewHolder.leftTitle = (TextView) leftView.findViewById(R.id.tv_title);
        viewHolder.leftCategory = (ImageView) leftView.findViewById(R.id.iv_category);
        viewHolder.leftCurrency = (TextView) leftView.findViewById(R.id.tv_currency);
        viewHolder.leftPrice = (TextView) leftView.findViewById(R.id.tv_price);
        viewHolder.leftFace = (SimpleDraweeView) leftView.findViewById(R.id.iv_face);
        viewHolder.leftPriceContainer = (LinearLayout) leftView.findViewById(R.id.ll_priceContainer);
        viewHolder.leftLocationConteiner = (LinearLayout) leftView.findViewById(R.id.ll_locationConteiner);
        viewHolder.leftPerPrice = (TextView) leftView.findViewById(R.id.tv_perprice);
        viewHolder.leftCountry= (TextView) leftView.findViewById(R.id.tv_country);
        leftView.setLayoutParams(params);
        //right
        viewHolder.rightParent = (LinearLayout) rightView.findViewById(R.id.ll_parent);
        viewHolder.rightPicture = (SimpleDraweeView) rightView.findViewById(R.id.iv_picture);
        viewHolder.rightTitle = (TextView) rightView.findViewById(R.id.tv_title);
        viewHolder.rightCategory = (ImageView) rightView.findViewById(R.id.iv_category);
        viewHolder.rightCurrency = (TextView) rightView.findViewById(R.id.tv_currency);
        viewHolder.rightPrice = (TextView) rightView.findViewById(R.id.tv_price);
        viewHolder.rightFace = (SimpleDraweeView) rightView.findViewById(R.id.iv_face);
        viewHolder.rightPriceContainer = (LinearLayout) rightView.findViewById(R.id.ll_priceContainer);
        viewHolder.rightLocationConteiner = (LinearLayout) rightView.findViewById(R.id.ll_locationConteiner);
        viewHolder.rightPerPrice = (TextView) rightView.findViewById(R.id.tv_perprice);
        viewHolder.rightCountry= (TextView) rightView.findViewById(R.id.tv_country);
        rightView.setLayoutParams(params);
        layout.addView(leftView);
        layout.addView(rightView);


    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {
        ViewHolder viewHolder = null;
        Floor floor = mAllFloorsGood.get(groupPosition);

        if (convertView == null) {
            Log.e(TAG, "====" + floor.floorKey);
            viewHolder = new ViewHolder();
            LinearLayout layout = new LinearLayout(mContext);
            initConvertView(convertView, viewHolder, parent, layout);
            convertView = layout;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        int leftItemPosition = childPosition * 2;// 左边商品是偶数，0,2,4.......
        Guide leftGood = mAllFloorsGood.get(groupPosition).goodsList.get(leftItemPosition);
        if (leftGood != null) {
            // 添加点击事件
            viewHolder.leftParent.setOnClickListener(new OnItemClickListener(groupPosition, leftItemPosition,
                    leftGood));
        }
        Guide rightGood = null;
        if (childPosition * 2 + 1 < mAllFloorsGood.get(groupPosition).goodsList.size()) {
            int rightItemPosition = childPosition * 2 + 1;// 右边是奇数，1,3,5,7......
            rightGood = mAllFloorsGood.get(groupPosition).goodsList.get(rightItemPosition);
            if (rightGood != null) {
                // 添加点击事件
                viewHolder.rightParent.setOnClickListener(new OnItemClickListener(groupPosition, rightItemPosition,
                        rightGood));
            }
        }
        bindGuide(viewHolder, leftGood, rightGood, floor.floorKey);
//        if (Floor.GUIDE_KEY.equals(floor.floorKey)) {
//            bindGuide(viewHolder, leftGood, rightGood);
//        } else if (Floor.CAR_KEY.equals(floor.floorKey)) {
//            bindCar(viewHolder, leftGood, rightGood);
//        } else if (Floor.PRODUCT_KEY.equals(floor.floorKey)) {
//            bindGuide(viewHolder, leftGood, rightGood);
//        }
        return convertView;

    }

    /**
     * 把左边的商品和右边的商品都更新到viewHolder上面
     *
     * @param viewHolder
     * @param leftGood
     * @param rightGood
     */
    private void bindGuide(ViewHolder viewHolder, Guide leftGood, Guide rightGood, String floorKey) {
        try {

            if (leftGood != null) {
                viewHolder.leftParent.setVisibility(View.VISIBLE);
                if (Floor.PRODUCT_KEY.equals(floorKey)) {
                    viewHolder.leftLocationConteiner.setVisibility(View.GONE);
                    viewHolder.leftPriceContainer.setVisibility(View.VISIBLE);
                    viewHolder.leftFace.setVisibility(View.VISIBLE);

                    viewHolder.leftPicture.setImageURI(Uri.parse(Constants.SHOTIMAGEURL + leftGood.shot));
                    viewHolder.leftCategory.setBackgroundResource(R.drawable.icon_yiriyou);
                } else if(Floor.CAR_KEY.equals(floorKey)) {
                    viewHolder.leftLocationConteiner.setVisibility(View.VISIBLE);
                    viewHolder.leftPriceContainer.setVisibility(View.GONE);
                    viewHolder.leftFace.setVisibility(View.GONE);
                    viewHolder.leftCountry.setText(leftGood.country);
                    viewHolder.leftPerPrice.setText(mConst.currencyNameBy(leftGood.currency)+leftGood.price);
                    viewHolder.leftPicture.setImageURI(Uri.parse(Constants.SHOTIMAGEURL + leftGood.photos.get(0).shot));
                    viewHolder.leftCategory.setImageResource(R.drawable.icon_zuche);
                }else if(Floor.GUIDE_KEY.equals(floorKey)){
                    viewHolder.leftLocationConteiner.setVisibility(View.GONE);
                    viewHolder.leftPriceContainer.setVisibility(View.VISIBLE);
                    viewHolder.leftFace.setVisibility(View.VISIBLE);

                    viewHolder.leftPicture.setImageURI(Uri.parse(Constants.SHOTIMAGEURL + leftGood.photos.get(0).shot));
                    viewHolder.leftCategory.setImageResource(R.drawable.icon_daoyou);
                }else if(Floor.GROUP_KEY.equals(floorKey)){
                    viewHolder.leftLocationConteiner.setVisibility(View.VISIBLE);
                    viewHolder.leftPriceContainer.setVisibility(View.GONE);
                    viewHolder.leftFace.setVisibility(View.GONE);

                    viewHolder.leftPicture.setImageURI(Uri.parse(Constants.SHOTIMAGEURL + leftGood.photos.get(0).shot));
                    viewHolder.leftCategory.setImageResource(R.drawable.icon_pintuan);
                }
                viewHolder.leftTitle.setText(leftGood.title);
                viewHolder.leftFace.setImageURI(Uri.parse(Constants.FACEIMAGEURL + leftGood.userId));

                viewHolder.leftCurrency.setText(mConst.currencyNameBy(leftGood.currency));
                viewHolder.leftPrice.setText("" + leftGood.price);
            } else {
                viewHolder.leftParent.setVisibility(View.GONE);
            }
            if (rightGood != null) {
                viewHolder.rightParent.setVisibility(View.VISIBLE);
                if (Floor.PRODUCT_KEY.equals(floorKey)) {
                    viewHolder.rightLocationConteiner.setVisibility(View.GONE);
                    viewHolder.rightPriceContainer.setVisibility(View.VISIBLE);
                    viewHolder.rightFace.setVisibility(View.VISIBLE);
                    viewHolder.rightPicture.setImageURI(Uri.parse(Constants.SHOTIMAGEURL + rightGood.shot));
                    viewHolder.rightCategory.setImageResource(R.drawable.icon_yiriyou);
                } else if(Floor.CAR_KEY.equals(floorKey)) {
                    viewHolder.rightLocationConteiner.setVisibility(View.VISIBLE);
                    viewHolder.rightPriceContainer.setVisibility(View.GONE);
                    viewHolder.rightFace.setVisibility(View.GONE);
                    viewHolder.rightPerPrice.setText(mConst.currencyNameBy(rightGood.currency)+rightGood.price);
                    viewHolder.rightPicture.setImageURI(Uri.parse(Constants.SHOTIMAGEURL + rightGood.photos.get(0).shot));
                    viewHolder.rightCategory.setImageResource(R.drawable.icon_zuche);
                    viewHolder.rightPicture.setImageURI(Uri.parse(Constants.SHOTIMAGEURL + rightGood.photos.get(0).shot));
                }else if(Floor.GUIDE_KEY.equals(floorKey)){
                    viewHolder.rightLocationConteiner.setVisibility(View.GONE);
                    viewHolder.rightPriceContainer.setVisibility(View.VISIBLE);
                    viewHolder.rightFace.setVisibility(View.VISIBLE);



                    viewHolder.rightPicture.setImageURI(Uri.parse(Constants.SHOTIMAGEURL + rightGood.photos.get(0).shot));
                    viewHolder.rightCategory.setImageResource(R.drawable.icon_daoyou);
                }else if(Floor.GROUP_KEY.equals(floorKey)){
                    viewHolder.rightLocationConteiner.setVisibility(View.VISIBLE);
                    viewHolder.rightPriceContainer.setVisibility(View.GONE);
                    viewHolder.rightFace.setVisibility(View.GONE);

                    viewHolder.rightPicture.setImageURI(Uri.parse(Constants.SHOTIMAGEURL + rightGood.photos.get(0).shot));
                    viewHolder.rightCategory.setImageResource(R.drawable.icon_pintuan);
                }
                viewHolder.rightTitle.setText(rightGood.title);
                viewHolder.rightFace.setImageURI(Uri.parse(Constants.FACEIMAGEURL + rightGood.userId));
                viewHolder.rightCurrency.setText(mConst.currencyNameBy(rightGood.currency));
                viewHolder.rightPrice.setText("" + rightGood.price);
            } else {
                viewHolder.rightParent.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 把左边的商品和右边的商品都更新到viewHolder上面
     *
     * @param viewHolder
     * @param leftGood
     * @param rightGood
     */
    private void bindProduct(ViewHolder viewHolder, Guide leftGood, Guide rightGood) {
        try {

            if (leftGood != null) {
                viewHolder.leftParent.setVisibility(View.VISIBLE);
                viewHolder.leftPicture.setImageURI(Uri.parse(Constants.BIGIMAGEURL + leftGood.picture));
                viewHolder.leftTitle.setText(leftGood.title);
            } else {
                viewHolder.leftParent.setVisibility(View.GONE);
            }
            if (rightGood != null) {
                viewHolder.rightParent.setVisibility(View.VISIBLE);
                viewHolder.rightPicture.setImageURI(Uri.parse(Constants.BIGIMAGEURL + rightGood.picture));
                viewHolder.rightTitle.setText(rightGood.title);
            } else {
                viewHolder.rightParent.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 把左边的商品和右边的商品都更新到viewHolder上面
     *
     * @param viewHolder
     * @param leftGood
     * @param rightGood
     */
    private void bindCar(ViewHolder viewHolder, Guide leftGood, Guide rightGood) {
        try {

            if (leftGood != null) {
                viewHolder.leftParent.setVisibility(View.VISIBLE);
                viewHolder.leftPicture.setImageURI(Uri.parse(Constants.SHOTIMAGEURL + leftGood.photos.get(0).shot));
                viewHolder.leftTitle.setText(leftGood.title);
            } else {
                viewHolder.leftParent.setVisibility(View.GONE);
            }
            if (rightGood != null) {
                viewHolder.rightParent.setVisibility(View.VISIBLE);
                viewHolder.rightPicture.setImageURI(Uri.parse(Constants.SHOTIMAGEURL + rightGood.photos.get(0).shot));
                viewHolder.rightTitle.setText(rightGood.title);
            } else {
                viewHolder.rightParent.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 点击事件
     *
     * @author ZhaoFangyi
     *         <p/>
     *         Create at 2014-7-12 下午4:15:52
     */
    public class OnItemClickListener implements View.OnClickListener {

        private Guide mGoods;
        private int mGroupPosition;
        private int mItemPosition;

        public OnItemClickListener(int groupPosition, int itemPosition, Guide goods) {
            mGoods = goods;
            mGroupPosition = groupPosition;
            mItemPosition = itemPosition;
        }

        @Override
        public void onClick(View v) {
            Floor floor = mAllFloorsGood.get(mGroupPosition);
            if (floor != null) {
//                /**埋码需求**/
//                GoodsShelfMeasures.onAppSpecialFloorClick(mContext, floor.floorName, mItemPosition + 1);
//                // 跳转详情页面
//                ProductDetailMainActivity.jump(mContext, -1, "", mContext.getString(R.string.appMeas_special), mContext.getString(R.string.appMeas_special) + ":" + floor.floorName, mGoods.goodsNo, mGoods.skuID);
            }
        }

    }

    public class OnGroupItemClick implements View.OnClickListener {
        private int mGroupPosition;

        public OnGroupItemClick(int groupPosition) {
            this.mGroupPosition = groupPosition;
        }

        @Override
        public void onClick(View v) {
            Floor floor = mAllFloorsGood.get(mGroupPosition);
            if (floor != null) {
                Intent intent = new Intent(mContext, SearchListActivity.class);
                intent.putExtra(SearchListActivity.CATEGORY_KEY, floor.floorKey);
                mContext.startActivity(intent);
            }
        }
    }

    public class ViewHolder {
        LinearLayout leftParent,leftPriceContainer,leftLocationConteiner;
        SimpleDraweeView leftPicture, leftFace;
        TextView leftTitle, leftCurrency, leftPrice,leftCountry,leftPerPrice;
        ImageView leftCategory;


        LinearLayout rightParent,rightPriceContainer,rightLocationConteiner;
        SimpleDraweeView rightPicture, rightFace;
        TextView rightTitle, rightCurrency, rightPrice,rightCountry,rightPerPrice;
        ImageView rightCategory;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
