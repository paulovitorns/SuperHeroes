package br.com.superheroes.data.series

import br.com.superheroes.data.model.SignData

data class HeroSeriesRequest(
    val heroId: Int = 0,
    val order: String = "-startYear",
    val limit: Int = 10,
    val offset: Int = 0,
    val signData: SignData
)
