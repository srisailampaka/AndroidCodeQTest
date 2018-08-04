package com.quandoo.restaurant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quandoo.restaurant.R;
import com.quandoo.restaurant.model.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.ViewHolder> {
    private List<Customer> mCustomerList;
    private Context mContext;
    private  ItemClickListener mListener;

    public CustomerListAdapter(Context context, ItemClickListener listener) {
        mContext = context;
        mCustomerList = new ArrayList<>();
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.list_item_customer, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String customerName = mCustomerList.get(position).getCustomerFirstName() + " " + mCustomerList.get(position).getCustomerLastName();
        holder.customerName.setText(customerName);
    }

    @Override
    public int getItemCount() {
        return mCustomerList.size();
    }

    public void setItems(List<Customer> posts) {
        mCustomerList = posts;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView customerName;

        ViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClickListener(v);
                }
            });
            customerName = itemView.findViewById(R.id.text_view_customer_name);
        }
    }
    public interface ItemClickListener
    {
        void onItemClickListener(View view);
    }
}