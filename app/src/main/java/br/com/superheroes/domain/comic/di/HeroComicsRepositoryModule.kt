package br.com.superheroes.domain.comic.di

import br.com.superheroes.data.comic.RemoteHeroComicRepository
import br.com.superheroes.domain.comic.ComicsRepository
import br.com.superheroes.library.injector.ActivityScope
import br.com.superheroes.library.retrofit.di.SearchHeroComicsEndpointModule
import dagger.Binds
import dagger.Module

@Module(includes = [SearchHeroComicsEndpointModule::class])
abstract class HeroComicsRepositoryModule {

    @ActivityScope
    @Binds
    abstract fun bindRemoteHeroComicRepository(
        remoteHeroComicRepository: RemoteHeroComicRepository
    ): ComicsRepository
}
