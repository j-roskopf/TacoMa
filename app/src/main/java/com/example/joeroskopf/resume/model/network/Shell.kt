package com.example.joeroskopf.resume.model.network

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Shell(
        @SerializedName("name")
        var name: String? = null,
        @SerializedName("slug")
        var slug: String? = null,
        @SerializedName("recipe")
        var recipe: String? = null,
        @SerializedName("url")
        var url: String? = null
): Parcelable