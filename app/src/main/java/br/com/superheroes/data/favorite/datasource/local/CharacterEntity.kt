package br.com.superheroes.data.favorite.datasource.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "character")
data class CharacterEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "resourceURI") var resourceURI: String = "",
    @ColumnInfo(name = "path") var path: String = "",
    @ColumnInfo(name = "extension") var extension: String = "",
    @ColumnInfo(name = "comicsAvailable") var comicsAvailable: Int = 0,
    @ColumnInfo(name = "seriesAvailable") var seriesAvailable: Int = 0
)
