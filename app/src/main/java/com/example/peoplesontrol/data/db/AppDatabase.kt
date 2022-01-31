package com.example.peoplesontrol.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.peoplesontrol.data.db.dao.CategoryDao
import com.example.peoplesontrol.data.db.dao.ProfileDao
import com.example.peoplesontrol.data.db.dao.RequestDao
import com.example.peoplesontrol.data.model.*
import com.google.gson.Gson

@Database(entities = [Category::class, Request::class, Profile::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao

    abstract fun requestDao(): RequestDao

    abstract fun profileDao(): ProfileDao
}

class Converters {

    @TypeConverter
    fun categoriesToJson(value: List<Category>) = Gson().toJson(value)

    @TypeConverter
    fun jsonToCategories(value: String) =
        Gson().fromJson(value, Array<Category>::class.java).toList()

    @TypeConverter
    fun requestsToJson(value: List<Request>) = Gson().toJson(value)

    @TypeConverter
    fun jsonToRequests(value: String) = Gson().fromJson(value, Array<Request>::class.java).toList()

    @TypeConverter
    fun mediaToJson(value: List<MediaContent>) = Gson().toJson(value)

    @TypeConverter
    fun jsonToMedia(value: String) = Gson().fromJson(value, Array<MediaContent>::class.java).toList()
}