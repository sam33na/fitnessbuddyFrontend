package com.example.fitnessbuddy.api

import com.example.fitnessbuddy.entity.ItemsEnt
import com.example.fitnessbuddy.response.AddItemResponse
import retrofit2.Response
import retrofit2.http.*

interface AddItemAPI {
    //Add Ticket
    @POST("add/ticket")
    suspend fun addTicket(
        @Header("Authorization") token:String,
        @Body addTicket: ItemsEnt
    ): Response<AddItemResponse>

    //Show all the ticket of logged in admin
    @GET("myticket/{id}")
    suspend fun getmyTicket(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): Response<AddItemResponse>

    //fetch data of single ticket
    @GET("getTicket/{id}")
    suspend fun getTicket(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): Response<AddItemResponse>



    //update ticket
    @PUT("update/ticket/{id}")
    suspend fun updateMyTicket(
        @Header("Authorization") token:String,
        @Path("id") id: String,
        @Body ticket: ItemsEnt
    ): Response<AddItemResponse>

    //delete ticket
    @DELETE("delete/ticket/{id}")
    suspend fun deleteMyTicket(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<AddItemResponse>



}