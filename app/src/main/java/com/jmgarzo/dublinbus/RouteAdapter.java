package com.jmgarzo.dublinbus;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jmgarzo.dublinbus.model.Route;
import com.jmgarzo.dublinbus.utilities.DBUtils;

/**
 * Created by jmgarzo on 31/07/17.
 */

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteAdapterViewHolder> {

    private Cursor mCursor;

    private Context mContext;

    private final RouteAdapterOnClickHandler mClickHandler;

    public static interface RouteAdapterOnClickHandler {
        void onClick(Route route);
    }


    public RouteAdapter(@NonNull Context context, RouteAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public RouteAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.list_item_route, parent, false);
        view.setFocusable(true);
        return new RouteAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RouteAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        String origin = mContext.getString(R.string.list_label_from) +" "+ mCursor.getString(DBUtils.COL_ROUTE_ORIGIN);
        String destination = mContext.getString(R.string.list_label_to) +" "+mCursor.getString(DBUtils.COL_ROUTE_DESTINATION);
        String originLocalized = mCursor.getString(DBUtils.COL_ROUTE_ORIGIN_LOCALIZED);
//        if(originLocalized.isEmpty()) originLocalized=origin;
        String destinationLocalized = mCursor.getString(DBUtils.COL_ROUTE_DESTINATION);
//        if(destinationLocalized.isEmpty()) destinationLocalized = destination;

        if (mCursor != null && mCursor.moveToPosition(position)) {
            Glide.with(mContext).load(R.drawable.blue_circle).
                    apply(RequestOptions.circleCropTransform()).into(holder.mImageViewNameBackground);
            holder.mRouteName.setText(mCursor.getString(DBUtils.COL_ROUTE_NAME));
            holder.mRouteOrigin.setText(origin);
            holder.mRouteDestination.setText(destination);
//            holder.mRouteOriginLocalized.setText(originLocalized);
//            holder.mRouteDestinationLocalized.setText(destinationLocalized);

        }
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }





    public class RouteAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mImageViewNameBackground;
        public final TextView mRouteName;
        public final TextView mRouteOrigin;
//        public final TextView mRouteOriginLocalized;
        public final TextView mRouteDestination;
//        public final TextView mRouteDestinationLocalized;



        public RouteAdapterViewHolder(View view) {
            super(view);
            mImageViewNameBackground = view.findViewById(R.id.list_item_route_image_name_background);
            mRouteName = view.findViewById(R.id.list_item_route_name);
            mRouteOrigin = (TextView) view.findViewById(R.id.list_item_route_origin);
            mRouteDestination = (TextView) view.findViewById(R.id.list_item_route_destination);
//            mRouteOriginLocalized = view.findViewById(R.id.list_item_route_origin_localized);
//            mRouteDestinationLocalized = view.findViewById(R.id.list_item_route_destination_localized);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Route route  = new Route(mCursor,adapterPosition);
            mClickHandler.onClick(route);


        }


    }





}
