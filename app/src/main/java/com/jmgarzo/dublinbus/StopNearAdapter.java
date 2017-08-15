package com.jmgarzo.dublinbus;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jmgarzo.dublinbus.objects.BusStop;
import com.jmgarzo.dublinbus.utilities.DBUtils;

/**
 * Created by jmgarzo on 15/08/2017.
 */

public class StopNearAdapter extends RecyclerView.Adapter<StopNearAdapter.StopNearAdapterViewHolder> {

    private Cursor mCursor;
    private Context mContext;

    private final StopNearAdapterOnClickHandler mClickHandler;

    public static interface StopNearAdapterOnClickHandler {
        void onClick(BusStop busStop);
    }

    public StopNearAdapter(Context context, StopNearAdapterOnClickHandler clickHandler){
        mContext = context;
        mClickHandler = clickHandler;

    }


    @Override
    public StopNearAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.list_item_stops_near, parent, false);
        view.setFocusable(true);
        return new StopNearAdapterViewHolder(view);    }

    @Override
    public void onBindViewHolder(StopNearAdapterViewHolder holder, int position) {

        mCursor.moveToPosition(position);
        if (mCursor != null && mCursor.moveToPosition(position)) {
            holder.mStopNumber.setText(mCursor.getString(DBUtils.COL_BUS_STOP_NUMBER));
            holder.mStopName.setText(mCursor.getString(DBUtils.COL_BUS_STOP_SHORT_NAME_LOCALIZED));
            holder.mStopNameLocalized.setText(mCursor.getString(DBUtils.COL_BUS_STOP_SHORT_NAME_LOCALIZED));

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

    public class StopNearAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mStopNumber;
        public final TextView mStopName;
        public final TextView mStopNameLocalized;

        public StopNearAdapterViewHolder(View view) {
            super(view);
            mStopNumber =  view.findViewById(R.id.list_item_stop_near_number);
            mStopName =  view.findViewById(R.id.list_item_stop_near_short_name);
            mStopNameLocalized =  view.findViewById(R.id.list_item_stop_near_name_localized);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
//            Route route  = new Route(mCursor,adapterPosition);
//            mClickHandler.onClick(route);


        }


    }
}
