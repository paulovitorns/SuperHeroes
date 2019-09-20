package br.com.superheroes.data.search

import br.com.superheroes.data.model.CharacterDataWrapper
import br.com.superheroes.domain.search.CharactersRepository
import br.com.superheroes.library.extension.md5
import br.com.superheroes.library.retrofit.endpoint.SearchCharactersEndpoint
import io.reactivex.Single
import javax.inject.Inject

class RemoteCharactersRepository @Inject constructor(
    private val searchCharactersEndpoint: SearchCharactersEndpoint
) : CharactersRepository {
    override fun fetchCharacters(
        charactersRequest: SearchCharactersRequest
    ): Single<CharacterDataWrapper> {

        val hash =
            "${charactersRequest.ts}${charactersRequest.pvtKey}${charactersRequest.apiKey}".md5()

        return searchCharactersEndpoint.searchCharacters(
            startWith = charactersRequest.namesStartWith,
            orderBy = charactersRequest.orderBy,
            limit = charactersRequest.limit,
            offset = charactersRequest.offset,
            apiKey = charactersRequest.apiKey,
            ts = charactersRequest.ts,
            hash = hash
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
