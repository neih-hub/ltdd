package com.example.orderapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.orderapp.api.RetrofitClient
import com.example.orderapp.model.Order
import com.example.orderapp.ui.components.MainScreenUI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                var orders by remember { mutableStateOf(listOf<Order>()) }
                val lifecycleOwner = LocalLifecycleOwner.current

                fun loadOrders() {
                    RetrofitClient.instance.getOrders()
                        .enqueue(object : Callback<List<Order>> {
                            override fun onResponse(
                                call: Call<List<Order>>,
                                response: Response<List<Order>>
                            ) {
                                if (response.isSuccessful) {
                                    orders = response.body() ?: emptyList()
                                } else {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Lỗi server: ${response.code()}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            override fun onFailure(call: Call<List<Order>>, t: Throwable) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Lỗi kết nối: ${t.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        })
                }

                DisposableEffect(lifecycleOwner) {
                    val observer = LifecycleEventObserver { _, event ->
                        if (event == Lifecycle.Event.ON_RESUME) {
                            loadOrders()
                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)
                    
                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }
                }

                MainScreenUI(
                    orderList = orders,
                    onAddClick = {
                        startActivity(Intent(this@MainActivity, AddNewActivity::class.java))
                    },
                    onItemClick = { order ->
                        val intent = Intent(this@MainActivity, UpdateActivity::class.java)
                        intent.putExtra("ORDER_DATA", order)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}