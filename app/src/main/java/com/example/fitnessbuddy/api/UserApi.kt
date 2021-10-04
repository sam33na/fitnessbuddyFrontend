package com.example.fitnessbuddy.api

import com.example.fitnessbuddy.entity.User
import com.example.fitnessbuddy.response.ImageResponse
import com.example.fitnessbuddy.response.LoginRes
import com.example.fitnessbuddy.response.RegisterResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*


interface UserApi {
    // to register the user
    @POST("register/user")
    suspend fun registerUser(
        @Body register: User
    ): Response<RegisterResponse>


    //login
    @POST("login/user")
    @FormUrlEncoded
    suspend fun checkUser(@Field("username") username:String,
                          @Field("password") password:String): Response<LoginRes>


    //retrieve the detail of logged in user
    @GET("user/{id}")
    suspend fun getUser(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ):Response<RegisterResponse>



    @FormUrlEncoded
    @PUT("userq/update")
    suspend fun updateUser(
       @Header("Authorization") token: String,
        @Field("id") id : String,
        @Field("username") username : String,
        @Field("email") email : String,
        @Field("address") address : String,
        @Field("password") password: String,
        @Field("phone") phone: String,
    ): Response<RegisterResponse>


    @Multipart
    @PUT("userr/profile/image/{id}")
    suspend fun uploadImage(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Part file: MultipartBody.Part
    ): Response<ImageResponse>
}