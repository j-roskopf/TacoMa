package com.example.joeroskopf.resume.di

import android.app.Application
import android.content.Context
import com.example.joeroskopf.resume.BuildConfig
import com.example.joeroskopf.resume.CommonHelloService
import com.example.joeroskopf.resume.ResumeApplication
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


/**
 * This is where you will inject application-wide dependencies.
 */
@Module
class AppModule {

    @Provides
    fun provideApplication(application: ResumeApplication): Application {
        return application
    }

    @Provides
    internal fun provideContext(application: ResumeApplication): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    internal fun provideCommonHelloService(): CommonHelloService {
        return CommonHelloService()
    }
}