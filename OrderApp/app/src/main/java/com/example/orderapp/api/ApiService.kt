package com.example.orderapp.api

import com.example.orderapp.model.Order
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class ApiResponse(val message: String)

interface ApiService {

    @GET("order_api/get_orders.php")
    fun getOrders(): Call<List<Order>>

    @POST("order_api/create_order.php")
    fun createOrder(@Body order: Order): Call<ApiResponse>

    @POST("order_api/update_order.php")
    fun updateOrder(@Body order: Order): Call<ApiResponse>

    @POST("order_api/delete_order.php")
    fun deleteOrder(@Body order: Order): Call<ApiResponse>
}
