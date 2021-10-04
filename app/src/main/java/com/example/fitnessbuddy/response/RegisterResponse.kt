package com.example.fitnessbuddy.response

import com.example.fitnessbuddy.entity.User

data class RegisterResponse (
    val message:String?=null,
    val success: Boolean?=null,
    val data: User?=null
)