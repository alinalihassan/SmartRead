package com.teched.smartread;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class OverviewPagerAdapter extends PagerAdapter {

    public Object instantiateItem(ViewGroup collection, int position) {

        return collection.getChildAt(position);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);
    }
}