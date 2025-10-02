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
    val images: List<Image> = emptyList(),
    val firstImage: Image? = null,
    val isFeatured: Boolean? = false,
    val isDetailsFetched: Boolean = false,
    val isImagesFetched: Boolean = false,
    val isRecommended: Boolean = false,
    val isRated: Boolean = false,
    val averageStars : Double = 0.0,
    val totalReviews: Int
) {
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            name,
            name.first().toString(),
            name.last().toString(),
            price.toString(),
            categoryName,
            description,
            discountedPrice.toString()
        )
        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}
