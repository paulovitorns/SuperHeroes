package br.com.superheroes.screen.detail

import br.com.superheroes.data.model.Character
import br.com.superheroes.data.model.Comic
import br.com.superheroes.data.model.ComicDataContainer
import br.com.superheroes.data.model.ComicList
import br.com.superheroes.data.model.Series
import br.com.superheroes.data.model.SeriesDataContainer
import br.com.superheroes.data.model.SeriesList
import br.com.superheroes.data.model.Thumbnail
import br.com.superheroes.domain.comic.GetHeroComicsUserCase
import br.com.superheroes.domain.config.EnvironmentConfig
import br.com.superheroes.domain.series.GetHeroSeriesUserCase
import br.com.superheroes.library.reactivex.DisposeBag
import br.com.superheroes.library.state.StateStore
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.willReturn
import io.reactivex.Single
import org.junit.Test

class HeroPresenterTest {

    private val getHeroComics = mock<GetHeroComicsUserCase>()
    private val getHeroSeries = mock<GetHeroSeriesUserCase>()
    private val stateStore = mock<StateStore>()
    private val environmentConfig = EnvironmentConfig()
    private val heroUi = mock<HeroUi>()

    private val presenter = HeroPresenter(
        environmentConfig,
        stateStore,
        getHeroComics,
        getHeroSeries
    ).apply {
        disposeBag = DisposeBag()
    }

    init {
        presenter.setUi(heroUi)
    }

    @Test
    fun `load hero details with comics and series`() {
        val character = Character(
            name = "Random Hero",
            description = "Random description",
            series = SeriesList(available = 10),
            comics = ComicList(available = 10),
            thumbnail = Thumbnail(path = "http://images", extension = "jpg")
        )
        val expectedComics = ComicDataContainer(total = 10)
        val expectedSeries = SeriesDataContainer(total = 10)
        val expectedState = HeroViewState.Builder(presenter.heroViewState)
            .setCharacter(character)
            .setComicsResult(expectedComics)
            .setSeriesResult(expectedSeries)
            .build()

        given { stateStore.load<Character>(HeroUi::class) }.willReturn { character }
        given { getHeroComics(presenter.heroViewState.comicsRequest) }.willReturn {
            Single.just(
                expectedComics
            )
        }
        given { getHeroSeries(presenter.heroViewState.seriesRequest) }.willReturn {
            Single.just(
                expectedSeries
            )
        }

        presenter.onCreate()

        assert(presenter.heroViewState == expectedState)

        verify(heroUi).setHeroTitle(character.name)
        verify(heroUi).showHeroCover("https://images.jpg")
        verify(heroUi).showHeroDescription(character.description)

        verify(heroUi).showComicsSection()
        verify(heroUi).showComicsProgress()

        verify(heroUi).showSeriesSection()
        verify(heroUi).showSeriesProgress()

        verify(heroUi).showComics(expectedComics.results)
        verify(heroUi).hideComicsProgress()

        verify(heroUi).showSeries(expectedSeries.results)
        verify(heroUi).hideSeriesProgress()

        verify(getHeroComics).invoke(presenter.heroViewState.comicsRequest)
        verify(getHeroSeries).invoke(presenter.heroViewState.seriesRequest)
    }

    @Test
    fun `load hero details with series only`() {
        val character = Character(
            name = "Random Hero",
            description = "Random description",
            series = SeriesList(available = 10),
            comics = ComicList(available = 0),
            thumbnail = Thumbnail(path = "http://images", extension = "jpg")
        )
        val expectedSeries = SeriesDataContainer(total = 10)
        val expectedState = HeroViewState.Builder(presenter.heroViewState)
            .setCharacter(character)
            .setSeriesResult(expectedSeries)
            .build()

        given { stateStore.load<Character>(HeroUi::class) }.willReturn { character }
        given { getHeroSeries(presenter.heroViewState.seriesRequest) }.willReturn {
            Single.just(
                expectedSeries
            )
        }

        presenter.onCreate()

        assert(presenter.heroViewState == expectedState)

        verify(heroUi).setHeroTitle(character.name)
        verify(heroUi).showHeroCover("https://images.jpg")
        verify(heroUi).showHeroDescription(character.description)

        verify(heroUi, never()).showComicsSection()
        verify(heroUi, never()).showComicsProgress()

        verify(heroUi).showSeriesSection()
        verify(heroUi).showSeriesProgress()

        verify(heroUi, never()).showComics(emptyList())
        verify(heroUi, never()).hideComicsProgress()

        verify(heroUi).showSeries(expectedSeries.results)
        verify(heroUi).hideSeriesProgress()

        verify(getHeroComics, never()).invoke(presenter.heroViewState.comicsRequest)
        verify(getHeroSeries).invoke(presenter.heroViewState.seriesRequest)
    }

    @Test
    fun `load hero details with comics only`() {
        val character = Character(
            name = "Random Hero",
            description = "Random description",
            series = SeriesList(available = 0),
            comics = ComicList(available = 10),
            thumbnail = Thumbnail(path = "http://images", extension = "jpg")
        )
        val expectedComics = ComicDataContainer(total = 10)
        val expectedState = HeroViewState.Builder(presenter.heroViewState)
            .setCharacter(character)
            .setComicsResult(expectedComics)
            .build()

        given { stateStore.load<Character>(HeroUi::class) }.willReturn { character }
        given { getHeroComics(presenter.heroViewState.comicsRequest) }.willReturn {
            Single.just(
                expectedComics
            )
        }

        presenter.onCreate()

        assert(presenter.heroViewState == expectedState)

        verify(heroUi).setHeroTitle(character.name)
        verify(heroUi).showHeroCover("https://images.jpg")
        verify(heroUi).showHeroDescription(character.description)

        verify(heroUi).showComicsSection()
        verify(heroUi).showComicsProgress()

        verify(heroUi, never()).showSeriesSection()
        verify(heroUi, never()).showSeriesProgress()

        verify(heroUi).showComics(expectedComics.results)
        verify(heroUi).hideComicsProgress()

        verify(heroUi, never()).showSeries(emptyList())
        verify(heroUi, never()).hideSeriesProgress()

        verify(getHeroComics).invoke(presenter.heroViewState.comicsRequest)
        verify(getHeroSeries, never()).invoke(presenter.heroViewState.seriesRequest)
    }

    @Test
    fun `load hero details without comics and series`() {
        val character = Character(
            name = "Random Hero",
            description = "Random description",
            series = SeriesList(available = 0),
            comics = ComicList(available = 0),
            thumbnail = Thumbnail(path = "http://images", extension = "jpg")
        )
        val expectedState = HeroViewState.Builder(presenter.heroViewState)
            .setCharacter(character)
            .build()

        given { stateStore.load<Character>(HeroUi::class) }.willReturn { character }

        presenter.onCreate()

        assert(presenter.heroViewState == expectedState)

        verify(heroUi).setHeroTitle(character.name)
        verify(heroUi).showHeroCover("https://images.jpg")
        verify(heroUi).showHeroDescription(character.description)

        verify(heroUi, never()).showComicsSection()
        verify(heroUi, never()).showComicsProgress()

        verify(heroUi, never()).showSeriesSection()
        verify(heroUi, never()).showSeriesProgress()

        verify(heroUi, never()).showComics(emptyList())
        verify(heroUi, never()).hideComicsProgress()

        verify(heroUi, never()).showSeries(emptyList())
        verify(heroUi, never()).hideSeriesProgress()

        verify(getHeroComics, never()).invoke(presenter.heroViewState.comicsRequest)
        verify(getHeroSeries, never()).invoke(presenter.heroViewState.seriesRequest)
    }

    @Test
    fun `load hero details with comics and series from a restored state`() {
        val character = Character(
            name = "Random Hero",
            description = "Random description",
            series = SeriesList(available = 10),
            comics = ComicList(available = 10),
            thumbnail = Thumbnail(path = "http://images", extension = "jpg")
        )
        val comics = ComicDataContainer(total = 10)
        val series = SeriesDataContainer(total = 10)
        val expectedState = HeroViewState.Builder(presenter.heroViewState)
            .setCharacter(character)
            .setComicsResult(comics)
            .setSeriesResult(series)
            .build()

        presenter.heroViewState = expectedState

        presenter.onSaveState()

        given { stateStore.load<HeroViewState>(HeroUi::class) }.willReturn { expectedState }

        presenter.onCreate()

        assert(presenter.heroViewState == expectedState)

        verify(heroUi).setHeroTitle(character.name)
        verify(heroUi).showHeroCover("https://images.jpg")
        verify(heroUi).showHeroDescription(character.description)

        verify(heroUi).showComicsSection()
        verify(heroUi, never()).showComicsProgress()

        verify(heroUi).showSeriesSection()
        verify(heroUi, never()).showSeriesProgress()

        verify(heroUi).showComics(comics.results)
        verify(heroUi, never()).hideComicsProgress()

        verify(heroUi).showSeries(series.results)
        verify(heroUi, never()).hideSeriesProgress()

        verify(getHeroComics, never()).invoke(presenter.heroViewState.comicsRequest)
        verify(getHeroSeries, never()).invoke(presenter.heroViewState.seriesRequest)
    }

    @Test
    fun `load hero details with comics pending from a restored state`() {
        val character = Character(
            name = "Random Hero",
            description = "Random description",
            series = SeriesList(available = 10),
            comics = ComicList(available = 10),
            thumbnail = Thumbnail(path = "http://images", extension = "jpg")
        )
        val expectedComics = ComicDataContainer(total = 10)
        val series = SeriesDataContainer(total = 10)
        val previousState = HeroViewState.Builder(presenter.heroViewState)
            .setCharacter(character)
            .setSeriesResult(series)
            .build()

        presenter.heroViewState = previousState
        presenter.onSaveState()

        val expectedState = HeroViewState.Builder(presenter.heroViewState)
            .setCharacter(character)
            .setComicsResult(expectedComics)
            .build()

        given { stateStore.load<HeroViewState>(HeroUi::class) }.willReturn { previousState }
        given { getHeroComics(presenter.heroViewState.comicsRequest) }.willReturn {
            Single.just(
                expectedComics
            )
        }
        presenter.onCreate()

        assert(presenter.heroViewState == expectedState)

        verify(heroUi).setHeroTitle(character.name)
        verify(heroUi).showHeroCover("https://images.jpg")
        verify(heroUi).showHeroDescription(character.description)

        verify(heroUi).showComicsSection()
        verify(heroUi).showComicsProgress()

        verify(heroUi).showSeriesSection()
        verify(heroUi, never()).showSeriesProgress()

        verify(heroUi).showComics(expectedComics.results)
        verify(heroUi).hideComicsProgress()

        verify(heroUi).showSeries(series.results)
        verify(heroUi, never()).hideSeriesProgress()

        verify(getHeroComics).invoke(presenter.heroViewState.comicsRequest)
        verify(getHeroSeries, never()).invoke(presenter.heroViewState.seriesRequest)
    }

    @Test
    fun `load hero details with series pending from a restored state`() {
        val character = Character(
            name = "Random Hero",
            description = "Random description",
            series = SeriesList(available = 10),
            comics = ComicList(available = 10),
            thumbnail = Thumbnail(path = "http://images", extension = "jpg")
        )
        val comics = ComicDataContainer(total = 10)
        val expectedSeries = SeriesDataContainer(total = 10)
        val previousState = HeroViewState.Builder(presenter.heroViewState)
            .setCharacter(character)
            .setComicsResult(comics)
            .build()

        presenter.heroViewState = previousState
        presenter.onSaveState()

        val expectedState = HeroViewState.Builder(presenter.heroViewState)
            .setCharacter(character)
            .setSeriesResult(expectedSeries)
            .build()

        given { stateStore.load<HeroViewState>(HeroUi::class) }.willReturn { previousState }
        given { getHeroSeries(presenter.heroViewState.seriesRequest) }.willReturn {
            Single.just(
                expectedSeries
            )
        }
        presenter.onCreate()

        assert(presenter.heroViewState == expectedState)

        verify(heroUi).setHeroTitle(character.name)
        verify(heroUi).showHeroCover("https://images.jpg")
        verify(heroUi).showHeroDescription(character.description)

        verify(heroUi).showComicsSection()
        verify(heroUi, never()).showComicsProgress()

        verify(heroUi).showSeriesSection()
        verify(heroUi).showSeriesProgress()

        verify(heroUi).showComics(comics.results)
        verify(heroUi, never()).hideComicsProgress()

        verify(heroUi).showSeries(expectedSeries.results)
        verify(heroUi).hideSeriesProgress()

        verify(getHeroComics, never()).invoke(presenter.heroViewState.comicsRequest)
        verify(getHeroSeries).invoke(presenter.heroViewState.seriesRequest)
    }

    @Test
    fun `load hero details with series and comics pending from a restored state`() {
        val character = Character(
            name = "Random Hero",
            description = "Random description",
            series = SeriesList(available = 10),
            comics = ComicList(available = 10),
            thumbnail = Thumbnail(path = "http://images", extension = "jpg")
        )
        val expectedComics = ComicDataContainer(total = 10)
        val expectedSeries = SeriesDataContainer(total = 10)
        val previousState = HeroViewState.Builder(presenter.heroViewState)
            .setCharacter(character)
            .build()

        presenter.heroViewState = previousState
        presenter.onSaveState()

        val expectedState = HeroViewState.Builder(presenter.heroViewState)
            .setCharacter(character)
            .setSeriesResult(expectedSeries)
            .setComicsResult(expectedComics)
            .build()

        given { stateStore.load<HeroViewState>(HeroUi::class) }.willReturn { previousState }
        given { getHeroComics(presenter.heroViewState.comicsRequest) }.willReturn {
            Single.just(
                expectedComics
            )
        }
        given { getHeroSeries(presenter.heroViewState.seriesRequest) }.willReturn {
            Single.just(
                expectedSeries
            )
        }
        presenter.onCreate()

        assert(presenter.heroViewState == expectedState)

        verify(heroUi).setHeroTitle(character.name)
        verify(heroUi).showHeroCover("https://images.jpg")
        verify(heroUi).showHeroDescription(character.description)

        verify(heroUi).showComicsSection()
        verify(heroUi).showComicsProgress()

        verify(heroUi).showSeriesSection()
        verify(heroUi).showSeriesProgress()

        verify(heroUi).showComics(expectedComics.results)
        verify(heroUi).hideComicsProgress()

        verify(heroUi).showSeries(expectedSeries.results)
        verify(heroUi).hideSeriesProgress()

        verify(getHeroComics).invoke(presenter.heroViewState.comicsRequest)
        verify(getHeroSeries).invoke(presenter.heroViewState.seriesRequest)
    }

    @Test
    fun `load next comics page`() {
        val character = Character(
            name = "Random Hero",
            description = "Random description",
            series = SeriesList(available = 0),
            comics = ComicList(available = 10),
            thumbnail = Thumbnail(path = "http://images", extension = "jpg")
        )
        val expectedComics = ComicDataContainer(
            offset = 0,
            count = 10,
            limit = 10,
            total = 100,
            results = listOf(Comic(), Comic())
        )
        val expectedPaginatedComics = ComicDataContainer(
            offset = 10,
            count = 10,
            limit = 10,
            total = 100,
            results = listOf(Comic(), Comic())
        )
        val initialState = HeroViewState.Builder(presenter.heroViewState)
            .setCharacter(character)
            .setComicsResult(expectedComics)
            .build()

        given { stateStore.load<Character>(HeroUi::class) }.willReturn { character }
        given { getHeroComics(presenter.heroViewState.comicsRequest) }.willReturn {
            Single.just(
                expectedComics
            )
        }

        presenter.onCreate()

        assert(presenter.heroViewState == initialState)

        given { getHeroComics(presenter.heroViewState.comicsRequest.copy(offset = 10)) }.willReturn {
            Single.just(
                expectedPaginatedComics
            )
        }

        val expectedState = HeroViewState.Builder(presenter.heroViewState)
            .setComicsResult(
                expectedPaginatedComics.copy(
                    results = listOf(
                        Comic(),
                        Comic(),
                        Comic(),
                        Comic()
                    )
                )
            )
            .setComicRequest(presenter.heroViewState.comicsRequest.copy(offset = 10))
            .build()

        presenter.loadHeroComicsNextPage()

        assert(presenter.heroViewState == expectedState)

        verify(heroUi, times(2)).showComicsProgress()
        verify(heroUi, times(2)).hideComicsProgress()
        verify(heroUi).showNextComicsPage(presenter.heroViewState.comicsResult.results)
        verify(getHeroComics).invoke(presenter.heroViewState.comicsRequest)
    }

    @Test
    fun `show all comics page loaded`() {
        presenter.heroViewState = HeroViewState.Builder(presenter.heroViewState)
            .setComicsResult(ComicDataContainer(count = 4, limit = 10))
            .build()

        presenter.loadHeroComicsNextPage()

        verify(heroUi).showAllComicsLoaded()
    }

    @Test
    fun `load next series page`() {
        val character = Character(
            name = "Random Hero",
            description = "Random description",
            series = SeriesList(available = 10),
            comics = ComicList(available = 0),
            thumbnail = Thumbnail(path = "http://images", extension = "jpg")
        )
        val expectedSeries = SeriesDataContainer(
            offset = 0,
            count = 10,
            limit = 10,
            total = 100,
            results = listOf(Series(), Series())
        )
        val expectedPaginatedComics = SeriesDataContainer(
            offset = 10,
            count = 10,
            limit = 10,
            total = 100,
            results = listOf(Series(), Series())
        )
        val initialState = HeroViewState.Builder(presenter.heroViewState)
            .setCharacter(character)
            .setSeriesResult(expectedSeries)
            .build()

        given { stateStore.load<Character>(HeroUi::class) }.willReturn { character }
        given { getHeroSeries(presenter.heroViewState.seriesRequest) }.willReturn {
            Single.just(
                expectedSeries
            )
        }

        presenter.onCreate()

        assert(presenter.heroViewState == initialState)

        given { getHeroSeries(presenter.heroViewState.seriesRequest.copy(offset = 10)) }.willReturn {
            Single.just(
                expectedPaginatedComics
            )
        }

        val expectedState = HeroViewState.Builder(presenter.heroViewState)
            .setSeriesResult(
                expectedPaginatedComics.copy(
                    results = listOf(
                        Series(),
                        Series(),
                        Series(),
                        Series()
                    )
                )
            )
            .setSeriesRequest(presenter.heroViewState.seriesRequest.copy(offset = 10))
            .build()

        presenter.loadHeroSeriesNextPage()

        assert(presenter.heroViewState == expectedState)

        verify(heroUi, times(2)).showSeriesProgress()
        verify(heroUi, times(2)).hideSeriesProgress()
        verify(heroUi).showNextSeriesPage(presenter.heroViewState.seriesResult.results)
        verify(getHeroSeries).invoke(presenter.heroViewState.seriesRequest)
    }

    @Test
    fun `show all series page loaded`() {
        presenter.heroViewState = HeroViewState.Builder(presenter.heroViewState)
            .setSeriesResult(SeriesDataContainer(count = 4, limit = 10))
            .build()

        presenter.loadHeroSeriesNextPage()

        verify(heroUi).showAllSeriesLoaded()
    }
}
