package com.example.peoplesontrol.data.model

import android.os.Parcelable
import androidx.room.*
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Request(
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    val requestId: Long,
    val parent_request_id: Long?,
    val problem_categories: List<Category>,
    val description: String?,
    val rating: Long,
    //val attachments: MediaContent?,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val status: String,
    val created_at: String?,
    val deleted_at: String?
) : Parcelable

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
}

@Parcelize
data class RequestPost(
    @SerializedName("id")
    val requestId: Long?,
    val parent_request_id: Long?,
    val problem_categories: Array<Int>,
    val description: String?,
    val source: String,
    val rating: Long,
    //val attachments: MediaContent?,
    val location: String,
    val latitude: Double,
    val longitude: Double
) : Parcelable

@Parcelize
data class RequestUpdate(
    val problem_categories: Array<Int>,
    val description: String?,
    val source: String,
    //val attachments: MediaContent?,
    val latitude: Double,
    val longitude: Double
) : Parcelable


data class RequestResponse(
    val data: List<Request>
)