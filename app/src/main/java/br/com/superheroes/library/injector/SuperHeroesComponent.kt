package br.com.superheroes.library.injector

import android.app.Application
import br.com.superheroes.SuperHeroesApp
import br.com.superheroes.library.injector.modules.SuperHeroesModule
import br.com.superheroes.library.reactivex.di.ScheduleProviderModule
import br.com.superheroes.screen.di.ActivityBindingModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        SuperHeroesModule::class,
        ScheduleProviderModule::class,
        ActivityBindingModule::class,
        AndroidSupportInjectionModule::class
    ]
)
interface SuperHeroesComponent : AndroidInjector<SuperHeroesApp> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): SuperHeroesComponent
    }
}
