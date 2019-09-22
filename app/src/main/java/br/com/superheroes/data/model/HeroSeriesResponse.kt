package br.com.superheroes.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SeriesDataWrapper(
    val code: Int = 0,
    val status: String = "",
    val copyright: String = "",
    val data: SeriesDataContainer = SeriesDataContainer()
)

@Serializable
data class SeriesDataContainer(
    val offset: Int = 0,
    val limit: Int = 0,
    val total: Int = 0,
    val count: Int = 0,
    val results: List<Series> = emptyList()
)

@Serializable
data class Series(
    val id: Int = 0,
    val title: String = "",
    val thumbnail: Thumbnail = Thumbnail()
)
