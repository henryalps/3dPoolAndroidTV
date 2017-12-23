package com.intel.tvpresent.data.remote;

import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by henryalps on 2017/9/10.
 */

public interface PickyHttpImpl {
    @FormUrlEncoded
    @POST("arpoolcharge/monitor/login")
    Call<ResponseBody> login(@Field("serialNum") String serialNum);
}
