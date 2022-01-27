package com.example.peoplesontrol.data.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Profile(
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true) val profileId: Long,
    val user_id: Long,
    val full_name: String,
    val location: String,
    val phone: String,
    val email: String,
    val requests: List<Request>,
    val is_notification_email: Boolean = false,
    val is_notification_sms: Boolean = false,
    val is_anonymous_requests: Boolean = false
) : Parcelable

@Parcelize
data class ProfilePost(
    val full_name: String,
    val location: String,
    val phone: String,
    val email: String,
    val is_notification_email: Boolean = false,
    val is_notification_sms: Boolean = false,
    val is_anonymous_requests: Boolean = false
) : Parcelable