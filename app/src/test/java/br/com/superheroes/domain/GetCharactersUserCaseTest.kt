package br.com.superheroes.domain

import br.com.superheroes.data.model.CharacterDataWrapper
import br.com.superheroes.data.search.SearchCharactersRequest
import br.com.superheroes.domain.config.EnvironmentConfig
import br.com.superheroes.domain.search.CharactersRepository
import br.com.superheroes.domain.search.GetCharactersUserCase
import br.com.superheroes.library.reactivex.TestSchedulerProvider
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.willReturn
import io.reactivex.Single
import org.junit.Test

class GetCharactersUserCaseTest {

    private val charactersRepository = mock<CharactersRepository>()
    private val schedulerProvider = TestSchedulerProvider()

    private val getCharactersUserCase = GetCharactersUserCase(
        charactersRepository,
        EnvironmentConfig(),
        schedulerProvider
    )

    @Test
    fun `throw an exception trying to create a query with a blank namesStartWith`() {
        val request = SearchCharactersRequest(
            namesStartWith = "",
            offset = 0,
            apiKey = ""
        )

        getCharactersUserCase(request)
            .test()
            .assertError { error ->
                error.message == "Missing name parameter"
            }
            .assertNotComplete()
            .assertNoValues()
            .awaitTerminalEvent()
    }

    @Test
    fun `success on fetch Spider-Man characters`() {
        val request = SearchCharactersRequest(
            namesStartWith = "Spider-Man",
            offset = 0,
            apiKey = "656ace3b6053ed496242e3d3f7dca830"
        )

        val result = CharacterDataWrapper()

        given {
            charactersRepository.fetchCharacters(request)
        }.willReturn { Single.just(result) }

        getCharactersUserCase(request)
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValue(result.data.results)
            .awaitTerminalEvent()

        verify(charactersRepository).fetchCharacters(request)
    }
}
