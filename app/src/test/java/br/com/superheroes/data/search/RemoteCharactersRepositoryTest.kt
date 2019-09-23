package br.com.superheroes.data.search

import br.com.superheroes.data.model.CharacterDataContainer
import br.com.superheroes.data.model.CharacterDataWrapper
import br.com.superheroes.data.model.ResultNotFoundException
import br.com.superheroes.data.model.SignData
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
            signData = SignData("", "")
        )

        val result = CharacterDataWrapper(data = CharacterDataContainer(count = 10))
        val hash = "${request.signData.ts}${request.signData.pvtKey}${request.signData.apiKey}".md5()

        given {
            getCharactersEndPoint.searchCharacters(
                startWith = request.namesStartWith,
                orderBy = request.orderBy,
                limit = request.limit,
                offset = request.offset,
                apiKey = request.signData.apiKey,
                ts = request.signData.ts,
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
            apiKey = request.signData.apiKey,
            ts = request.signData.ts,
            hash = hash
        )
    }

    @Test
    fun `success on fetch characters without name reference`() {
        val request = SearchCharactersRequest(
            offset = 0,
            signData = SignData("", "")
        )

        val result = CharacterDataWrapper(data = CharacterDataContainer(count = 10))
        val hash = "${request.signData.ts}${request.signData.pvtKey}${request.signData.apiKey}".md5()

        given {
            getCharactersEndPoint.searchCharactersWithoutReference(
                orderBy = request.orderBy,
                limit = request.limit,
                offset = request.offset,
                apiKey = request.signData.apiKey,
                ts = request.signData.ts,
                hash = hash
            )
        }.willReturn { Single.just(Response.success(200, result)) }

        repository.fetchCharacters(request)
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValue(result)
            .awaitTerminalEvent()

        verify(getCharactersEndPoint).searchCharactersWithoutReference(
            orderBy = request.orderBy,
            limit = request.limit,
            offset = request.offset,
            apiKey = request.signData.apiKey,
            ts = request.signData.ts,
            hash = hash
        )
    }

    @Test
    fun `success on fetch next page characters result`() {
        val request = SearchCharactersRequest(
            namesStartWith = "Spider-Man",
            offset = 10,
            signData = SignData("", "")
        )

        val result = CharacterDataWrapper(data = CharacterDataContainer(count = 20))
        val hash = "${request.signData.ts}${request.signData.pvtKey}${request.signData.apiKey}".md5()

        given {
            getCharactersEndPoint.searchCharacters(
                startWith = request.namesStartWith,
                orderBy = request.orderBy,
                limit = request.limit,
                offset = request.offset,
                apiKey = request.signData.apiKey,
                ts = request.signData.ts,
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
            apiKey = request.signData.apiKey,
            ts = request.signData.ts,
            hash = hash
        )
    }

    @Test
    fun `fail to search DC characters on marvel API`() {
        val request = SearchCharactersRequest(
            namesStartWith = "Superman",
            offset = 10,
            signData = SignData("", "")
        )

        val result = CharacterDataWrapper()
        val hash = "${request.signData.ts}${request.signData.pvtKey}${request.signData.apiKey}".md5()

        given {
            getCharactersEndPoint.searchCharacters(
                startWith = request.namesStartWith,
                orderBy = request.orderBy,
                limit = request.limit,
                offset = request.offset,
                apiKey = request.signData.apiKey,
                ts = request.signData.ts,
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
            apiKey = request.signData.apiKey,
            ts = request.signData.ts,
            hash = hash
        )
    }
}
