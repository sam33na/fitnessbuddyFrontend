package com.example.fitnessbuddy.response

import com.example.fitnessbuddy.entity.User
import com.example.fitnessbuddy.entity.OTP

data class OTPResponse (
    val message:String?=null,
    val data: OTP?=null
)