package com.example.peoplesontrol.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Category(
    @SerializedName("id")
    @PrimaryKey val categoryId: Int,
    val title: String,
    val icon: String?
) : Parcelable

data class CategoryResponse(
    val data: List<Category>
)
