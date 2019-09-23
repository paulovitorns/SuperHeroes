package br.com.superheroes.domain.series.di

import br.com.superheroes.data.series.RemoteHeroSeriesRepository
import br.com.superheroes.domain.series.SeriesRepository
import br.com.superheroes.library.injector.ActivityScope
import br.com.superheroes.library.retrofit.di.SearchHeroSeriesEndpointModule
import dagger.Binds
import dagger.Module

@Module(includes = [SearchHeroSeriesEndpointModule::class])
abstract class HeroSeriesRepositoryModule {

    @ActivityScope
    @Binds
    abstract fun bindRemoteHeroSeriesRepository(
        remoteHeroSeriesRepository: RemoteHeroSeriesRepository
    ): SeriesRepository
}
