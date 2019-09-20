package br.com.superheroes

import br.com.superheroes.library.injector.DaggerSuperHeroesComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class SuperHeroesApp : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerSuperHeroesComponent.builder()
            .application(this)
            .build()
    }
}
