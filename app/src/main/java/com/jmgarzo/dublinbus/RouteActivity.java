package com.jmgarzo.dublinbus;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jmgarzo.dublinbus.objects.Route;

public class RouteActivity extends AppCompatActivity implements RouteActivityFragment.Callback {

    public static final String ROUTE_INTENT_TAG="route_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    @Override
    public void OnItemSelected(Route route) {
        Intent intent = new Intent(this,RouteDetailActivity.class);
        intent.putExtra(ROUTE_INTENT_TAG,route);
        startActivity(intent);

    }
}
