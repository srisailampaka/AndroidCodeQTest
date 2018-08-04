package com.quandoo.restaurant.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quandoo.restaurant.R;
import com.quandoo.restaurant.adapter.TableListAdapter;
import com.quandoo.restaurant.database.DataBaseHelper;
import com.quandoo.restaurant.model.Customer;
import com.quandoo.restaurant.network.RetrofitHelper;
import com.quandoo.restaurant.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


public class TableListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, TableListAdapter.ItemClickListener {


    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.recycler_table_list)
    RecyclerView mRecyclerViewTable;

    @BindView(R.id.layout_offline)
    LinearLayout mOfflineContainer;

    @BindView(R.id.progress_indicator)
    ProgressBar mProgressBar;

    private TableListAdapter mTableListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTableListAdapter = new TableListAdapter(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_table, container, false);

        ButterKnife.bind(this, fragmentView);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.hn_orange);
        setupRecyclerView();
        loadStoriesIfNetworkConnected();
        hideLoadingViews();
        return fragmentView;
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
        mRecyclerViewTable.setLayoutManager(new GridLayoutManager(getActivity(), gridSpanCount()));
        mRecyclerViewTable.setHasFixedSize(true);
        mRecyclerViewTable.setAdapter(mTableListAdapter);
        updateTableList();
    }

    private void loadStoriesIfNetworkConnected() {
        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            showHideOfflineLayout(false);
            //  getNewsStories();
        } else {
            showHideOfflineLayout(true);
        }
    }

    private void updateTableList() {
        int reservationCount = DataBaseHelper.getInstance(getActivity()).getReservationCount();

        if (reservationCount == 0) {
            Call<ResponseBody> responseCall = RetrofitHelper.getClient().getTableList();
            responseCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        Gson gson = new GsonBuilder().create();
                        String responseString = response.body().string();
                        Boolean[] tableList = gson.fromJson(responseString, Boolean[].class);
                        for (Boolean isTableReserved : tableList) {
                            DataBaseHelper.getInstance(getActivity()).insertTableReservation(isTableReserved);
                        }
                        mTableListAdapter.setItems(DataBaseHelper.getInstance(getActivity()).getTableReservations());
                    } catch (Exception e) {
                        Log.d(CustomerListFragment.class.getSimpleName(), e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d(CustomerListFragment.class.getSimpleName(), t.getMessage());
                }

            });
        }
        else {
            mTableListAdapter.setItems(DataBaseHelper.getInstance(getActivity()).getTableReservations());
        }
    }


    private void hideLoadingViews() {
        mProgressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void showHideOfflineLayout(boolean isOffline) {
        mOfflineContainer.setVisibility(isOffline ? View.VISIBLE : View.GONE);
        mRecyclerViewTable.setVisibility(isOffline ? View.GONE : View.VISIBLE);
        mProgressBar.setVisibility(isOffline ? View.GONE : View.VISIBLE);
    }

    private int gridSpanCount() {
        int value = getActivity().getResources().getConfiguration().orientation;
        if (value == Configuration.ORIENTATION_PORTRAIT) {
            return 2;
        } else if (value == Configuration.ORIENTATION_LANDSCAPE) {
            return 3;
        }
        return 0;
    }

    @Override
    public void onItemClickListener(View view) {
        int itemPosition = mRecyclerViewTable.getChildLayoutPosition(view);
        Toast.makeText(getActivity(), itemPosition + "", Toast.LENGTH_LONG).show();
    }
}
