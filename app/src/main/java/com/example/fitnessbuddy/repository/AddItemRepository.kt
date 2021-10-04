package com.example.fitnessbuddy.repository

import com.example.fitnessbuddy.api.AddItemAPI
import com.example.fitnessbuddy.api.ServiceBuilder
import com.example.fitnessbuddy.api.myApiRequest
import com.example.fitnessbuddy.entity.ItemsEnt
import com.example.fitnessbuddy.response.AddItemResponse
import com.example.fitnessbuddy.response.GetTicketResponse

class AddItemRepository: myApiRequest() {
    private val addItemApi= ServiceBuilder.buildService(AddItemAPI::class.java)

    suspend fun Addplan(addItem: ItemsEnt): AddItemResponse {
        return apiRequest {
            addItemApi.addTicket(ServiceBuilder.token!!,addItem)
        }
    }

    suspend fun getTicket(id:String): AddItemResponse {
        return apiRequest {
            addItemApi.getTicket(ServiceBuilder.token!!,id)
        }
    }

    suspend fun updateMyTicket(id:String,ticket: ItemsEnt): AddItemResponse {
        return apiRequest {
            addItemApi.updateMyTicket(ServiceBuilder.token!!,id,ticket)
        }

    }

    suspend fun deleteMyTicket(id:String): AddItemResponse {
        return apiRequest {
            addItemApi.deleteMyTicket(ServiceBuilder.token!!,id)
        }

    }


}
