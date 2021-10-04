package com.example.fitnessbuddy.repository

import com.example.fitnessbuddy.entity.User
import com.example.fitnessbuddy.response.ImageResponse
import com.example.fitnessbuddy.response.RegisterResponse
import com.example.fitnessbuddy.api.ServiceBuilder
import com.example.fitnessbuddy.api.UserApi
import com.example.fitnessbuddy.api.myApiRequest
import com.example.fitnessbuddy.response.LoginRes
import okhttp3.MultipartBody

class UserRepository: myApiRequest(){
    private val userApi= ServiceBuilder.buildService(UserApi::class.java)

    //registering user
    suspend fun registerUser(register: User): RegisterResponse {
        return apiRequest {
            userApi.registerUser(register)
        }
    }

    //login
    suspend fun checkUser(username:String, password:String): LoginRes {
        return apiRequest {
            userApi.checkUser(username,password)
        }
    }

    //retrieve the detail of user
    suspend fun getUser(id:String):RegisterResponse{
        return apiRequest {
            userApi.getUser(ServiceBuilder.token!!,id)
        }
    }

    suspend fun updateUser(id : String, username : String, email : String, address : String,password : String?,phone : String): RegisterResponse{
        return apiRequest {
            userApi.updateUser(ServiceBuilder.token!!,id = id, username = username,email=email, address = address, password = password!!,phone = phone)
        }
    }

    suspend fun uploadImage(id:String,body: MultipartBody.Part): ImageResponse {
        return apiRequest {
            userApi.uploadImage(ServiceBuilder.token!!,id,body)
        }
    }
}