package br.com.superheroes.domain.search

import br.com.superheroes.data.model.CharacterDataWrapper
import br.com.superheroes.data.model.SignData
import br.com.superheroes.data.search.SearchCharactersRequest
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
        schedulerProvider
    )

    @Test
    fun `success on fetch characters without a name reference to search`() {
        val request = SearchCharactersRequest(
            offset = 0,
            signData = SignData("", "")
        )

        val result = CharacterDataWrapper()

        given {
            charactersRepository.fetchCharacters(request)
        }.willReturn { Single.just(result) }

        getCharactersUserCase(request)
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValue(result.data)
            .awaitTerminalEvent()

        verify(charactersRepository).fetchCharacters(request)
    }

    @Test
    fun `success on fetch Spider-Man characters`() {
        val request = SearchCharactersRequest(
            namesStartWith = "Spider-Man",
            offset = 0,
            signData = SignData("", "")
        )

        val result = CharacterDataWrapper()

        given {
            charactersRepository.fetchCharacters(request)
        }.willReturn { Single.just(result) }

        getCharactersUserCase(request)
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValue(result.data)
            .awaitTerminalEvent()

        verify(charactersRepository).fetchCharacters(request)
    }
}
