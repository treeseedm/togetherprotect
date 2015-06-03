package com.thunder.wheel.view;

import android.content.Context;
import android.view.LayoutInflater;

public class TextWheelAdapter extends AbstractWheelTextAdapter {
    private String[] mValues;
    /**
     * Constructor
     *
     * @param context the current context
     */
    public TextWheelAdapter(Context context,String[] values) {
        super(context);
        this.mValues=values;
    }
    /**
     * Constructor
     * @param context the current context
     * @param itemResource the resource ID for a layout file containing a TextView to use when instantiating items views
     */
    public TextWheelAdapter(Context context, int itemResource,String[] values) {
        this(context, itemResource, NO_RESOURCE,values);
    }

    /**
     * Constructor
     * @param context the current context
     * @param itemResource the resource ID for a layout file containing a TextView to use when instantiating items views
     * @param itemTextResource the resource ID for a text view in the item layout
     */
    public TextWheelAdapter(Context context, int itemResource, int itemTextResource,String[] values) {
        super(context,itemResource,itemTextResource);
    }
    /**
     * Returns text for specified item
     *
     * @param index the item index
     * @return the text of specified items
     */
    @Override
    protected CharSequence getItemText(int index) {
        return mValues[index];
    }

    /**
     * Gets items count
     *
     * @return the count of wheel items
     */
    @Override
    public int getItemsCount() {
        return mValues.length;
    }
}
