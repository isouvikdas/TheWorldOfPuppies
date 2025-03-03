package com.example.theworldofpuppies.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

//@Database(
//    entities = [ProductEntity::class, CategoryEntity::class],
//    version = 1,
//    exportSchema = false
//)
//@TypeConverters(Converters::class)
abstract class Database: RoomDatabase() {
//    abstract val productDao: ProductDao
//    abstract val categoryDao: CategoryDao
}