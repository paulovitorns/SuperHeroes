package br.com.superheroes.data.comic

import br.com.superheroes.data.model.ComicDataWrapper
import br.com.superheroes.data.model.ResultNotFoundException
import br.com.superheroes.data.model.generateHash
import br.com.superheroes.domain.comic.ComicsRepository
import br.com.superheroes.library.retrofit.endpoint.SearchHeroComicsEndpoint
import io.reactivex.Single
import javax.inject.Inject

class RemoteHeroComicRepository @Inject constructor(
    private val searchHeroComicsEndpoint: SearchHeroComicsEndpoint
) : ComicsRepository {

    override fun fetchHeroComics(request: HeroComicsRequest): Single<ComicDataWrapper> {
        return searchHeroComicsEndpoint.searchHeroComics(
            characterId = request.heroId,
            limit = request.limit,
            offset = request.offset,
            apiKey = request.signData.apiKey,
            ts = request.signData.ts,
            hash = request.signData.generateHash()
        ).map { response ->
            when (response.code()) {
                200 -> {
                    val responseBody = response.body()
                    if (responseBody == null || responseBody.data.count == 0)
                        throw ResultNotFoundException(request.heroId.toString())

                    responseBody
                }
                else -> throw IllegalArgumentException(response.errorBody().toString())
            }
        }
    }
}
