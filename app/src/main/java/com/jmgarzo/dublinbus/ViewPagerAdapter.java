package com.jmgarzo.dublinbus;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by jmgarzo on 18/08/2017.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    int tabCount;

    public ViewPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount= tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                BusStopFragment busStopFragment = new BusStopFragment();
                return busStopFragment;
            case 1:
                RouteFragment routeFragment = new RouteFragment();
                return routeFragment;
            case 2:
                FavouriteBusStopFragment favouriteBusStopFragment = new FavouriteBusStopFragment();
                return favouriteBusStopFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
