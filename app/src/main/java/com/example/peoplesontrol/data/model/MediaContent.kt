package com.example.peoplesontrol.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MediaContent(
    val type: String,
    val url: String
) : Parcelable