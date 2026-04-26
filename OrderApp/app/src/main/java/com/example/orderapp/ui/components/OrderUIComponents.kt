package com.example.orderapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.orderapp.model.Order

@Composable
fun MainScreenUI(
    orderList: List<Order>,
    onAddClick: () -> Unit,
    onItemClick: (Order) -> Unit
) {
    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = {
                    Text(
                        text = "Danh Sách Đơn Hàng",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Thêm", tint = Color.White)
            }
        }
    ) { padding ->
        if (orderList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Chưa có đơn hàng nào.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                contentPadding = padding,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                items(orderList) { order ->
                    OrderCard(order = order, onClick = { onItemClick(order) })
                }
            }
        }
    }
}

@Composable
fun OrderCard(order: Order, onClick: () -> Unit) {
    val statusColor = when (order.status.lowercase()) {
        "completed" -> Color(0xFF2E7D32)
        "cancelled" -> Color(0xFFC62828)
        else        -> Color(0xFFF57C00)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = order.customerName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(text = "SĐT: ${order.phoneNumber}", color = Color.Gray, fontSize = 13.sp)
                Text(
                    text = "Tổng tiền: ${order.totalPrice} VNĐ",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Surface(
                color = statusColor.copy(alpha = 0.15f),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = order.status,
                    color = statusColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewScreenUI(
    onSaveClick: (String, String, Double, String) -> Unit,
    onBackClick: () -> Unit
) {
    var name   by remember { mutableStateOf("") }
    var phone  by remember { mutableStateOf("") }
    var price  by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Pending") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thêm Đơn Hàng Mới", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    TextButton(onClick = onBackClick) {
                        Text("← Quay lại", color = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Tên Khách Hàng") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Số Điện Thoại") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Tổng Tiền (VNĐ)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = status,
                onValueChange = { status = it },
                label = { Text("Trạng Thái (Pending / Completed / Cancelled)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    onSaveClick(name, phone, price.toDoubleOrNull() ?: 0.0, status)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = name.isNotBlank() && phone.isNotBlank() && price.isNotBlank()
            ) {
                Text("💾  Lưu Đơn Hàng", fontSize = 16.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateScreenUI(
    initialOrder: Order,
    onUpdateClick: (Order) -> Unit,
    onDeleteClick: (Order) -> Unit,
    onBackClick: () -> Unit
) {
    var name   by remember { mutableStateOf(initialOrder.customerName) }
    var phone  by remember { mutableStateOf(initialOrder.phoneNumber) }
    var price  by remember { mutableStateOf(initialOrder.totalPrice.toString()) }
    var status by remember { mutableStateOf(initialOrder.status) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Xác nhận xoá") },
            text  = { Text("Bạn có chắc muốn xoá đơn hàng của \"${initialOrder.customerName}\"?") },
            confirmButton = {
                TextButton(onClick = {
                    showConfirmDialog = false
                    onDeleteClick(initialOrder)
                }) { Text("Xoá", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) { Text("Huỷ") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cập Nhật Đơn Hàng", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    TextButton(onClick = onBackClick) {
                        Text("← Quay lại", color = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "ID: #${initialOrder.id}",
                color = Color.Gray,
                fontSize = 13.sp
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Tên Khách Hàng") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Số Điện Thoại") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Tổng Tiền (VNĐ)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = status,
                onValueChange = { status = it },
                label = { Text("Trạng Thái") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val updated = initialOrder.copy(
                        customerName = name,
                        phoneNumber  = phone,
                        totalPrice   = price.toDoubleOrNull() ?: 0.0,
                        status       = status
                    )
                    onUpdateClick(updated)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = name.isNotBlank() && phone.isNotBlank() && price.isNotBlank()
            ) {
                Text("✏  Cập Nhật", fontSize = 16.sp)
            }

            OutlinedButton(
                onClick = { showConfirmDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("🗑  Xoá Đơn Hàng", fontSize = 16.sp)
            }
        }
    }
}

@Preview(showBackground = true, name = "Main Screen Preview")
@Composable
fun PreviewMainScreen() {
    MaterialTheme {
        MainScreenUI(
            orderList = listOf(
                Order(1, "Nguyễn Văn A", "0123456789", 500000.0, "Pending"),
                Order(2, "Trần Thị B",   "0987654321", 1200000.0, "Completed"),
                Order(3, "Lê Văn C",     "0911223344", 350000.0, "Cancelled")
            ),
            onAddClick  = {},
            onItemClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Add New Screen Preview")
@Composable
fun PreviewAddNewScreen() {
    MaterialTheme {
        AddNewScreenUI(
            onSaveClick = { _, _, _, _ -> },
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Update Screen Preview")
@Composable
fun PreviewUpdateScreen() {
    MaterialTheme {
        UpdateScreenUI(
            initialOrder = Order(1, "Nguyễn Văn A", "0123456789", 500000.0, "Pending"),
            onUpdateClick = {},
            onDeleteClick = {},
            onBackClick   = {}
        )
    }
}
