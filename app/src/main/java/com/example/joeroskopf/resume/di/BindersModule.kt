package com.example.joeroskopf.resume.di

import com.example.joeroskopf.resume.MainActivity
import com.example.joeroskopf.resume.ui.main.MainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Binds all sub-components within the app.
 */
@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    internal abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    internal abstract fun bindsMainFragment(): MainFragment

    // Add bindings for other sub-components here
}