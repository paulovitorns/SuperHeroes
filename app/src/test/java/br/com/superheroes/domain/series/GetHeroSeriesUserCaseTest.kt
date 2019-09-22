package br.com.superheroes.domain.series

import br.com.superheroes.data.model.SeriesDataWrapper
import br.com.superheroes.data.model.SignData
import br.com.superheroes.data.series.HeroSeriesRequest
import br.com.superheroes.library.reactivex.TestSchedulerProvider
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.willReturn
import io.reactivex.Single
import org.junit.Test

class GetHeroSeriesUserCaseTest {

    private val repository = mock<SeriesRepository>()
    private val schedulerProvider = TestSchedulerProvider()

    private val heroComicsUserCase = GetHeroSeriesUserCase(
        repository,
        schedulerProvider
    )

    @Test
    fun `throw an exception trying to create a query with a invalid heroId`() {
        val request = HeroSeriesRequest(
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
    fun `success on fetch a Hero series`() {
        val request = HeroSeriesRequest(
            heroId = 1234,
            offset = 0,
            signData = SignData("", "")
        )

        val result = SeriesDataWrapper()

        given {
            repository.fetchHeroSeries(request)
        }.willReturn { Single.just(result) }

        heroComicsUserCase(request)
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValue(result.data)
            .awaitTerminalEvent()

        verify(repository).fetchHeroSeries(request)
    }
}
