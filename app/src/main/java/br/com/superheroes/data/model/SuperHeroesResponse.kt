package br.com.superheroes.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CharacterDataWrapper(
    val code: Int = 0,
    val status: String = "",
    val copyright: String = "",
    val data: CharacterDataContainer = CharacterDataContainer()
)

@Serializable
data class CharacterDataContainer(
    val offset: Int = 0,
    val limit: Int = 0,
    val total: Int = 0,
    val count: Int = 0,
    val results: List<Character> = emptyList()
)

@Serializable
data class Character(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val resourceURI: String = "",
    val urls: List<Url> = emptyList(),
    val thumbnail: Thumbnail = Thumbnail(),
    val comics: ComicList = ComicList(),
    val series: SeriesList = SeriesList(),
    var isFavorite: Boolean = false
)

@Serializable
data class Url(
    val type: String = "",
    val url: String = ""
)

@Serializable
data class Thumbnail(
    val path: String = "",
    val extension: String = ""
)

@Serializable
data class ComicList(
    val available: Int = 0,
    val returned: Int = 0,
    val collectionURI: String = "",
    val items: List<ComicSummary> = emptyList()
)

@Serializable
data class ComicSummary(
    val resourceURI: String = "",
    val name: String = ""
)

@Serializable
data class SeriesList(
    val available: Int = 0,
    val returned: Int = 0,
    val collectionURI: String = "",
    val items: List<SeriesSummary> = emptyList()
)

@Serializable
data class SeriesSummary(
    val resourceURI: String = "",
    val name: String = ""
)