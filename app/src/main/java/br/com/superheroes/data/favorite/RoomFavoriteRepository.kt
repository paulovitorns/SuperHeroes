package br.com.superheroes.data.favorite

import br.com.superheroes.data.favorite.datasource.local.LocalCharacterDataSource
import br.com.superheroes.data.model.Character
import br.com.superheroes.domain.favorite.FavoriteRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

class RoomFavoriteRepository @Inject constructor(
    private val localDataSource: LocalCharacterDataSource
) : FavoriteRepository {

    override fun getFavorite(character: Character): Single<Character> {
        return localDataSource.fetchCharacter(character)
    }

    override fun isFavoriteSaved(character: Character): Boolean =
        localDataSource.isCharacterSaved(character)

    override fun listFavorites(): Flowable<List<Character>> {
        return localDataSource.fetchCharacters()
    }

    override fun saveFavorite(character: Character): Completable {
        return localDataSource.addCharacter(character)
    }

    override fun removeFavorite(character: Character): Completable {
        return localDataSource.deleteCharacter(character)
    }
}
