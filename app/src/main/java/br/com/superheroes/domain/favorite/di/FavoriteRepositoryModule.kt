package br.com.superheroes.domain.favorite.di

import android.content.Context
import androidx.room.Room
import br.com.superheroes.data.favorite.RoomFavoriteRepository
import br.com.superheroes.data.favorite.database.CharactersDataBase
import br.com.superheroes.data.favorite.datasource.CharacterDataSource
import br.com.superheroes.data.favorite.datasource.local.CharacterDao
import br.com.superheroes.data.favorite.datasource.local.LocalCharacterDataSource
import br.com.superheroes.domain.favorite.FavoriteRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [CharactersDatabaseModule::class])
abstract class FavoriteRepositoryModule {

    @Binds
    abstract fun bindCharacterLocalDataSource(
        localCharacterDataSource: LocalCharacterDataSource
    ): CharacterDataSource

    @Binds
    abstract fun bindFavoriteRespository(
        roomFavoriteRepository: RoomFavoriteRepository
    ): FavoriteRepository
}

@Module
class CharactersDatabaseModule {

    @Provides
    fun providesCharactersDB(context: Context): CharactersDataBase = Room.databaseBuilder(
        context,
        CharactersDataBase::class.java,
        DB_NAME
    ).build()

    @Provides
    fun provideCharacterDao(charactersDataBase: CharactersDataBase): CharacterDao =
        charactersDataBase.charactersDao()
}

private const val DB_NAME = "SuperHeroApp.db"
