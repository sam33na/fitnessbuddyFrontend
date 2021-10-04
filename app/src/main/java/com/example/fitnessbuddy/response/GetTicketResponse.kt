package com.example.fitnessbuddy.response

import com.example.fitnessbuddy.entity.ItemsEnt

data class GetTicketResponse (
    val message:String?=null,
    val data:MutableList<ItemsEnt>?=null
)
