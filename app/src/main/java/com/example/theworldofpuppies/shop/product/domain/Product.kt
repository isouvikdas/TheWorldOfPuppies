package com.example.theworldofpuppies.shop.product.domain

data class Product(
    val localId: Long,
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val discountedPrice: Double,
    val discount: Int = 0,
    val inventory: Int,
    val categoryName: String,
    val imageIds: List<String> = ArrayList(),
    val firstImageId: String? = null,
    val firstImageUri: String? = "",
    val isFeatured: Boolean? = false,
    val isDetailsFetched: Boolean = false,
    val isImagesFetched: Boolean = false,
    val isRecommended: Boolean = false,
    val rating: Double = 0.0
) {
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            "$name",
            "${name.first()}",
            "${name.last()}",
            "$price",
            "$categoryName",
            "$description.",
            "$discountedPrice"
        )
        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}
