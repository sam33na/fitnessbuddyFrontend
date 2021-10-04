package com.example.fitnessbuddy.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class OTP (
    @PrimaryKey
    var _id :String="",
    val email:String?=null,
    val code:String?=null,
        )