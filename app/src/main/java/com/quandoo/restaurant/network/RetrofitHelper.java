package com.quandoo.restaurant.network;




import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {

    public static RestaurantInfoInterface getClient() {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(RestaurantInfoInterface.ENDPOINT_URL);
        builder.addConverterFactory(GsonConverterFactory.create());
        return builder.build().create(RestaurantInfoInterface.class);
    }

}
