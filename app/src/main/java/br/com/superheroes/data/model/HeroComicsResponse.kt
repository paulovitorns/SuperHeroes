package br.com.superheroes.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ComicDataWrapper(
    val code: Int = 0,
    val status: String = "",
    val copyright: String = "",
    val data: ComicDataContainer = ComicDataContainer()
)

@Serializable
data class ComicDataContainer(
    val offset: Int = 0,
    val limit: Int = 0,
    val total: Int = 0,
    val count: Int = 0,
    val results: List<Comic> = emptyList()
)

@Serializable
data class Comic(
    val id: Int = 0,
    val title: String = "",
    val thumbnail: Thumbnail = Thumbnail()
)
