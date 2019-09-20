package br.com.superheroes.screen.di

import br.com.superheroes.library.injector.ActivityScope
import br.com.superheroes.screen.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun splashActivity(): SplashActivity
}
