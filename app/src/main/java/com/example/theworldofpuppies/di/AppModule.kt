package com.example.theworldofpuppies.di

import android.content.Context
import com.example.theworldofpuppies.auth.data.AuthApiImpl
import com.example.theworldofpuppies.auth.presentation.login.AuthEventManager
import com.example.theworldofpuppies.auth.domain.AuthApi
import com.example.theworldofpuppies.auth.presentation.login.LoginViewModel
import com.example.theworldofpuppies.auth.presentation.register.RegistrationViewModel
import com.example.theworldofpuppies.core.data.networking.HttpClientFactory
import com.example.theworldofpuppies.core.domain.UserRepository
import com.example.theworldofpuppies.core.presentation.AuthViewModel
import io.ktor.client.engine.cio.CIO
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { HttpClientFactory.create(CIO.create()) }
    single { androidContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE) }
//    single {
//        Room.databaseBuilder(
//            androidContext(),
//            Database::class.java,
//            "product_database"
//        ).fallbackToDestructiveMigration()
//            .build()
//    }
//    single { get<Database>().productDao }
//    single { get<Database>().categoryDao }
//    singleOf(::ProductApi)
    singleOf(::UserRepository)
    singleOf(::AuthEventManager)
//    singleOf(::ProductRepositoryImpl).bind<ProductRepository>()
    singleOf(::AuthApiImpl).bind<AuthApi>()
//    singleOf(::CartApi)
//    singleOf(::CategoryRepositoryImpl).bind<CategoryRepository>()
//    singleOf(::CartRepositoryImpl).bind<CartRepository>()

    viewModelOf(::AuthViewModel)
//    viewModelOf(::LoginViewModel)
//    viewModelOf(::AccountDetailViewModel)
//    viewModelOf(::ProductViewModel)
//    viewModelOf(::CartViewModel)
    viewModelOf(::RegistrationViewModel)
    viewModelOf(::LoginViewModel)
}
