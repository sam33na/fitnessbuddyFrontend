package com.example.fitnessbuddy.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    var _id :String="",
    val name:String?=null,
    val email:String?=null,
    val username:String?=null,
    val password:String?=null,
    val phone:String?=null,
    val dp:String?=null
)