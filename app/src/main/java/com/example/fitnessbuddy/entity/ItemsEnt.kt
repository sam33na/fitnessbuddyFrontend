package com.example.fitnessbuddy.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class ItemsEnt(
    @PrimaryKey
    var _id :String="",
    val act:String?=null,
    val description:String?=null,
    val date:String?=null,
    val start:String?=null,
    val end:String?=null,
    val priority:String?=null
)