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
) {
    /**
     * Converts a [TacoEntity] to a [TacoResponse]
     */
    fun toTacoResponse(): TacoResponse {
        val shell = Shell(name = shell_name, slug = shell_slug, recipe = shell_recipe, url = shell_url)
        val seasoning = Seasoning(name = seasoning_name, slug = seasoning_slug, recipe = seasoning_recipe, url = seasoning_url)
        val mixin = Mixin(name = mixinx_name, slug = mixinx_slug, recipe = mixinx_recipe, url = mixinx_url)
        val baseLayer = BaseLayer(name = base_layer_name, slug = base_layer_slug, recipe = base_layer_recipe, url = base_layer_url)
        val condiment = Condiment(name = condiment_name, slug = condiment_slug, recipe = condiment_recipe, url = condiment_url)
        return TacoResponse(
                shell = shell,
                seasoning = seasoning,
                mixin = mixin,
                baseLayer = baseLayer,
                condiment = condiment
        )


    }
}