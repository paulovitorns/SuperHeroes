package br.com.superheroes.library.retrofit.di

import br.com.superheroes.domain.config.EnvironmentConfig
import br.com.superheroes.library.injector.ActivityScope
import br.com.superheroes.library.retrofit.RetrofitFactory
import br.com.superheroes.library.retrofit.endpoint.SearchHeroSeriesEndpoint
import dagger.Module
import dagger.Provides
import kotlinx.serialization.UnstableDefault

@Module
class SearchHeroSeriesEndpointModule {

    @UnstableDefault
    @ActivityScope
    @Provides
    fun provideSearchHeroSeriesEndPoint(
        retrofitFactory: RetrofitFactory
    ): SearchHeroSeriesEndpoint {
        val baseUrl = EnvironmentConfig().baseUrl
        return retrofitFactory.create(SearchHeroSeriesEndpoint::class.java, baseUrl)
    }
}
