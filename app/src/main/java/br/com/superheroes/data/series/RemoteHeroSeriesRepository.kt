package br.com.superheroes.data.series

import br.com.superheroes.data.model.ResultNotFoundException
import br.com.superheroes.data.model.SeriesDataWrapper
import br.com.superheroes.data.model.generateHash
import br.com.superheroes.domain.series.SeriesRepository
import br.com.superheroes.library.retrofit.endpoint.SearchHeroSeriesEndpoint
import io.reactivex.Single
import javax.inject.Inject

class RemoteHeroSeriesRepository @Inject constructor(
    private val searchHeroSeriesEndpoint: SearchHeroSeriesEndpoint
) : SeriesRepository {

    override fun fetchHeroSeries(request: HeroSeriesRequest): Single<SeriesDataWrapper> {
        return searchHeroSeriesEndpoint.searchHeroSeries(
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
