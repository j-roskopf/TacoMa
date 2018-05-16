package com.example.joeroskopf.resume.model.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Mixin(@SerializedName("name")
                 var name: String? = null,
                 @SerializedName("slug")
                 var slug: String? = null,
                 @SerializedName("recipe")
                 var recipe: String? = null,
                 @SerializedName("url")
                 var url: String? = null)