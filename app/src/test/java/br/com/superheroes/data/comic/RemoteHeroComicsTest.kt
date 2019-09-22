package br.com.superheroes.data.comic

import br.com.superheroes.data.model.ComicDataContainer
import br.com.superheroes.data.model.ComicDataWrapper
import br.com.superheroes.data.model.ResultNotFoundException
import br.com.superheroes.data.model.SignData
import br.com.superheroes.library.extension.md5
import br.com.superheroes.library.retrofit.endpoint.SearchHeroComicsEndpoint
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.willReturn
import io.reactivex.Single
import org.junit.Test
import retrofit2.Response

class RemoteHeroComicsTest {

    private val endpoint = mock<SearchHeroComicsEndpoint>()
    private val repository = RemoteHeroComicRepository(endpoint)

    @Test
    fun `success on fetch hero comics`() {
        val request = HeroComicsRequest(
            heroId = 1234,
            offset = 0,
            signData = SignData("", "")
        )

        val result = ComicDataWrapper(data = ComicDataContainer(count = 10))
        val hash =
            "${request.signData.ts}${request.signData.pvtKey}${request.signData.apiKey}".md5()

        given {
            endpoint.searchHeroComics(
                characterId = request.heroId,
                limit = request.limit,
                offset = request.offset,
                apiKey = request.signData.apiKey,
                ts = request.signData.ts,
                hash = hash
            )
        }.willReturn { Single.just(Response.success(200, result)) }

        repository.fetchHeroComics(request)
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValue(result)
            .awaitTerminalEvent()

        verify(endpoint).searchHeroComics(
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
        val request = HeroComicsRequest(
            heroId = 1234,
            offset = 10,
            signData = SignData("", "")
        )

        val result = ComicDataWrapper(data = ComicDataContainer(count = 20))
        val hash =
            "${request.signData.ts}${request.signData.pvtKey}${request.signData.apiKey}".md5()

        given {
            endpoint.searchHeroComics(
                characterId = request.heroId,
                limit = request.limit,
                offset = request.offset,
                apiKey = request.signData.apiKey,
                ts = request.signData.ts,
                hash = hash
            )
        }.willReturn { Single.just(Response.success(200, result)) }

        repository.fetchHeroComics(request)
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValue(result)
            .awaitTerminalEvent()

        verify(endpoint).searchHeroComics(
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
        val request = HeroComicsRequest(
            heroId = 1234,
            offset = 10,
            signData = SignData("", "")
        )

        val result = ComicDataWrapper()
        val hash =
            "${request.signData.ts}${request.signData.pvtKey}${request.signData.apiKey}".md5()

        given {
            endpoint.searchHeroComics(
                characterId = request.heroId,
                limit = request.limit,
                offset = request.offset,
                apiKey = request.signData.apiKey,
                ts = request.signData.ts,
                hash = hash
            )
        }.willReturn { Single.just(Response.success(200, result)) }

        repository.fetchHeroComics(request)
            .test()
            .assertError(ResultNotFoundException::class.java)
            .assertNotComplete()
            .assertNoValues()
            .awaitTerminalEvent()

        verify(endpoint).searchHeroComics(
            characterId = request.heroId,
            limit = request.limit,
            offset = request.offset,
            apiKey = request.signData.apiKey,
            ts = request.signData.ts,
            hash = hash
        )
    }
}
