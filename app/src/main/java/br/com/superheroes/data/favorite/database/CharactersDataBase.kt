package br.com.superheroes.data.favorite.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.superheroes.data.favorite.datasource.local.CharacterEntity
import br.com.superheroes.data.favorite.datasource.local.CharacterDao

@Database(
    entities = [CharacterEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CharactersDataBase : RoomDatabase() {
    abstract fun charactersDao(): CharacterDao
}
