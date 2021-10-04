package com.example.fitnessbuddy.response

import android.content.ClipData

data class AddItemResponse (
    val message:String?=null,
    val success:Boolean?=null,
    val data: ClipData.Item?=null

)