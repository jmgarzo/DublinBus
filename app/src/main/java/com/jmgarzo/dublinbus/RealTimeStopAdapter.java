package com.jmgarzo.dublinbus;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jmgarzo.dublinbus.utilities.DBUtils;

/**
 * Created by jmgarzo on 14/08/2017.
 */

public class RealTimeStopAdapter extends RecyclerView.Adapter<RealTimeStopAdapter.RealTimeStopAdapterViewHolder> {

    private Cursor mCursor;

    private Context mContext;

    public RealTimeStopAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RealTimeStopAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item_real_time_stop, parent, false);
        view.setFocusable(true);
        return new RealTimeStopAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RealTimeStopAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        if (mCursor != null && mCursor.moveToPosition(position)) {
            holder.mRouteName.setText(mCursor.getString(DBUtils.COL_REAL_TIME_STOP_ROUTE));
            String minutes = mCursor.getString(DBUtils.COL_REAL_TIME_STOP_DUE_TIME);
            holder.mDueTime.setText(minutes);
            if(minutes.equalsIgnoreCase("Due")){
                holder.mMinutesLabel.setVisibility(View.GONE);
            }
            holder.mOrigin.setText(mCursor.getString(DBUtils.COL_REAL_TIME_STOP_ORIGIN));
            holder.mDestination.setText(mCursor.getString(DBUtils.COL_REAL_TIME_STOP_DESTINATION));
        }
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }


    public class RealTimeStopAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mRouteName;
        public final TextView mDueTime;
        public final TextView mMinutesLabel;
        public final TextView mOrigin;
        public final TextView mDestination;

        public RealTimeStopAdapterViewHolder(View view) {
            super(view);
            mRouteName = view.findViewById(R.id.list_item_real_time_route);
            mDueTime = view.findViewById(R.id.list_item_real_time_due_time);
            mMinutesLabel= view.findViewById(R.id.list_item_real_time_minutes_label);
            mOrigin = view.findViewById(R.id.list_item_real_time_origin);
            mDestination =  view.findViewById(R.id.list_item_real_time_destination);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {


        }


    }
}
