package com.jmgarzo.dublinbus;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.jmgarzo.dublinbus.objects.BusStop;
import com.jmgarzo.dublinbus.utilities.DBUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmgarzo on 03/08/17.
 */

public class BusStopAdapter extends RecyclerView.Adapter<BusStopAdapter.BusStopAdapterViewHolder>
        implements Filterable {

    private Cursor mCursor;

    private Context mContext;
    private final BusStopAdapterOnClickHandler mClickHandler;
    ValueFilter valueFilter;

    List<String> mStringFilterList;


    public static interface BusStopAdapterOnClickHandler {
        void onClick(BusStop busStop);
    }

    public BusStopAdapter(Context context, BusStopAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
        mStringFilterList = cursorToArrayList(mCursor);


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
            holder.mBusStopName.setText(mCursor.getString(DBUtils.COL_BUS_STOP_NUMBER));
            holder.mBusStopShortName.setText(mCursor.getString(DBUtils.COL_BUS_STOP_SHORTNAME));
            holder.mBusStopShortNameLocalized.setText(mCursor.getString(DBUtils.COL_BUS_STOP_SHORT_NAME_LOCALIZED));

        }
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;

        mStringFilterList = cursorToArrayList(mCursor);
        notifyDataSetChanged();
    }




    public class BusStopAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mBusStopName;
        public final TextView mBusStopShortName;
        public final TextView mBusStopShortNameLocalized;

        public BusStopAdapterViewHolder(View view) {
            super(view);
            mBusStopName = view.findViewById(R.id.list_item_bus_stop_name);
            mBusStopShortName = view.findViewById(R.id.list_item_bus_stop_short_name);
            mBusStopShortNameLocalized = view.findViewById(R.id.list_item_bus_stop_short_name_localized);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            BusStop busStop = new BusStop(mCursor, adapterPosition);
            mClickHandler.onClick(busStop);


        }


    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                List<String> filterList = new ArrayList<>();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if ((mStringFilterList.get(i).toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        filterList.add(mStringFilterList.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mStringFilterList.size();
                results.values = mStringFilterList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            // mData = (List<String>) results.values;
            notifyDataSetChanged();
        }

    }

    private ArrayList<String> cursorToArrayList(Cursor cursor) {
        ArrayList<String> stringFilterList = null;
        if (cursor != null && cursor.moveToFirst()) {
            stringFilterList = new ArrayList<>();

            do {
                cursor.getString(DBUtils.COL_BUS_STOP_NUMBER);
            } while (cursor.moveToNext());
        }
        return stringFilterList;
    }
}
