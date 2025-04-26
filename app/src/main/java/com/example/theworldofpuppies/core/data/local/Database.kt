package com.example.theworldofpuppies.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.theworldofpuppies.shop.product.data.local.CategoryDao
import com.example.theworldofpuppies.shop.product.data.local.CategoryEntity
import com.example.theworldofpuppies.shop.product.data.local.ProductDao
import com.example.theworldofpuppies.shop.product.data.local.ProductEntity
import com.example.theworldofpuppies.shop.product.data.utils.Converters

@Database(
    entities = [ProductEntity::class, CategoryEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    abstract val productDao: ProductDao
    abstract val categoryDao: CategoryDao
}