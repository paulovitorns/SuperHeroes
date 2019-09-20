package br.com.superheroes.data.search

import br.com.superheroes.data.model.CharacterDataWrapper
import br.com.superheroes.domain.search.CharactersRepository
import br.com.superheroes.library.retrofit.endpoint.GetCharactersEndPoint
import io.reactivex.Single
import javax.inject.Inject

class RemoteCharactersRepository @Inject constructor(
    private val getCharactersEndPoint: GetCharactersEndPoint
) : CharactersRepository {
    override fun fetchCharacters(
        charactersRequest: SearchCharactersRequest
    ): Single<CharacterDataWrapper> {
        return getCharactersEndPoint.searchCharacters(
            startWith = charactersRequest.namesStartWith,
            orderBy = charactersRequest.orderBy,
            limit = charactersRequest.limit,
            offset = charactersRequest.offset,
            apiKey = charactersRequest.apiKey
        ).map { response ->
            when (response.code()) {
                200 -> {
                    val responseBody = response.body()
                    if (responseBody == null || responseBody.data.count == 0)
                        throw ResultNotFoundException(charactersRequest.namesStartWith)

                    responseBody
                }
                else -> throw IllegalArgumentException(response.errorBody().toString())
            }
        }
    }
}
