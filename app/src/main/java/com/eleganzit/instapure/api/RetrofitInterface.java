package com.eleganzit.instapure.api;



import com.eleganzit.instapure.model.AddFeedbackResponse;
import com.eleganzit.instapure.model.AddPromotionResponse;
import com.eleganzit.instapure.model.CompanyListResponse;
import com.eleganzit.instapure.model.GetUserResponse;
import com.eleganzit.instapure.model.HistoryListResponse;
import com.eleganzit.instapure.model.ImageFeedResponse;
import com.eleganzit.instapure.model.NotificationsResponse;
import com.eleganzit.instapure.model.RegisterUserResponse;
import com.eleganzit.instapure.model.ReorderBannerResponse;
import com.eleganzit.instapure.model.ReorderResponse;
import com.eleganzit.instapure.model.RewardsResponse;
import com.eleganzit.instapure.model.ScanQRResponse;
import com.eleganzit.instapure.model.UpdateProfileResponse;
import com.eleganzit.instapure.model.ValidateLoginResponse;
import com.eleganzit.instapure.model.VideoFeedResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by eleganz on 30/4/19.
 */

public interface RetrofitInterface {

    @FormUrlEncoded()
    @POST("/Instapure/instapure-api/addUser")
    Call<RegisterUserResponse> registerUser(
            @Field("fullname") String fullname,
            @Field("gender") String gender,
            @Field("company_id") String company_id,
            @Field("contact") String contact,
            @Field("email") String email,
            @Field("address") String address,
            @Field("language") String language,
            @Field("device_token") String device_token

    );

    @FormUrlEncoded()
    @POST("/Instapure/instapure-api/validateUser")
    Call<ValidateLoginResponse> loginUser(
            @Field("contact") String contact,
            @Field("sentcode") String sentcode,
            @Field("device_token") String device_token

    );


    @Multipart
    @POST("/Instapure/instapure-api/updateProfile")
    Call<UpdateProfileResponse> updateProfile(@Part("user_id") RequestBody user_id,
                                              @Part MultipartBody.Part photo,
                                              @Part("fullname") RequestBody fullname,
                                              @Part("gender") RequestBody gender,
                                              @Part("company_id") RequestBody company_id,
                                              @Part("contact") RequestBody contact,
                                              @Part("email") RequestBody email,
                                              @Part("address") RequestBody address
    );

    @FormUrlEncoded()
    @POST("/Instapure/instapure-api/updateProfile")
    Call<UpdateProfileResponse> updateProfile(
            @Field("user_id") String user_id,
            @Field("fullname") String fullname,
            @Field("gender") String gender,
            @Field("company_id") String company_id,
            @Field("contact") String contact,
            @Field("email") String email,
            @Field("address") String address
    );

    @FormUrlEncoded()
    @POST("/Instapure/instapure-api/companyList")
    Call<CompanyListResponse> companyList(
            @Field("dummy") String dummy

    );

    @FormUrlEncoded()
    @POST("/Instapure/instapure-api/getUser")
    Call<GetUserResponse> getUser(
            @Field("user_id") String user_id

    );

    @FormUrlEncoded()
    @POST("/Instapure/instapure-api/getRewards")
    Call<RewardsResponse> getRewards(
            @Field("user_id") String user_id

    );

    @FormUrlEncoded()
    @POST("/Instapure/instapure-api/addFeedback")
    Call<AddFeedbackResponse> addFeedback(
            @Field("user_id") String user_id,
            @Field("feedback") String feedback

    );

    @FormUrlEncoded()
    @POST("/Instapure/instapure-api/userReorder")
    Call<ReorderResponse> userReorder(
            @Field("user_id") String user_id,
            @Field("q1") String q1,
            @Field("q2") String q2,
            @Field("q3") String q3,
            @Field("comments") String comments
    );


    @FormUrlEncoded()
    @POST("/Instapure/instapure-api/videoFeedlist")
    Call<VideoFeedResponse> videoFeedlist(
            @Field("from_limit") String from_limit,
            @Field("to_limit") String to_limit,
            @Field("video_type") String video_type

    );

    @FormUrlEncoded()
    @POST("/Instapure/instapure-api/imageFeedlist")
    Call<ImageFeedResponse> imageFeedlist(
            @Field("from_limit") String from_limit,
            @Field("image_type") String image_type

    );

    @FormUrlEncoded()
    @POST("/Instapure/instapure-api/addPromotion")
    Call<AddPromotionResponse> addPromotion(
            @Field("user_id") String user_id,
            @Field("promo_type") String promo_type,
            @Field("feed_id") String feed_id
    );


    @FormUrlEncoded()
    @POST("/Instapure/instapure-api/userHistory")
    Call<HistoryListResponse> historyList(
            @Field("user_id") String user_id,
            @Field("from_limit") String from_limit,
            @Field("to_limit") String to_limit,
            @Field("select_months") String select_months

    );

    @FormUrlEncoded()
    @POST("/Instapure/instapure-api/userHistory")
    Call<HistoryListResponse> historyFilterList(
            @Field("user_id") String user_id,
            @Field("from_limit") String from_limit,
            @Field("to_limit") String to_limit,
            @Field("select_month") String select_month

    );

    @FormUrlEncoded()
    @POST("/Instapure/instapure-api/scanQRcode")
    Call<ScanQRResponse> scanQRcode(
            @Field("user_id") String user_id,
            @Field("code") String code

    );

    @FormUrlEncoded()
    @POST("/Instapure/instapure-api/reorderBanner")
    Call<ReorderBannerResponse> reorderBanner(
            @Field("dummy") String dummy

    );


    @FormUrlEncoded()
    @POST("/Instapure/instapure-api/notifications")
    Call<NotificationsResponse> notifications(
            @Field("from_limit") String from_limit

    );


}
