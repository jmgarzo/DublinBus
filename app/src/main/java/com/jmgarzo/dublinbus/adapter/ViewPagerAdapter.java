package com.jmgarzo.dublinbus.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.jmgarzo.dublinbus.BusStopFragment;
import com.jmgarzo.dublinbus.FavouriteBusStopFragment;
import com.jmgarzo.dublinbus.NearBusStopFragment;
import com.jmgarzo.dublinbus.RouteActivityFragment;

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
                RouteActivityFragment routeFragment = new RouteActivityFragment();
                return routeFragment;
            case 2:
                FavouriteBusStopFragment favouriteBusStopFragment = new FavouriteBusStopFragment();
                return favouriteBusStopFragment;
            case 3:
                NearBusStopFragment nearBusStop = new NearBusStopFragment();
                return nearBusStop;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
