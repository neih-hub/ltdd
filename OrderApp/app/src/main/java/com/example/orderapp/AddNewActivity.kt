package com.example.orderapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.example.orderapp.api.ApiResponse
import com.example.orderapp.api.RetrofitClient
import com.example.orderapp.model.Order
import com.example.orderapp.ui.components.AddNewScreenUI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddNewActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AddNewScreenUI(
                    onSaveClick = { name, phone, price, status ->
                        val newOrder = Order(
                            customerName = name,
                            phoneNumber  = phone,
                            totalPrice   = price,
                            status       = status
                        )

                        RetrofitClient.instance.createOrder(newOrder)
                            .enqueue(object : Callback<ApiResponse> {
                                override fun onResponse(
                                    call: Call<ApiResponse>,
                                    response: Response<ApiResponse>
                                ) {
                                    if (response.isSuccessful) {
                                        Toast.makeText(
                                            this@AddNewActivity,
                                            "Thêm thành công!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this@AddNewActivity,
                                            "Lỗi server: ${response.code()}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                                    Toast.makeText(
                                        this@AddNewActivity,
                                        "Lỗi kết nối: ${t.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            })
                    },
                    onBackClick = { finish() }
                )
            }
        }
    }
}
