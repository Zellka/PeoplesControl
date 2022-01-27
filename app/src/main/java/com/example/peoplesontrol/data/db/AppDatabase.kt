package com.example.peoplesontrol.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.peoplesontrol.data.db.dao.CategoryDao
import com.example.peoplesontrol.data.db.dao.ProfileDao
import com.example.peoplesontrol.data.db.dao.RequestDao
import com.example.peoplesontrol.data.model.Category
import com.example.peoplesontrol.data.model.Converters
import com.example.peoplesontrol.data.model.Profile
import com.example.peoplesontrol.data.model.Request

@Database(entities = [Category::class, Request::class, Profile::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao

    abstract fun requestDao(): RequestDao

    abstract fun profileDao(): ProfileDao
}