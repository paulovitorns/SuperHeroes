package br.com.superheroes.domain.search.di

import br.com.superheroes.data.search.RemoteCharactersRepository
import br.com.superheroes.domain.search.CharactersRepository
import br.com.superheroes.library.injector.ActivityScope
import br.com.superheroes.library.retrofit.di.GetCharactersEndpointModule
import dagger.Binds
import dagger.Module

@Module(includes = [GetCharactersEndpointModule::class])
abstract class CharactersRepositoryModule {

    @ActivityScope
    @Binds
    abstract fun bindRemoteCharactersRepository(
        remoteCharactersRepository: RemoteCharactersRepository
    ): CharactersRepository
}
