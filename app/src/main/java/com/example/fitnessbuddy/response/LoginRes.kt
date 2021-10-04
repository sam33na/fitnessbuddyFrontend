package com.example.fitnessbuddy.response

import com.example.fitnessbuddy.entity.User

data class LoginRes(
    val token:String?=null,
    val success: Boolean?=null,
    val message:String?=null,
    val data: User?=null,
    val role:String?=null
)
