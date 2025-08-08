package com.example.theworldofpuppies.shop.order.data.response

import com.example.theworldofpuppies.address.data.dto.AddressDto
import com.example.theworldofpuppies.address.domain.Address
import com.example.theworldofpuppies.shop.order.domain.enums.OrderStatus
import com.example.theworldofpuppies.shop.order.domain.enums.ReturnStatus
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class OrderDto(
    val id: String,
    val publicOrderId: String,
    val userId: String,
    val orderItemIds: List<String>,
    val totalAmount: Double,
    val cartTotalAmount: Double,
    val orderStatus: OrderStatus,
    val returnStatus: ReturnStatus,
    val address: AddressDto,
    val deliveryDate: Long,
    val createdDate: Long,
    val updatedDate: Long
)
