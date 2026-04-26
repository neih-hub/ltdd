package com.example.orderapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.example.orderapp.api.ApiResponse
import com.example.orderapp.api.RetrofitClient
import com.example.orderapp.model.Order
import com.example.orderapp.ui.components.UpdateScreenUI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        @Suppress("DEPRECATION")
        val orderData = intent.getSerializableExtra("ORDER_DATA") as? Order

        if (orderData == null) {
            Toast.makeText(this, "Không tìm thấy dữ liệu đơn hàng.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setContent {
            MaterialTheme {
                UpdateScreenUI(
                    initialOrder = orderData,

                    onUpdateClick = { updatedOrder ->
                        RetrofitClient.instance.updateOrder(updatedOrder)
                            .enqueue(object : Callback<ApiResponse> {
                                override fun onResponse(
                                    call: Call<ApiResponse>,
                                    response: Response<ApiResponse>
                                ) {
                                    if (response.isSuccessful) {
                                        Toast.makeText(
                                            this@UpdateActivity,
                                            "✅ Cập nhật thành công!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this@UpdateActivity,
                                            "Lỗi server: ${response.code()}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                                    Toast.makeText(
                                        this@UpdateActivity,
                                        "Lỗi kết nối: ${t.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            })
                    },

                    onDeleteClick = { orderToDelete ->
                        RetrofitClient.instance.deleteOrder(orderToDelete)
                            .enqueue(object : Callback<ApiResponse> {
                                override fun onResponse(
                                    call: Call<ApiResponse>,
                                    response: Response<ApiResponse>
                                ) {
                                    if (response.isSuccessful) {
                                        Toast.makeText(
                                            this@UpdateActivity,
                                            "🗑 Xoá thành công!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this@UpdateActivity,
                                            "Lỗi server: ${response.code()}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                                    Toast.makeText(
                                        this@UpdateActivity,
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
