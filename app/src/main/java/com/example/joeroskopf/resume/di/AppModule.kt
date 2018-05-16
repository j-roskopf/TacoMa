package com.example.joeroskopf.resume.di

import android.content.Context
import com.example.joeroskopf.resume.CommonHelloService
import com.example.joeroskopf.resume.ResumeApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * This is where you will inject application-wide dependencies.
 */
@Module
class AppModule {

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