package com.jmgarzo.dublinbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        if(savedInstanceState == null ) {
            AboutFragment fragment = new AboutFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.about_activity, fragment)
                    .commit();
        }
    }
}
