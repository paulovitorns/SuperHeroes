package br.com.superheroes.screen.di

import br.com.superheroes.domain.search.di.CharactersRepositoryModule
import br.com.superheroes.library.injector.ActivityScope
import br.com.superheroes.screen.detail.HeroActivity
import br.com.superheroes.screen.home.HomeActivity
import br.com.superheroes.screen.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun splashActivity(): SplashActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [CharactersRepositoryModule::class])
    abstract fun homeActivity(): HomeActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun heroActivity(): HeroActivity
}
