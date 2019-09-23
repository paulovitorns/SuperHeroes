package br.com.superheroes.data.favorite.datasource.local

import br.com.superheroes.data.favorite.datasource.CharacterDataSource
import br.com.superheroes.data.favorite.datasource.mapper.toCharacter
import br.com.superheroes.data.favorite.datasource.mapper.toEntity
import br.com.superheroes.data.model.Character
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

class LocalCharacterDataSource @Inject constructor(
    private val characterDao: CharacterDao
) : CharacterDataSource {
    override fun fetchCharacters(): Flowable<List<Character>> {
        return characterDao.fetchCharacters().map { mapper ->
            if (mapper.isNotEmpty()) {
                mapper.map {
                    it.toCharacter()
                }
            } else {
                throw IllegalArgumentException("No favorite stored")
            }
        }
    }

    override fun fetchCharacter(character: Character): Single<Character> {
        return characterDao.fetchCharacter(character.id).map {
            it.toCharacter()
        }
    }

    override fun isCharacterSaved(character: Character): Boolean {
        return characterDao.isCharacterSaved(character.id) != null
    }

    override fun addCharacter(character: Character): Completable {
        return Completable.fromAction {
            characterDao.insertCharacter(character.toEntity())
        }
    }

    override fun deleteCharacter(character: Character): Completable {
        return Completable.create { emitter ->
            val result = characterDao.deleteCharacter(character.id)
            if (result > 0) {
                emitter.onComplete()
            } else {
                emitter.onError(IllegalArgumentException("Delete failed"))
            }
        }
    }
}
