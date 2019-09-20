package br.com.superheroes.library.injector.modules

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module

@Module
abstract class SuperHeroesModule {

    @Binds
    abstract fun bindContext(application: Application): Context
}
