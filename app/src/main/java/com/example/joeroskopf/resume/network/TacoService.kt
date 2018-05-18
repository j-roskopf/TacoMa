package com.example.joeroskopf.resume.network

import com.example.joeroskopf.resume.model.network.TacoResponse
import io.reactivex.Observable
import retrofit2.http.GET

interface TacoService {
    @GET("random")
    fun getRandomTaco(): Observable<TacoResponse>
}