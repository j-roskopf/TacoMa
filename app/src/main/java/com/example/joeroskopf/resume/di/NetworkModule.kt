package com.example.joeroskopf.resume.di

import android.app.Application
import com.example.joeroskopf.resume.BuildConfig
import com.example.joeroskopf.resume.network.TacoService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideHttpCache(application: Application): Cache {
        val cacheSize = 10 * 1024 * 1024
        return Cache(application.cacheDir, cacheSize.toLong())
    }

    @Provides
    @Singleton
    fun provideOkhttpClient(cache: Cache): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.cache(cache)
        return client.build()
    }

    @Provides
    @Singleton
    fun gson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun retrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BuildConfig.TACO_API_URL)
                .client(okHttpClient)
                .build()
    }

    @Provides
    @Singleton
    fun tacoService(retrofit: Retrofit): TacoService {
        return retrofit.create(TacoService::class.java)
    }
}