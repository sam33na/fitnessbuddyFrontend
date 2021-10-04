package com.example.fitnessbuddy.repository

import com.example.fitnessbuddy.api.OTPApi
import com.example.fitnessbuddy.api.ServiceBuilder
import com.example.fitnessbuddy.response.OTPResponse
import com.example.fitnessbuddy.api.myApiRequest

class OTPRepository :myApiRequest(){
    private val otpApi= ServiceBuilder.buildService(OTPApi::class.java)
    suspend fun checkEmail(email:String): OTPResponse {
        return apiRequest {
            otpApi.checkEmail(email)
        }
    }

    suspend fun checkOtp(email:String,code:String): OTPResponse {
        return apiRequest {
            otpApi.checkOtp(email,code)
        }
    }
    suspend fun updatePass(email:String,password: String): OTPResponse {
        return apiRequest {
            otpApi.updatePass(email,password)
        }

    }

    suspend fun removeOTP(email:String): OTPResponse {
        return apiRequest {
            otpApi.removeOTP(email)
        }

    }
}