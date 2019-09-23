package br.com.superheroes.domain.favorite

import br.com.superheroes.data.model.Character
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface FavoriteRepository {
    fun getFavorite(character: Character): Single<Character>
    fun isFavoriteSaved(character: Character): Boolean
    fun listFavorites(): Flowable<List<Character>>
    fun saveFavorite(character: Character): Completable
    fun removeFavorite(character: Character): Completable
}
