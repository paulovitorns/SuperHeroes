package br.com.superheroes.data

import br.com.superheroes.data.model.CharacterDataContainer
import br.com.superheroes.data.model.CharacterDataWrapper
import br.com.superheroes.data.search.RemoteCharactersRepository
import br.com.superheroes.data.search.ResultNotFoundException
import br.com.superheroes.data.search.SearchCharactersRequest
import br.com.superheroes.library.extension.md5
import br.com.superheroes.library.retrofit.endpoint.SearchCharactersEndpoint
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.willReturn
import io.reactivex.Single
import org.junit.Test
import retrofit2.Response

class RemoteCharactersRepositoryTest {

    private val getCharactersEndPoint = mock<SearchCharactersEndpoint>()
    private val repository = RemoteCharactersRepository(getCharactersEndPoint)

    @Test
    fun `success on fetch characters`() {
        val request = SearchCharactersRequest(
            namesStartWith = "Spider-Man",
            offset = 0,
            apiKey = "",
            pvtKey = ""
        )

        val result = CharacterDataWrapper(data = CharacterDataContainer(count = 10))
        val hash = "${request.ts}${request.pvtKey}${request.apiKey}".md5()

        given {
            getCharactersEndPoint.searchCharacters(
                startWith = request.namesStartWith,
                orderBy = request.orderBy,
                limit = request.limit,
                offset = request.offset,
                apiKey = request.apiKey,
                ts = request.ts,
                hash = hash
            )
        }.willReturn { Single.just(Response.success(200, result)) }

        repository.fetchCharacters(request)
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValue(result)
            .awaitTerminalEvent()

        verify(getCharactersEndPoint).searchCharacters(
            startWith = request.namesStartWith,
            orderBy = request.orderBy,
            limit = request.limit,
            offset = request.offset,
            apiKey = request.apiKey,
            ts = request.ts,
            hash = hash
        )
    }

    @Test
    fun `success on fetch next page characters result`() {
        val request = SearchCharactersRequest(
            namesStartWith = "Spider-Man",
            offset = 10,
            apiKey = "",
            pvtKey = ""
        )

        val result = CharacterDataWrapper(data = CharacterDataContainer(count = 20))
        val hash = "${request.ts}${request.pvtKey}${request.apiKey}".md5()

        given {
            getCharactersEndPoint.searchCharacters(
                startWith = request.namesStartWith,
                orderBy = request.orderBy,
                limit = request.limit,
                offset = request.offset,
                apiKey = request.apiKey,
                ts = request.ts,
                hash = hash
            )
        }.willReturn { Single.just(Response.success(200, result)) }

        repository.fetchCharacters(request)
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValue(result)
            .awaitTerminalEvent()

        verify(getCharactersEndPoint).searchCharacters(
            startWith = request.namesStartWith,
            orderBy = request.orderBy,
            limit = request.limit,
            offset = request.offset,
            apiKey = request.apiKey,
            ts = request.ts,
            hash = hash
        )
    }

    @Test
    fun `fail to search DC characters on marvel API`() {
        val request = SearchCharactersRequest(
            namesStartWith = "Superman",
            offset = 10,
            apiKey = "",
            pvtKey = ""
        )

        val result = CharacterDataWrapper()
        val hash = "${request.ts}${request.pvtKey}${request.apiKey}".md5()

        given {
            getCharactersEndPoint.searchCharacters(
                startWith = request.namesStartWith,
                orderBy = request.orderBy,
                limit = request.limit,
                offset = request.offset,
                apiKey = request.apiKey,
                ts = request.ts,
                hash = hash
            )
        }.willReturn { Single.just(Response.success(200, result)) }

        repository.fetchCharacters(request)
            .test()
            .assertError(ResultNotFoundException::class.java)
            .assertNotComplete()
            .assertNoValues()
            .awaitTerminalEvent()

        verify(getCharactersEndPoint).searchCharacters(
            startWith = request.namesStartWith,
            orderBy = request.orderBy,
            limit = request.limit,
            offset = request.offset,
            apiKey = request.apiKey,
            ts = request.ts,
            hash = hash
        )
    }
}
