package com.example.theworldofpuppies.di

import android.content.Context
import androidx.room.Room
import com.example.theworldofpuppies.address.data.AddressRepositoryImpl
import com.example.theworldofpuppies.address.data.local.DummyAddressApi
import com.example.theworldofpuppies.address.data.remote.AddressApi
import com.example.theworldofpuppies.address.domain.AddressRepository
import com.example.theworldofpuppies.address.presentation.AddressViewModel
import com.example.theworldofpuppies.auth.data.AuthApiImpl
import com.example.theworldofpuppies.auth.domain.AuthApi
import com.example.theworldofpuppies.auth.presentation.login.AuthEventManager
import com.example.theworldofpuppies.auth.presentation.login.LoginViewModel
import com.example.theworldofpuppies.auth.presentation.register.RegistrationViewModel
import com.example.theworldofpuppies.booking.core.presentation.utils.BookingEventManager
import com.example.theworldofpuppies.booking.dog_training.data.DogTrainingBookingRepositoryImpl
import com.example.theworldofpuppies.booking.dog_training.data.remote.DogTrainingBookingApi
import com.example.theworldofpuppies.booking.dog_training.domain.DogTrainingBookingRepository
import com.example.theworldofpuppies.booking.dog_training.presentation.DogTrainingBookingViewModel
import com.example.theworldofpuppies.booking.grooming.data.BookingGroomingRepositoryImpl
import com.example.theworldofpuppies.booking.grooming.data.DummyGroomingBookingApi
import com.example.theworldofpuppies.booking.grooming.data.GroomingBookingApi
import com.example.theworldofpuppies.booking.pet_walk.data.PetWalkBookingApi
import com.example.theworldofpuppies.booking.pet_walk.data.PetWalkBookingRepositoryImpl
import com.example.theworldofpuppies.booking.grooming.domain.BookingGroomingRepository
import com.example.theworldofpuppies.booking.pet_walk.domain.PetWalkBookingRepository
import com.example.theworldofpuppies.booking.grooming.presentation.GroomingBookingViewModel
import com.example.theworldofpuppies.booking.history.data.BookingHistoryApi
import com.example.theworldofpuppies.booking.history.data.BookingHistoryRepositoryImpl
import com.example.theworldofpuppies.booking.history.domain.BookingHistoryRepository
import com.example.theworldofpuppies.booking.history.presentation.BookingHistoryViewModel
import com.example.theworldofpuppies.booking.pet_walk.presentation.BookingPetWalkViewModel
import com.example.theworldofpuppies.booking.vet.data.VetBookingApi
import com.example.theworldofpuppies.booking.vet.data.VetBookingRepositoryImpl
import com.example.theworldofpuppies.booking.vet.domain.VetBookingRepository
import com.example.theworldofpuppies.booking.vet.presentation.VetBookingViewModel
import com.example.theworldofpuppies.core.data.local.Database
import com.example.theworldofpuppies.core.data.networking.HttpClientFactory
import com.example.theworldofpuppies.core.domain.UserRepository
import com.example.theworldofpuppies.core.presentation.AuthViewModel
import com.example.theworldofpuppies.membership.data.PremiumOptionApi
import com.example.theworldofpuppies.membership.data.PremiumOptionRepositoryImpl
import com.example.theworldofpuppies.membership.domain.PremiumOptionRepository
import com.example.theworldofpuppies.membership.presentation.PremiumOptionViewModel
import com.example.theworldofpuppies.pet_insurance.data.PetInsuranceRepositoryImpl
import com.example.theworldofpuppies.pet_insurance.data.remote.PetInsuranceBookingApi
import com.example.theworldofpuppies.pet_insurance.domain.PetInsuranceRepository
import com.example.theworldofpuppies.pet_insurance.presentation.PetInsuranceViewModel
import com.example.theworldofpuppies.profile.pet.data.PetRepositoryImpl
import com.example.theworldofpuppies.profile.pet.data.remote.PetApi
import com.example.theworldofpuppies.profile.pet.domain.PetRepository
import com.example.theworldofpuppies.profile.pet.presentation.PetProfileViewModel
import com.example.theworldofpuppies.profile.presentation.ProfileViewModel
import com.example.theworldofpuppies.profile.user.data.UpdateUserApi
import com.example.theworldofpuppies.profile.user.data.UpdateUserRepositoryImpl
import com.example.theworldofpuppies.profile.user.domain.UpdateUserRepository
import com.example.theworldofpuppies.profile.user.presentation.UpdateUserViewModel
import com.example.theworldofpuppies.refer_earn.data.ReferEarnRepositoryImpl
import com.example.theworldofpuppies.refer_earn.data.remote.ReferEarnApi
import com.example.theworldofpuppies.refer_earn.domain.ReferEarnRepository
import com.example.theworldofpuppies.refer_earn.presentation.ReferEarnViewModel
import com.example.theworldofpuppies.review.data.ReviewApi
import com.example.theworldofpuppies.review.data.ReviewRepositoryImpl
import com.example.theworldofpuppies.review.domain.ReviewRepository
import com.example.theworldofpuppies.review.presentation.ReviewViewModel
import com.example.theworldofpuppies.review.presentation.utils.ReviewEventManager
import com.example.theworldofpuppies.services.dog_training.data.DogTrainingRepositoryImpl
import com.example.theworldofpuppies.services.dog_training.data.remote.DogTrainingApi
import com.example.theworldofpuppies.services.dog_training.domain.DogTrainingRepository
import com.example.theworldofpuppies.services.dog_training.presentation.DogTrainingViewModel
import com.example.theworldofpuppies.services.grooming.data.GroomingRepositoryImpl
import com.example.theworldofpuppies.services.grooming.data.remote.GroomingApi
import com.example.theworldofpuppies.services.grooming.domain.GroomingRepository
import com.example.theworldofpuppies.services.grooming.presentation.GroomingViewModel
import com.example.theworldofpuppies.services.pet_walking.data.PetWalkRepositoryImpl
import com.example.theworldofpuppies.services.pet_walking.data.remote.PetWalkApi
import com.example.theworldofpuppies.services.pet_walking.domain.PetWalkRepository
import com.example.theworldofpuppies.services.pet_walking.presentation.PetWalkingViewModel
import com.example.theworldofpuppies.services.vet.data.VetRepositoryImpl
import com.example.theworldofpuppies.services.vet.data.remote.DemoApi
import com.example.theworldofpuppies.services.vet.data.remote.VetApi
import com.example.theworldofpuppies.services.vet.domain.VetRepository
import com.example.theworldofpuppies.services.vet.presentation.VetViewModel
import com.example.theworldofpuppies.shop.cart.data.CartApi
import com.example.theworldofpuppies.shop.cart.data.CartRepositoryImpl
import com.example.theworldofpuppies.shop.cart.domain.CartRepository
import com.example.theworldofpuppies.shop.cart.presentation.CartViewModel
import com.example.theworldofpuppies.shop.order.data.OrderApi
import com.example.theworldofpuppies.shop.order.data.OrderRepositoryImpl
import com.example.theworldofpuppies.shop.order.data.PaymentApi
import com.example.theworldofpuppies.shop.order.data.PaymentRepositoryImpl
import com.example.theworldofpuppies.shop.order.domain.OrderRepository
import com.example.theworldofpuppies.shop.order.domain.PaymentRepository
import com.example.theworldofpuppies.shop.order.presentation.OrderViewModel
import com.example.theworldofpuppies.shop.order.presentation.utils.OrderEventManager
import com.example.theworldofpuppies.shop.product.data.remote.CategoryRepositoryImpl
import com.example.theworldofpuppies.shop.product.data.remote.DummyApi
import com.example.theworldofpuppies.shop.product.data.remote.ProductApiImpl
import com.example.theworldofpuppies.shop.product.data.remote.ProductRepositoryImpl
import com.example.theworldofpuppies.shop.product.domain.CategoryRepository
import com.example.theworldofpuppies.shop.product.domain.ProductApi
import com.example.theworldofpuppies.shop.product.domain.ProductRepository
import com.example.theworldofpuppies.shop.product.presentation.product_list.ProductViewModel
import io.ktor.client.engine.cio.CIO
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { HttpClientFactory.create(CIO.create()) }
    single { androidContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE) }
    single {
        Room.databaseBuilder(
            androidContext(),
            Database::class.java,
            "product_database"
        ).fallbackToDestructiveMigration()
            .build()
    }
    single { get<Database>().productDao }
    single { get<Database>().categoryDao }
    singleOf(::ProductApiImpl).bind<ProductApi>()
    singleOf(::PaymentApi)
    singleOf(::DummyAddressApi)
    singleOf(::AddressApi)
    singleOf(::OrderApi)
    singleOf(::DummyApi)
    singleOf(::GroomingApi)
    singleOf(::DummyGroomingBookingApi)
    singleOf(::GroomingBookingApi)
    singleOf(::PetWalkApi)
    singleOf(::PetWalkBookingApi)
    singleOf(::VetApi)
    singleOf(::DemoApi)
    singleOf(::VetBookingApi)
    singleOf(::DogTrainingApi)
    singleOf(::PetInsuranceBookingApi)
    singleOf(::DogTrainingBookingApi)
    singleOf(::PetApi)
    singleOf(::BookingHistoryApi)
    singleOf(::ReferEarnApi)
    singleOf(::UserRepository)
    singleOf(::AuthEventManager)
    singleOf(::OrderEventManager)
    singleOf(::ReviewEventManager)
    singleOf(::BookingEventManager)
    singleOf(::ProductRepositoryImpl).bind<ProductRepository>()
    singleOf(::AuthApiImpl).bind<AuthApi>()
    singleOf(::CategoryRepositoryImpl).bind<CategoryRepository>()
    singleOf(::CartApi)
    singleOf(::ReviewApi)
    singleOf(::UpdateUserApi)
    singleOf(::PremiumOptionApi)
    singleOf(::CartRepositoryImpl).bind<CartRepository>()
    singleOf(::OrderRepositoryImpl).bind<OrderRepository>()
    singleOf(::PaymentRepositoryImpl).bind<PaymentRepository>()
    singleOf(::AddressRepositoryImpl).bind<AddressRepository>()
    singleOf(::GroomingRepositoryImpl).bind<GroomingRepository>()
    singleOf(::BookingGroomingRepositoryImpl).bind<BookingGroomingRepository>()
    singleOf(::PetWalkRepositoryImpl).bind<PetWalkRepository>()
    singleOf(::PetWalkBookingRepositoryImpl).bind<PetWalkBookingRepository>()
    singleOf(::VetRepositoryImpl).bind<VetRepository>()
    singleOf(::VetBookingRepositoryImpl).bind<VetBookingRepository>()
    singleOf(::DogTrainingRepositoryImpl).bind<DogTrainingRepository>()
    singleOf(::DogTrainingBookingRepositoryImpl).bind<DogTrainingBookingRepository>()
    singleOf(::PetInsuranceRepositoryImpl).bind<PetInsuranceRepository>()
    singleOf(::PetRepositoryImpl).bind<PetRepository>()
    singleOf(::ReviewRepositoryImpl).bind<ReviewRepository>()
    singleOf(::BookingHistoryRepositoryImpl).bind<BookingHistoryRepository>()
    singleOf(::UpdateUserRepositoryImpl).bind<UpdateUserRepository>()
    singleOf(::ReferEarnRepositoryImpl).bind<ReferEarnRepository>()
    singleOf(::PremiumOptionRepositoryImpl).bind<PremiumOptionRepository>()

    viewModelOf(::AuthViewModel)
    viewModelOf(::ProductViewModel)
    viewModelOf(::CartViewModel)
    viewModelOf(::RegistrationViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::OrderViewModel)
    viewModelOf(::AddressViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::GroomingViewModel)
    viewModelOf(::PetWalkingViewModel)
    viewModelOf(::PetProfileViewModel)
    viewModelOf(::GroomingBookingViewModel)
    viewModelOf(::BookingPetWalkViewModel)
    viewModelOf(::VetViewModel)
    viewModelOf(::VetBookingViewModel)
    viewModelOf(::DogTrainingViewModel)
    viewModelOf(::DogTrainingBookingViewModel)
    viewModelOf(::PetInsuranceViewModel)
    viewModelOf(::ReviewViewModel)
    viewModelOf(::BookingHistoryViewModel)
    viewModelOf(::UpdateUserViewModel)
    viewModelOf(::ReferEarnViewModel)
    viewModelOf(::PremiumOptionViewModel)
}
