package com.example.joeroskopf.resume.db

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.example.joeroskopf.resume.model.network.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "taco_favorites")
data class TacoEntity(
        @PrimaryKey @SerializedName("id") var id: String = "",
        @SerializedName("shell_name")
        var shell_name: String? = null,
        @SerializedName("shell_slug")
        var shell_slug: String? = null,
        @SerializedName("shell_recipe")
        var shell_recipe: String? = null,
        @SerializedName("shell_url")
        var shell_url: String? = null,
        @SerializedName("condiment_name")
        var condiment_name: String? = null,
        @SerializedName("condiment_slug")
        var condiment_slug: String? = null,
        @SerializedName("condiment_recipe")
        var condiment_recipe: String? = null,
        @SerializedName("condiment_url")
        var condiment_url: String? = null,
        @SerializedName("mixinx_name")
        var mixinx_name: String? = null,
        @SerializedName("mixinx_slug")
        var mixinx_slug: String? = null,
        @SerializedName("mixinx_recipe")
        var mixinx_recipe: String? = null,
        @SerializedName("mixinx_url")
        var mixinx_url: String? = null,
        @SerializedName("seasoning_name")
        var seasoning_name: String? = null,
        @SerializedName("seasoning_slug")
        var seasoning_slug: String? = null,
        @SerializedName("seasoning_recipe")
        var seasoning_recipe: String? = null,
        @SerializedName("seasoning_url")
        var seasoning_url: String? = null,
        @SerializedName("base_layer_name")
        var base_layer_name: String? = null,
        @SerializedName("base_layer_slug")
        var base_layer_slug: String? = null,
        @SerializedName("base_layer_recipe")
        var base_layer_recipe: String? = null,
        @SerializedName("base_layer_url")
        var base_layer_url: String? = null
)