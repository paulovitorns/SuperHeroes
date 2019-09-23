package br.com.superheroes.data.favorite.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface CharacterDao {

    @Query("SELECT * FROM character ORDER BY name ASC")
    fun fetchCharacters(): Flowable<List<CharacterEntity>>

    @Query("SELECT * FROM character WHERE id = :characterId")
    fun fetchCharacter(characterId: Int): Single<CharacterEntity>

    @Query("SELECT * FROM character WHERE id = :characterId")
    fun isCharacterSaved(characterId: Int): CharacterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCharacter(character: CharacterEntity)

    @Query("DELETE FROM character WHERE id = :characterId")
    fun deleteCharacter(characterId: Int): Int
}
