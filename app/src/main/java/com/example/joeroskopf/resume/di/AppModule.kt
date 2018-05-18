package com.example.joeroskopf.resume.di

import android.app.Application
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.joeroskopf.resume.ResumeApplication
import com.example.joeroskopf.resume.db.AppDatabase
import com.example.joeroskopf.resume.db.TacoDao
import dagger.Module
import dagger.Provides
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

    @Provides
    @Singleton
    fun provideTacoDao(appDatabase: AppDatabase): TacoDao = appDatabase.tacoDao()

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "TACO_DATABASE")
                .fallbackToDestructiveMigration()
                .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
                .build()
    }
}