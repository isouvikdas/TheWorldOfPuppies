package com.example.theworldofpuppies.review.presentation

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.theworldofpuppies.booking.core.domain.Category
import com.example.theworldofpuppies.core.domain.util.NetworkError
import com.example.theworldofpuppies.core.domain.util.Result
import com.example.theworldofpuppies.core.presentation.util.toString
import com.example.theworldofpuppies.review.domain.ReviewListState
import com.example.theworldofpuppies.review.domain.ReviewRepository
import com.example.theworldofpuppies.review.domain.ReviewUiState
import com.example.theworldofpuppies.review.domain.TargetType
import com.example.theworldofpuppies.review.presentation.utils.ReviewEvent
import com.example.theworldofpuppies.review.presentation.utils.ReviewEventManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReviewViewModel(
    private val reviewRepository: ReviewRepository,
    private val reviewEvenManager: ReviewEventManager
) : ViewModel() {

    private val _reviewUiState = MutableStateFlow(ReviewUiState())
    val reviewUiState = _reviewUiState.asStateFlow()

    private val _reviewListState = MutableStateFlow(ReviewListState())
    val reviewListState = _reviewListState.asStateFlow()

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    fun onDescriptionChange(value: String) {
        _reviewUiState.update { it.copy(description = value) }
    }

    fun onStarsChange(value: Float) {
        _reviewUiState.update { it.copy(stars = value) }
    }

    fun setOrderType(targetId: String) {
        viewModelScope.launch {
            _reviewUiState.update { it.copy(targetType = TargetType.ORDER, targetId = targetId) }
        }
    }

    fun setBookingType(targetId: String, subType: Category) {
        viewModelScope.launch {
            _reviewUiState.update {
                it.copy(
                    targetType = TargetType.BOOKING,
                    subType = subType,
                    targetId = targetId
                )
            }
        }
    }

    suspend fun sendToastEvent(message: String) {
        _toastEvent.emit(message)
    }

    fun resetReviewState() {
        viewModelScope.launch {
            _reviewUiState.update {
                it.copy(
                    stars = 0f,
                    description = "",
                    isLoading = false,
                    errorMessage = null,
                    targetId = "",
                    targetType = TargetType.NULL,
                    subType = null
                )
            }

        }
    }

    fun addReview(
        context: Context,
        navController: NavController
    ) {
        viewModelScope.launch {
            try {
                _reviewUiState.update { it.copy(isLoading = true, errorMessage = null) }
                val state = reviewUiState.value
                when (val result = reviewRepository.addReview(
                    targetType = state.targetType,
                    subType = state.subType,
                    targetId = state.targetId,
                    stars = state.stars.toInt(),
                    description = state.description
                )) {
                    is Result.Success -> {
                        reviewEvenManager.sendEvent(
                            ReviewEvent.ReviewConfirmed(
                                state.targetId,
                                targetType = state.targetType
                            )
                        )
                        sendToastEvent("Review submitted successfully")
                        navController.popBackStack()
                    }

                    is Result.Error -> {
                        sendToastEvent(result.error.toString(context))
                    }
                }
            } catch (e: Exception) {
                sendToastEvent(e.message.toString())
            } finally {
                _reviewUiState.update { it.copy(isLoading = false) }
                resetReviewState()
            }
        }
    }

    fun getProductReviews(productId: String) {
        viewModelScope.launch {
            try {
                _reviewListState.update {
                    it.copy(
                        productReviewLoading = true,
                        productReviewError = null
                    )
                }
                when (val result = reviewRepository.getProductReviews(productId)) {
                    is Result.Success -> {
                        _reviewListState.update {
                            it.copy(
                                productReviews = result.data + it.productReviews
                            )
                        }
                    }

                    is Result.Error -> {
                        _reviewListState.update {
                            it.copy(
                                productReviewError = result.error
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _reviewListState.update {
                    it.copy(
                        productReviewError = NetworkError.UNKNOWN
                    )
                }
            } finally {
                _reviewListState.update { it.copy(productReviewLoading = false) }
            }
        }
    }

    fun getBookingReviews(subType: Category) {
        viewModelScope.launch {
            try {
                handleBookingLoadingAndError(subType, true)
                when (val result = reviewRepository.getBookingReviews(subType)) {
                    is Result.Success -> {
                        _reviewListState.update { state ->
                            when (subType) {
                                Category.GROOMING -> {
                                    state.copy(
                                        groomingReviews = result.data,
                                        groomingReviewLoading = false
                                    )
                                }

                                Category.WALKING -> state.copy(
                                    petWalkReviews = result.data,
                                    petWalkReviewLoading = false
                                )

                                Category.DOG_TRAINING -> state.copy(
                                    dogTrainingReviews = result.data,
                                    dogTrainingReviewLoading = false
                                )

                                Category.VETERINARY -> state.copy(
                                    vetReviews = result.data,
                                    vetReviewLoading = false
                                )

                                else -> state
                            }
                        }
                    }

                    is Result.Error -> {
                        handleBookingLoadingAndError(subType, false, result.error)
                    }
                }
            } catch (e: Exception) {
                handleBookingLoadingAndError(subType, false, NetworkError.UNKNOWN)
            }
        }
    }

    private fun handleBookingLoadingAndError(
        subType: Category,
        value: Boolean,
        error: NetworkError? = null
    ) {
        _reviewListState.update { state ->
            when (subType) {
                Category.GROOMING -> state.copy(
                    groomingReviewLoading = value,
                    groomingReviewError = if (!value) error else null
                )

                Category.WALKING -> state.copy(
                    petWalkReviewLoading = value,
                    petWalkReviewError = if (!value) error else null
                )

                Category.DOG_TRAINING -> state.copy(
                    dogTrainingReviewLoading = value,
                    dogTrainingReviewError = if (!value) error else null
                )

                Category.VETERINARY -> state.copy(
                    vetReviewLoading = value,
                    vetReviewError = if (!value) error else null
                )

                else -> state
            }
        }
    }
}