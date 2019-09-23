package br.com.superheroes.data.comic

import br.com.superheroes.data.model.SignData

data class HeroComicsRequest(
    val heroId: Int = 0,
    val limit: Int = 10,
    val offset: Int = 0,
    val signData: SignData
)
