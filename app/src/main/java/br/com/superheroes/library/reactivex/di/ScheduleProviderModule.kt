package br.com.superheroes.library.reactivex.di

import br.com.superheroes.library.reactivex.DefaultSchedulerProvider
import br.com.superheroes.library.reactivex.SchedulerProvider
import dagger.Binds
import dagger.Module

@Module
abstract class ScheduleProviderModule {

    @Binds
    abstract fun bindDefaultScheduleProvider(
        defaultSchedulerProvider: DefaultSchedulerProvider
    ): SchedulerProvider
}
