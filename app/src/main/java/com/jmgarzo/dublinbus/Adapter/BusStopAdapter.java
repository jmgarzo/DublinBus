package com.jmgarzo.dublinbus.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jmgarzo.dublinbus.R;
import com.jmgarzo.dublinbus.model.BusStop;
import com.jmgarzo.dublinbus.utilities.DBUtils;

/**
 * Created by jmgarzo on 03/08/17.
 */

public class BusStopAdapter extends RecyclerView.Adapter<BusStopAdapter.BusStopAdapterViewHolder>
         {

    private Cursor mCursor;

    private Context mContext;
    private final BusStopAdapterOnClickHandler mClickHandler;



    public static interface BusStopAdapterOnClickHandler {
        void onClick(BusStop busStop);
    }

    public BusStopAdapter(Context context, BusStopAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public BusStopAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.list_item_bus_stop, parent, false);
        view.setFocusable(true);
        return new BusStopAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BusStopAdapterViewHolder holder, int position) {

        mCursor.moveToPosition(position);

        if (mCursor != null && mCursor.moveToPosition(position)) {
//            Glide.with(mContext).load(R.drawable.yellow_a700).
//                    apply(RequestOptions.circleCropTransform()).into(holder.mBusStopImageView);

            holder.mBusStopName.setText(mCursor.getString(DBUtils.COL_BUS_STOP_NUMBER));
            holder.mBusStopShortName.setText(mCursor.getString(DBUtils.COL_BUS_STOP_SHORTNAME));

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




    public class BusStopAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        public final ImageView mBusStopImageView;
        public final TextView mBusStopName;
        public final TextView mBusStopShortName;

        public BusStopAdapterViewHolder(View view) {
            super(view);
            mBusStopName = view.findViewById(R.id.list_item_bus_stop_name);
            mBusStopShortName = view.findViewById(R.id.list_item_bus_stop_short_name);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            BusStop busStop = new BusStop(mCursor, adapterPosition);
            mClickHandler.onClick(busStop);


        }


    }
}
