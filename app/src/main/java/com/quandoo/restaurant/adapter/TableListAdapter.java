package com.quandoo.restaurant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.quandoo.restaurant.R;
import com.quandoo.restaurant.model.ReservationTable;

import java.util.ArrayList;

public class TableListAdapter extends RecyclerView.Adapter<TableListAdapter.ViewHolder> {

    private ArrayList<ReservationTable> mTableList;
    private Context mContext;
    private ItemClickListener mListener;

    public TableListAdapter(Context context, ItemClickListener listener) {
        mContext = context;
        mTableList = new ArrayList<>();
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.list_item_table, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mTableList.get(position).isReserved()) {
            holder.tableAvailability.setImageResource(R.drawable.available);
        } else {
            holder.tableAvailability.setImageResource(R.drawable.reserved);
        }
    }

    @Override
    public int getItemCount() {
        return mTableList.size();
    }

    public void setItems(ArrayList<ReservationTable> tableList) {
        mTableList = tableList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView tableAvailability;

        ViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClickListener(v);
                }
            });
            tableAvailability = itemView.findViewById(R.id.img_view_table);
        }
    }

    public interface ItemClickListener {
        void onItemClickListener(View view);
    }
}