package br.com.superheroes.library.retrofit.di

import br.com.superheroes.domain.config.EnvironmentConfig
import br.com.superheroes.library.injector.ActivityScope
import br.com.superheroes.library.retrofit.RetrofitFactory
import br.com.superheroes.library.retrofit.endpoint.SearchCharactersEndpoint
import dagger.Module
import dagger.Provides
import kotlinx.serialization.UnstableDefault

@Module
class GetCharactersEndpointModule {

    @UnstableDefault
    @ActivityScope
    @Provides
    fun provideGetCharactersEndPoint(
        retrofitFactory: RetrofitFactory
    ): SearchCharactersEndpoint {
        val baseUrl = EnvironmentConfig().baseUrl
        return retrofitFactory.create(SearchCharactersEndpoint::class.java, baseUrl)
    }
}
