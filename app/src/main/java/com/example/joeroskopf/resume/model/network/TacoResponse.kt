package com.example.joeroskopf.resume.model.network

import android.os.Parcelable
import android.util.Log
import com.example.joeroskopf.resume.db.TacoEntity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TacoResponse(@SerializedName("shell")
                        var shell: Shell? = null,
                        @SerializedName("seasoning")
                        var seasoning: Seasoning? = null,
                        @SerializedName("mixin")
                        var mixin: Mixin? = null,
                        @SerializedName("base_layer")
                        var baseLayer: BaseLayer? = null,
                        @SerializedName("condiment")
                        var condiment: Condiment? = null): Parcelable {

    fun toTacoEntity(): TacoEntity {
        //id is concat of shell_slug_seasoning_slug_mixinx_slug_base_layer_slug_condiment_slug
        return TacoEntity(
                id = "${shell?.slug}_${seasoning?.slug}_${mixin?.slug}_${baseLayer?.slug}_${condiment?.slug}",
                shell_name = shell?.name,
                shell_recipe = shell?.recipe,
                shell_slug = shell?.slug,
                shell_url = shell?.url,
                seasoning_name = seasoning?.name,
                seasoning_recipe = seasoning?.recipe,
                seasoning_slug = seasoning?.slug,
                seasoning_url = seasoning?.url,
                mixinx_name = mixin?.name,
                mixinx_recipe = mixin?.recipe,
                mixinx_slug = mixin?.slug,
                mixinx_url = mixin?.url,
                base_layer_name = baseLayer?.name,
                base_layer_recipe = baseLayer?.recipe,
                base_layer_slug = baseLayer?.slug,
                base_layer_url = baseLayer?.url,
                condiment_name = condiment?.name,
                condiment_recipe = condiment?.recipe,
                condiment_slug = condiment?.slug,
                condiment_url = condiment?.url
        )
    }
}