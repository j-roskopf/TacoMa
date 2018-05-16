package com.example.joeroskopf.resume.di

import android.app.Application
import android.net.Network
import com.example.joeroskopf.resume.ResumeApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    NetworkModule::class,
    BuildersModule::class
])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(resumeApplication: ResumeApplication): AppComponent.Builder
        fun build(): AppComponent
    }

    fun inject(resumeApplication: ResumeApplication)
}
