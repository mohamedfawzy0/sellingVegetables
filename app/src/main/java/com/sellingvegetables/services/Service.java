package com.sellingvegetables.services;

import com.sellingvegetables.model.DepartmentDataModel;
import com.sellingvegetables.model.OrderDataModel;
import com.sellingvegetables.model.ProductDataModel;
import com.sellingvegetables.model.SingleProductDataModel;
import com.sellingvegetables.model.UserModel;

import java.util.List;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Service {
    @FormUrlEncoded
    @POST("api/auth/login")
    Single<Response<UserModel>> login(@Field("user_name") String user_name,
                                      @Field("lang") String lang,
                                      @Field("password") String password);

    @GET("api/home/categories")
    Single<Response<DepartmentDataModel>> getDepartments(@Header("Authorization") String authorization);

    @GET("api/home/products")
    Single<Response<ProductDataModel>> getProducts(@Header("Authorization") String authorization);

    @Multipart
    @POST("api/home/addProduct")
    Single<Response<SingleProductDataModel>> addPeoduct(@Header("Authorization") String authorization,
                                                        @Part("title_ar") RequestBody title_ar,
                                                        @Part("title_en") RequestBody title_en,
                                                        @Part("category_id") RequestBody category_id,
                                                        @Part MultipartBody.Part photo);
    @GET("api/order/orders")
    Single<Response<OrderDataModel>> getOrders(@Header("Authorization") String authorization);

}