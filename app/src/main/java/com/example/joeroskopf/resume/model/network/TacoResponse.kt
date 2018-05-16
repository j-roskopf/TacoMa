package com.example.joeroskopf.resume.model.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TacoResponse(@SerializedName("shell")
                        var shell: Shell? = null,
                        @SerializedName("seasoning")
                        var seasoning: Seasoning? = null,
                        @SerializedName("mixin")
                        var mixin: Mixin? = null,
                        @SerializedName("base_layer")
                        var baseLayer: BaseLayer? = null,
                        @SerializedName("condiment")
                        var condiment: Condiment? = null)