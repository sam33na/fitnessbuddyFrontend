package com.example.fitnessbuddy.api


import com.example.fitnessbuddy.response.OTPResponse
import retrofit2.Response
import retrofit2.http.*

interface OTPApi {
    @FormUrlEncoded
    @POST("forgot-password")
    suspend fun checkEmail(
        @Field("email") email: String
    ): Response<OTPResponse>

    @FormUrlEncoded
    @POST("checkOTP")
    suspend fun checkOtp(
        @Field("email") email: String,
        @Field("code") code: String
    ): Response<OTPResponse>


    @FormUrlEncoded
    @PUT("reset-password")
    suspend fun updatePass(
        @Field("email") email : String,
        @Field("password") password: String,
    ): Response<OTPResponse>


    @DELETE("delete-token/{email}")
    suspend fun removeOTP(
        @Path("email") email: String
    ): Response<OTPResponse>
}