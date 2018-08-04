package com.quandoo.restaurant.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quandoo.restaurant.R;
import com.quandoo.restaurant.adapter.CustomerListAdapter;
import com.quandoo.restaurant.database.DataBaseHelper;
import com.quandoo.restaurant.model.Customer;
import com.quandoo.restaurant.network.RetrofitHelper;
import com.quandoo.restaurant.utils.Communicator;
import com.quandoo.restaurant.utils.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


public class CustomerListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        CustomerListAdapter.ItemClickListener {


    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.recycler_customer_list)
    RecyclerView mRecyclerViewCustomers;

    @BindView(R.id.layout_offline)
    LinearLayout mOfflineContainer;

    @BindView(R.id.progress_indicator)
    ProgressBar mProgressBar;

    private CustomerListAdapter mCustomerListAdapter;
    private Communicator fragmentCommunicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCustomerListAdapter = new CustomerListAdapter(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_customer, container, false);

        ButterKnife.bind(this, fragmentView);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.hn_orange);
        setupRecyclerView();
        loadStoriesIfNetworkConnected();
        hideLoadingViews();
        return fragmentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentCommunicator = (Communicator) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentCommunicator = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onRefresh() {
        // getNewsStories();
    }

    @OnClick(R.id.button_try_again)
    public void onTryAgainClick() {
        loadStoriesIfNetworkConnected();
    }


    private void setupRecyclerView() {
        mRecyclerViewCustomers.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewCustomers.setHasFixedSize(true);
        mRecyclerViewCustomers.setAdapter(mCustomerListAdapter);
        updateCustomerList();
    }

    private void loadStoriesIfNetworkConnected() {
        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            showHideOfflineLayout(false);
            //  getNewsStories();
        } else {
            showHideOfflineLayout(true);
        }
    }


    private void updateCustomerList() {

        int customerCount = DataBaseHelper.getInstance(getActivity()).getCustomerCount();

        if (customerCount == 0) {
            Call<ResponseBody> responseCall = RetrofitHelper.getClient().getCustomerList();
            responseCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        Gson gson = new GsonBuilder().create();
                        Customer[] customerList = gson.fromJson(response.body().string(), Customer[].class);
                        for (Customer customer : customerList) {
                            DataBaseHelper.getInstance(getActivity()).insertCustomer(customer);
                        }
                        mCustomerListAdapter.setItems(DataBaseHelper.getInstance(getActivity()).getAllCustomers());
                    } catch (Exception e) {
                        Log.d(CustomerListFragment.class.getSimpleName(), e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d(CustomerListFragment.class.getSimpleName(), t.getMessage());
                }

            });
        } else {
            mCustomerListAdapter.setItems(DataBaseHelper.getInstance(getActivity()).getAllCustomers());
        }

    }


    private void hideLoadingViews() {
        mProgressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void showHideOfflineLayout(boolean isOffline) {
        mOfflineContainer.setVisibility(isOffline ? View.VISIBLE : View.GONE);
        mRecyclerViewCustomers.setVisibility(isOffline ? View.GONE : View.VISIBLE);
        mProgressBar.setVisibility(isOffline ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onItemClickListener(View view) {
        if (fragmentCommunicator != null) {
            fragmentCommunicator.customerClicked();
        }
    }
}
