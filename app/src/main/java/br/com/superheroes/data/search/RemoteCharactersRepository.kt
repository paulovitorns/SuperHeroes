package br.com.superheroes.data.search

import br.com.superheroes.data.model.CharacterDataWrapper
import br.com.superheroes.data.model.ResultNotFoundException
import br.com.superheroes.data.model.generateHash
import br.com.superheroes.domain.search.CharactersRepository
import br.com.superheroes.library.retrofit.endpoint.SearchCharactersEndpoint
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject

class RemoteCharactersRepository @Inject constructor(
    private val searchCharactersEndpoint: SearchCharactersEndpoint
) : CharactersRepository {
    override fun fetchCharacters(
        charactersRequest: SearchCharactersRequest
    ): Single<CharacterDataWrapper> {
        return if (charactersRequest.namesStartWith.isNotBlank()) {
            searchCharactersEndpoint.searchCharacters(
                startWith = charactersRequest.namesStartWith,
                orderBy = charactersRequest.orderBy,
                limit = charactersRequest.limit,
                offset = charactersRequest.offset,
                apiKey = charactersRequest.signData.apiKey,
                ts = charactersRequest.signData.ts,
                hash = charactersRequest.signData.generateHash()
            ).map { response ->
                handleApiResult(charactersRequest.namesStartWith, response)
            }
        } else {
            searchCharactersEndpoint.searchCharactersWithoutReference(
                orderBy = charactersRequest.orderBy,
                limit = charactersRequest.limit,
                offset = charactersRequest.offset,
                apiKey = charactersRequest.signData.apiKey,
                ts = charactersRequest.signData.ts,
                hash = charactersRequest.signData.generateHash()
            ).map { response ->
                handleApiResult(charactersRequest.namesStartWith, response)
            }
        }
    }

    private fun handleApiResult(
        queryString: String,
        response: Response<CharacterDataWrapper>
    ): CharacterDataWrapper {
        return when (response.code()) {
            200 -> {
                val responseBody = response.body()
                if (responseBody == null || responseBody.data.count == 0)
                    throw ResultNotFoundException(queryString)

                responseBody
            }
            else -> throw IllegalArgumentException(response.errorBody().toString())
        }
    }
}
