package com.example.theworldofpuppies.di

import android.content.Context
import androidx.room.Room
import com.example.theworldofpuppies.address.data.AddressRepositoryImpl
import com.example.theworldofpuppies.address.data.DummyAddressApi
import com.example.theworldofpuppies.address.domain.AddressRepository
import com.example.theworldofpuppies.address.presentation.AddressViewModel
import com.example.theworldofpuppies.auth.data.AuthApiImpl
import com.example.theworldofpuppies.auth.domain.AuthApi
import com.example.theworldofpuppies.auth.presentation.login.AuthEventManager
import com.example.theworldofpuppies.auth.presentation.login.LoginViewModel
import com.example.theworldofpuppies.auth.presentation.register.RegistrationViewModel
import com.example.theworldofpuppies.core.data.local.Database
import com.example.theworldofpuppies.core.data.networking.HttpClientFactory
import com.example.theworldofpuppies.core.domain.UserRepository
import com.example.theworldofpuppies.core.presentation.AuthViewModel
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
import com.example.theworldofpuppies.shop.product.data.remote.ProductApi
import com.example.theworldofpuppies.shop.product.data.remote.ProductRepositoryImpl
import com.example.theworldofpuppies.shop.product.domain.CategoryRepository
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
    singleOf(::ProductApi)
    singleOf(::PaymentApi)
    singleOf(::DummyAddressApi)
    singleOf(::OrderApi)
    singleOf(::DummyApi)
    singleOf(::UserRepository)
    singleOf(::AuthEventManager)
    singleOf(::OrderEventManager)
    singleOf(::ProductRepositoryImpl).bind<ProductRepository>()
    singleOf(::AuthApiImpl).bind<AuthApi>()
    singleOf(::CategoryRepositoryImpl).bind<CategoryRepository>()
    singleOf(::CartApi)
    singleOf(::CartRepositoryImpl).bind<CartRepository>()
    singleOf(::OrderRepositoryImpl).bind<OrderRepository>()
    singleOf(::PaymentRepositoryImpl).bind<PaymentRepository>()
    singleOf(::AddressRepositoryImpl).bind<AddressRepository>()

    viewModelOf(::AuthViewModel)
    viewModelOf(::ProductViewModel)
    viewModelOf(::CartViewModel)
    viewModelOf(::RegistrationViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::OrderViewModel)
    viewModelOf(::AddressViewModel)
}
