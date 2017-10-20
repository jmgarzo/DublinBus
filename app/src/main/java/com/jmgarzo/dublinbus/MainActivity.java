package com.jmgarzo.dublinbus;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.jmgarzo.dublinbus.sync.SyncTasks;
import com.jmgarzo.dublinbus.sync.UpdateDbSyncUtils;
import com.jmgarzo.dublinbus.utilities.DBUtils;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private FirebaseAnalytics mFirebaseAnalytics;

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        DBUtils.setIsAdmodActive(this,false);
        if (savedInstanceState == null) {
            if (!DBUtils.isExistDb(this)) {
                SyncTasks.copyDbFromAssets(this);

            }
            UpdateDbSyncUtils.initialize(this);

        }

        setTitle(getString(R.string.bus_stop_title));


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_access_time_white_24px));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_directions_bus_white_24px));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_favorite_white_24px));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_map_white_24px));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.pager);


        //Creating our pager adapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        viewPager.setCurrentItem(tab.getPosition());
        switch (tab.getPosition()) {
            case 0:
                this.setTitle(getString(R.string.bus_stop_title));
                break;
            case 1:
                this.setTitle(getString(R.string.route_title));
                break;
            case 2:
                this.setTitle(getString(R.string.favourite_bus_stop_title));
                break;
            case 3:
                this.setTitle(getString(R.string.bus_stop_near_title));
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
