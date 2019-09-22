package br.com.superheroes.domain.comic

import br.com.superheroes.data.comic.HeroComicsRequest
import br.com.superheroes.data.model.ComicDataWrapper
import br.com.superheroes.data.model.SignData
import br.com.superheroes.library.reactivex.TestSchedulerProvider
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.willReturn
import io.reactivex.Single
import org.junit.Test

class GetHeroComicsUserCaseTest {

    private val repository = mock<ComicsRepository>()
    private val schedulerProvider = TestSchedulerProvider()

    private val heroComicsUserCase = GetHeroComicsUserCase(
        repository,
        schedulerProvider
    )

    @Test
    fun `throw an exception trying to create a query with a invalid heroId`() {
        val request = HeroComicsRequest(
            heroId = 0,
            offset = 0,
            signData = SignData("", "")
        )

        heroComicsUserCase(request)
            .test()
            .assertError { error ->
                error.message == "Invalid heroId parameter"
            }
            .assertNotComplete()
            .assertNoValues()
            .awaitTerminalEvent()
    }

    @Test
    fun `success on fetch a Hero comics`() {
        val request = HeroComicsRequest(
            heroId = 1234,
            offset = 0,
            signData = SignData("", "")
        )

        val result = ComicDataWrapper()

        given {
            repository.fetchHeroComics(request)
        }.willReturn { Single.just(result) }

        heroComicsUserCase(request)
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValue(result.data)
            .awaitTerminalEvent()

        verify(repository).fetchHeroComics(request)
    }
}
