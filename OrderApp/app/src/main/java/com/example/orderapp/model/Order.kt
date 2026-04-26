package com.example.orderapp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Order(
    @SerializedName("id")            val id: Int = 0,
    @SerializedName("customer_name") val customerName: String,
    @SerializedName("phone_number")  val phoneNumber: String,
    @SerializedName("total_price")   val totalPrice: Double,
    @SerializedName("status")        val status: String
) : Serializable
