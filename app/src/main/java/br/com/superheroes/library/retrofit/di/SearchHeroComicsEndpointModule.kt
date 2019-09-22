package br.com.superheroes.library.retrofit.di

import br.com.superheroes.domain.config.EnvironmentConfig
import br.com.superheroes.library.injector.ActivityScope
import br.com.superheroes.library.retrofit.RetrofitFactory
import br.com.superheroes.library.retrofit.endpoint.SearchHeroComicsEndpoint
import dagger.Module
import dagger.Provides
import kotlinx.serialization.UnstableDefault

@Module
class SearchHeroComicsEndpointModule {

    @UnstableDefault
    @ActivityScope
    @Provides
    fun provideSearchHeroComicsEndPoint(
        retrofitFactory: RetrofitFactory
    ): SearchHeroComicsEndpoint {
        val baseUrl = EnvironmentConfig().baseUrl
        return retrofitFactory.create(SearchHeroComicsEndpoint::class.java, baseUrl)
    }
}
