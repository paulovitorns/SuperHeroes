package br.com.superheroes.data.search

import br.com.superheroes.data.model.SignData

data class SearchCharactersRequest(
    val namesStartWith: String = "Spider-Man",
    val orderBy: String = "name",
    val limit: Int = 20,
    val offset: Int = 0,
    val signData: SignData
)
