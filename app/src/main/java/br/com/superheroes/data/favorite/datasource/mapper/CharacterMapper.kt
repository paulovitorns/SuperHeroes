package br.com.superheroes.data.favorite.datasource.mapper

import br.com.superheroes.data.favorite.datasource.local.CharacterEntity
import br.com.superheroes.data.model.Character
import br.com.superheroes.data.model.ComicList
import br.com.superheroes.data.model.SeriesList
import br.com.superheroes.data.model.Thumbnail

fun CharacterEntity.toCharacter(): Character = Character(
    id,
    name,
    description,
    resourceURI,
    thumbnail = Thumbnail(this.path, this.extension),
    comics = ComicList(available = comicsAvailable),
    series = SeriesList(available = seriesAvailable)
)

fun Character.toEntity(): CharacterEntity {
    return CharacterEntity(
        id,
        name,
        description,
        resourceURI,
        thumbnail.path,
        thumbnail.extension,
        comics.available,
        series.available
    )
}
