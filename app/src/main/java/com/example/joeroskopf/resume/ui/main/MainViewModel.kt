package com.example.joeroskopf.resume.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.util.Log
import com.example.joeroskopf.resume.android.extensions.bold
import com.example.joeroskopf.resume.model.network.TacoResponse
import com.example.joeroskopf.resume.network.TacoService
import kotlinx.coroutines.experimental.async
import android.os.Build
import android.text.Html.fromHtml
import android.text.Spanned
import com.example.joeroskopf.resume.db.TacoRepository


class MainViewModel(private val tacoService: TacoService, private val tacoRepository: TacoRepository) : ViewModel() {

    /**
     *  Taco response returned from our API call
     */
    private var tacoResponse: MutableLiveData<TacoResponse>? = null

    fun getTacoResponse(): LiveData<TacoResponse> = tacoResponse ?: fetchRandomTaco()

    private fun fetchRandomTaco(): LiveData<TacoResponse> {
        tacoResponse = MutableLiveData()

        async {
            tacoService.getRandomTaco().subscribe({
                tacoResponse?.postValue(it)
            }, {
                tacoResponse?.postValue(null)
                Log.e("D", "tacoDebug - Something went wrong fetching a taco", it)
            })
        }

        return tacoResponse as MutableLiveData<TacoResponse>
    }

    /**
     * Given a [TacoResponse], returns a styled [SpannableStringBuilder]
     *
     * @param tacoResponse - response object from API
     */
    fun getHeadingFromTaco(tacoResponse: TacoResponse): SpannableStringBuilder {
        val space = " "

        return SpannableStringBuilder()
                .append(tacoResponse.baseLayer?.name).append(space)
                .bold {
                    append("with").append(space)
                }.append(tacoResponse.mixin?.name).append(space)
                .bold {
                    append("garnished with").append(space)
                }.append(tacoResponse.condiment?.name).append(space)
                .bold {
                    append("topped off with").append(space)
                }.append(tacoResponse.seasoning?.name).append(space)
                .bold {
                    append("and wrapped in delicious").append(space)
                }.append(tacoResponse.shell?.name)
    }

    /**
     * Invalidates our local taco response
     */
    fun invalidateLocalData() {
        tacoResponse = null
    }

    /**
     * Saves the last loaded taco into our local DB
     */
    fun saveTacoLocally() {
        tacoResponse?.value?.let {
            writeTacoToDatabase(it)
        }
    }

    private fun writeTacoToDatabase(tacoResponse: TacoResponse) {
        async {
            val id = tacoRepository.saveTacoToDatabase(tacoResponse)
            Log.d("D","tacoDebug - inserting with id = $id")
        }
    }
}
