package br.com.superheroes.data.series

import br.com.superheroes.data.model.ResultNotFoundException
import br.com.superheroes.data.model.SeriesDataContainer
import br.com.superheroes.data.model.SeriesDataWrapper
import br.com.superheroes.data.model.SignData
import br.com.superheroes.library.extension.md5
import br.com.superheroes.library.retrofit.endpoint.SearchHeroSeriesEndpoint
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.willReturn
import io.reactivex.Single
import org.junit.Test
import retrofit2.Response

class RemoteHeroSeriesTest {

    private val endpoint = mock<SearchHeroSeriesEndpoint>()
    private val repository = RemoteHeroSeriesRepository(endpoint)

    @Test
    fun `success on fetch hero comics`() {
        val request = HeroSeriesRequest(
            heroId = 1234,
            offset = 0,
            signData = SignData("", "")
        )

        val result = SeriesDataWrapper(data = SeriesDataContainer(count = 10))
        val hash =
            "${request.signData.ts}${request.signData.pvtKey}${request.signData.apiKey}".md5()

        given {
            endpoint.searchHeroSeries(
                characterId = request.heroId,
                limit = request.limit,
                offset = request.offset,
                apiKey = request.signData.apiKey,
                ts = request.signData.ts,
                hash = hash
            )
        }.willReturn { Single.just(Response.success(200, result)) }

        repository.fetchHeroSeries(request)
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValue(result)
            .awaitTerminalEvent()

        verify(endpoint).searchHeroSeries(
            characterId = request.heroId,
            limit = request.limit,
            offset = request.offset,
            apiKey = request.signData.apiKey,
            ts = request.signData.ts,
            hash = hash
        )
    }

    @Test
    fun `success on fetch next page hero comics result`() {
        val request = HeroSeriesRequest(
            heroId = 1234,
            offset = 10,
            signData = SignData("", "")
        )

        val result = SeriesDataWrapper(data = SeriesDataContainer(count = 20))
        val hash =
            "${request.signData.ts}${request.signData.pvtKey}${request.signData.apiKey}".md5()

        given {
            endpoint.searchHeroSeries(
                characterId = request.heroId,
                limit = request.limit,
                offset = request.offset,
                apiKey = request.signData.apiKey,
                ts = request.signData.ts,
                hash = hash
            )
        }.willReturn { Single.just(Response.success(200, result)) }

        repository.fetchHeroSeries(request)
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValue(result)
            .awaitTerminalEvent()

        verify(endpoint).searchHeroSeries(
            characterId = request.heroId,
            limit = request.limit,
            offset = request.offset,
            apiKey = request.signData.apiKey,
            ts = request.signData.ts,
            hash = hash
        )
    }

    @Test
    fun `fail to search comics from a hero that doesn't have published comics`() {
        val request = HeroSeriesRequest(
            heroId = 1234,
            offset = 10,
            signData = SignData("", "")
        )

        val result = SeriesDataWrapper()
        val hash =
            "${request.signData.ts}${request.signData.pvtKey}${request.signData.apiKey}".md5()

        given {
            endpoint.searchHeroSeries(
                characterId = request.heroId,
                limit = request.limit,
                offset = request.offset,
                apiKey = request.signData.apiKey,
                ts = request.signData.ts,
                hash = hash
            )
        }.willReturn { Single.just(Response.success(200, result)) }

        repository.fetchHeroSeries(request)
            .test()
            .assertError(ResultNotFoundException::class.java)
            .assertNotComplete()
            .assertNoValues()
            .awaitTerminalEvent()

        verify(endpoint).searchHeroSeries(
            characterId = request.heroId,
            limit = request.limit,
            offset = request.offset,
            apiKey = request.signData.apiKey,
            ts = request.signData.ts,
            hash = hash
        )
    }
}
