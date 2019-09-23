package br.com.superheroes.data.favorite.datasource

import br.com.superheroes.data.model.Character
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface CharacterDataSource {
    fun fetchCharacters(): Flowable<List<Character>>
    fun fetchCharacter(character: Character): Single<Character>
    fun isCharacterSaved(character: Character): Boolean
    fun addCharacter(character: Character): Completable
    fun deleteCharacter(character: Character): Completable
}
