package br.com.superheroes.data

import br.com.superheroes.data.model.CharacterDataContainer
import br.com.superheroes.data.model.CharacterDataWrapper
import br.com.superheroes.data.search.RemoteCharactersRepository
import br.com.superheroes.data.search.ResultNotFoundException
import br.com.superheroes.data.search.SearchCharactersRequest
import br.com.superheroes.library.retrofit.endpoint.GetCharactersEndPoint
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.willReturn
import io.reactivex.Single
import org.junit.Test
import retrofit2.Response

class RemoteCharactersRepositoryTest {

    private val getCharactersEndPoint = mock<GetCharactersEndPoint>()
    private val repository = RemoteCharactersRepository(getCharactersEndPoint)

    @Test
    fun `success on fetch characters`() {
        val request = SearchCharactersRequest(
            namesStartWith = "Spider-Man",
            offset = 0,
            apiKey = ""
        )

        val result = CharacterDataWrapper(data = CharacterDataContainer(count = 10))

        given {
            getCharactersEndPoint.searchCharacters(
                startWith = request.namesStartWith,
                orderBy = request.orderBy,
                limit = request.limit,
                offset = request.offset,
                apiKey = request.apiKey
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
            apiKey = request.apiKey
        )
    }

    @Test
    fun `success on fetch next page characters result`() {
        val request = SearchCharactersRequest(
            namesStartWith = "Spider-Man",
            offset = 10,
            apiKey = ""
        )

        val result = CharacterDataWrapper(data = CharacterDataContainer(count = 20))

        given {
            getCharactersEndPoint.searchCharacters(
                startWith = request.namesStartWith,
                orderBy = request.orderBy,
                limit = request.limit,
                offset = request.offset,
                apiKey = request.apiKey
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
            apiKey = request.apiKey
        )
    }

    @Test
    fun `fail to search DC characters on marvel API`() {
        val request = SearchCharactersRequest(
            namesStartWith = "Superman",
            offset = 10,
            apiKey = ""
        )

        val result = CharacterDataWrapper()

        given {
            getCharactersEndPoint.searchCharacters(
                startWith = request.namesStartWith,
                orderBy = request.orderBy,
                limit = request.limit,
                offset = request.offset,
                apiKey = request.apiKey
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
            apiKey = request.apiKey
        )
    }
}
