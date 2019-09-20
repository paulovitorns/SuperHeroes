package br.com.superheroes.domain.search

import br.com.superheroes.data.model.CharacterDataWrapper
import br.com.superheroes.data.search.SearchCharactersRequest
import io.reactivex.Single

interface CharactersRepository {
    fun fetchCharacters(charactersRequest: SearchCharactersRequest): Single<CharacterDataWrapper>
}
