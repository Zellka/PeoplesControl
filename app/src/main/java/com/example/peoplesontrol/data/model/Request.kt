package com.example.peoplesontrol.data.model

import android.os.Parcelable
import androidx.room.*
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
    val attachments: List<MediaContent>?,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val status: String,
    val created_at: String?,
    val deleted_at: String?
) : Parcelable

@Parcelize
data class RequestPost(
    @SerializedName("id")
    val requestId: Long?,
    val parent_request_id: Long?,
    val problem_categories: Array<Int>,
    val description: String?,
    val source: String,
    val rating: Long,
    val location: String,
    val latitude: Double,
    val longitude: Double
) : Parcelable

data class RequestResponse(
    val data: List<Request>
)