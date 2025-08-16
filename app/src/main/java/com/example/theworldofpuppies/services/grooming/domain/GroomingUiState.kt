package com.example.theworldofpuppies.services.grooming.domain

import com.example.theworldofpuppies.core.domain.util.Error

data class GroomingUiState(
    val isLoading: Boolean = false,
    val error: Error? = null,
    val discount: Int = 25,
    val serviceFeatures: List<GroomingFeature> = mutableListOf(
        GroomingFeature(
            title = "Spa Bath",
            features = mutableListOf(
                "Bath With Shampoo & Conditioner",
                "Blow Dry",
                "Nail Clipping",
                "Ears Cleaning",
                "Eyes Cleaning",
                "Paw Massage",
                "Combing/Brushing"
            ),
            price = 800.00,
            discountedPrice = 800.00 * (100 - 25) / 100
        ),
        GroomingFeature(
            title = "Bath & Basic Grooming",
            features = mutableListOf(
                "Bath With Shampoo & Conditioner",
                "Blow Dry",
                "Face Haircut",
                "Teeth Cleaning/Mouth Spray",
                "Sanitary Trim",
                "Paw Massage",
                "Nail Clipping",
                "Ears Cleaning",
                "Eyes Cleaning",
                "Paw Massage",
                "Combing/Brushing"
            ),
            price = 1199.00,
            discountedPrice = 1199.00 * (100 - 25) / 100
        ),
        GroomingFeature(
            title = "Full Service",
            features = mutableListOf(
                "Bath With Shampoo & Conditioner",
                "Blow Dry",
                "Face Haircut",
                "Teeth Cleaning/Mouth Spray",
                "Sanitary Trim",
                "Paw Massage",
                "Nail Clipping",
                "Ears Cleaning",
                "Eyes Cleaning",
                "Paw Massage",
                "Combing/Brushing"
            ),
            price = 1499.00,
            discountedPrice = 1499.00 * (100 - 25) / 100
        ),
        GroomingFeature(
            title = "PawLuxe",
            features = mutableListOf(
                "Bath With Shampoo & Conditioner",
                "Blow Dry",
                "Face Haircut",
                "Teeth Cleaning/Mouth Spray",
                "Sanitary Trim",
                "Paw Massage",
                "Nail Clipping",
                "Ears Cleaning",
                "Eyes Cleaning",
                "Paw Massage",
                "Combing/Brushing"
            ),
            price = 1999.00,
            discountedPrice = 1999.00 * (100 - 25) / 100
        ),
        GroomingFeature(
            title = "Trans-fur-mation",
            features = mutableListOf(
                "Full Body Trimming",
                "Ear Cleaning",
                "Eyes Cleaning",
                "Nail Clipping"
            ),
            price = 1199.00,
            discountedPrice = 1199.00 * (100 - 25) / 100
        )
    )
)
