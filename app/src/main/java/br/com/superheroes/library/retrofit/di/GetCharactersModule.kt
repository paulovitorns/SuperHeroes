package br.com.superheroes.library.retrofit.di

import br.com.superheroes.domain.config.EnvironmentConfig
import br.com.superheroes.library.injector.ActivityScope
import br.com.superheroes.library.retrofit.RetrofitFactory
import br.com.superheroes.library.retrofit.endpoint.GetCharactersEndPoint
import dagger.Module
import dagger.Provides
import kotlinx.serialization.UnstableDefault

@Module
class GetCharactersModule {

    @UnstableDefault
    @ActivityScope
    @Provides
    fun provideGetCharactersEndPoint(
        retrofitFactory: RetrofitFactory
    ): GetCharactersEndPoint {
        val baseUrl = EnvironmentConfig().baseUrl
        return retrofitFactory.create(GetCharactersEndPoint::class.java, baseUrl)
    }
}
