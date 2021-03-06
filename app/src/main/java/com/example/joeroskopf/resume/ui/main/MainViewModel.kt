package com.example.joeroskopf.resume.ui.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.text.SpannableStringBuilder
import android.util.Log
import com.example.joeroskopf.resume.android.extensions.bold
import com.example.joeroskopf.resume.db.TacoRepository
import com.example.joeroskopf.resume.model.network.TacoResponse
import com.example.joeroskopf.resume.network.TacoService
import io.reactivex.Maybe
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.rx2.await


class MainViewModel(private val tacoService: TacoService, private val tacoRepository: TacoRepository) : ViewModel() {

    companion object {
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
    }
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
     * Invalidates our local taco response
     */
    fun invalidateLocalData() {
        tacoResponse = null
    }

    /**
     * Saves the last loaded taco into our local DB
     */
    suspend fun saveTacoLocally(): Maybe<Boolean> {
        tacoResponse?.value?.let {
            val tacoEntity = tacoRepository.checkIfTacoExists(it.toTacoEntity().id).await()

            return if (tacoEntity == null) {
                //it doesn't exist! insert it
                Maybe.create { emitter ->
                    tacoRepository.saveTacoToDatabase(it).subscribe({
                        emitter.onSuccess(true)
                    }, {
                        emitter.onError(it)
                    })
                }

            } else {
                Maybe.create { emitter ->
                    //it does exist, unfavorite it
                    tacoRepository.deleteTaco(it.toTacoEntity())
                    emitter.onSuccess(false)
                }
            }

        }

        return Maybe.create {
            it.onError(RuntimeException("No Taco Response To Save"))
        }
    }

}
