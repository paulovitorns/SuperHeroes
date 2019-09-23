package br.com.superheroes.domain.search.di

import br.com.superheroes.data.search.RemoteCharactersRepository
import br.com.superheroes.domain.search.CharactersRepository
import br.com.superheroes.library.retrofit.di.SearchCharactersEndpointModule
import dagger.Binds
import dagger.Module

@Module(includes = [SearchCharactersEndpointModule::class])
abstract class CharactersRepositoryModule {

    @Binds
    abstract fun bindRemoteCharactersRepository(
        remoteCharactersRepository: RemoteCharactersRepository
    ): CharactersRepository
}
