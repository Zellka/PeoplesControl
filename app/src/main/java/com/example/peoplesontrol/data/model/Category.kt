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
    val created_at: String,
    val deleted_at: String?,
    val updated_at: String,
    val hash_tag: String,
    val icon: String?,
    val is_active: Boolean,
    val is_visible: Boolean,
    val mnemonic_name: String,
    val rating: Int
) : Parcelable

data class CategoryResponse(
    val data: List<Category>
)
