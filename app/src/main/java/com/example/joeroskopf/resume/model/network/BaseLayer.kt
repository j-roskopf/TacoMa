package com.example.joeroskopf.resume.model.network

import com.google.gson.annotations.SerializedName

data class BaseLayer(
        @SerializedName("name") var name: String?,
        @SerializedName("slug") var slug: String? = null,
        @SerializedName("recipe") var recipe: String? = null,
        @SerializedName("url") var url: String? = null
)