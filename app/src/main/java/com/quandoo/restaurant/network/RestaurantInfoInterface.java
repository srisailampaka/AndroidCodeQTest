package com.quandoo.restaurant.network;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;

public interface RestaurantInfoInterface {

    String ENDPOINT_URL = "https://s3-eu-west-1.amazonaws.com/quandoo-assessment/";

    /**
     * Return Customers list .
     */
    @GET("customer-list.json")
    Call<ResponseBody> getCustomerList();

    /**
     * Return table list with availability status .
     */
    @GET("table-map.json")
    Call<ResponseBody> getTableList();
}
