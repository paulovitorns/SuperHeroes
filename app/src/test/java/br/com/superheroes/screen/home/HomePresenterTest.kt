package br.com.superheroes.screen.home

import br.com.superheroes.data.model.Character
import br.com.superheroes.data.model.CharacterDataContainer
import br.com.superheroes.data.search.ResultNotFoundException
import br.com.superheroes.domain.config.EnvironmentConfig
import br.com.superheroes.domain.search.GetCharactersUserCase
import br.com.superheroes.library.reactivex.DisposeBag
import br.com.superheroes.library.reactivex.TestSchedulerProvider
import br.com.superheroes.library.state.StateStore
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.willReturn
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Test
import java.net.UnknownHostException

class HomePresenterTest {

    private val schedulerProvider = TestSchedulerProvider()
    private val getCharactersUserCase = mock<GetCharactersUserCase>()
    private val stateStore = mock<StateStore>()
    private val environmentConfig = EnvironmentConfig()
    private val homeView = mock<HomeUi>()
    private val presenter = HomePresenter(
        environmentConfig,
        getCharactersUserCase,
        schedulerProvider,
        stateStore
    ).apply {
        disposeBag = DisposeBag()
    }

    init {
        presenter.setUi(homeView)
    }

    @Test
    fun `success on load default hero with no previous state stored`() {
        val expectedItems = CharacterDataContainer(results = listOf(Character(), Character()))

        val expectedViewState = SearchViewState.Builder(presenter.searchViewState)
            .setSearchResult(expectedItems)
            .setLoadedAllPages(false)
            .setStateError(null)
            .build()

        given { stateStore.load<SearchViewState>(HomeUi::class) }.willReturn { null }
        given { homeView.search() }.willReturn { Observable.just("") }
        given {
            getCharactersUserCase(presenter.searchViewState.request)
        }.willReturn { Single.just(expectedItems) }

        presenter.onCreate()

        assert(presenter.searchViewState == expectedViewState)

        verify(homeView).showDefaultQueryString(presenter.searchViewState.request.namesStartWith)

        verify(homeView).showProgress()
        verify(homeView).hideAllErrorState()
        verify(homeView).showSearchResult(expectedItems.results)
        verify(homeView).hideProgress()
        verify(stateStore).load<SearchViewState>(HomeUi::class)
        verify(getCharactersUserCase).invoke(presenter.searchViewState.request)
    }

    @Test
    fun `success on search heroes with a typed name`() {
        val expectedItems = CharacterDataContainer(results = listOf(Character(), Character()))
        val typedQuery = "Marvel"

        given { stateStore.load<SearchViewState>(HomeUi::class) }.willReturn { null }
        given { homeView.search() }.willReturn { Observable.just(typedQuery) }
        given {
            getCharactersUserCase(presenter.searchViewState.request)
        }.willReturn { Single.just(expectedItems) }

        presenter.onCreate()

        val expectedViewState = SearchViewState.Builder(presenter.searchViewState)
            .setRequest(presenter.searchViewState.request.copy(namesStartWith = typedQuery))
            .setSearchResult(expectedItems)
            .setLoadedAllPages(false)
            .setStateError(null)
            .build()

        assert(presenter.searchViewState == expectedViewState)

        verify(homeView, times(2)).showProgress()
        verify(homeView).hideAllErrorState()
        verify(homeView).showSearchResult(expectedItems.results)
        verify(homeView).hideProgress()
        verify(stateStore).load<SearchViewState>(HomeUi::class)
        verify(getCharactersUserCase).invoke(presenter.searchViewState.request)
    }

    @Test
    fun `success on load next page of a GitHub search result`() {
        val expectedItems = CharacterDataContainer(results = listOf(Character(), Character()))
        val paginatedExpectedItems = CharacterDataContainer(
            results = listOf(
                Character(),
                Character(),
                Character(),
                Character()
            )
        )

        given { stateStore.load<SearchViewState>(HomeUi::class) }.willReturn { null }
        given { homeView.search() }.willReturn { Observable.just("") }

        given {
            getCharactersUserCase(presenter.searchViewState.request)
        }.willReturn { Single.just(expectedItems) }

        presenter.onCreate()

        given {
            getCharactersUserCase(presenter.searchViewState.request.copy(offset = 20))
        }.willReturn { Single.just(expectedItems) }

        presenter.loadNextPage()

        val expectedViewState = SearchViewState.Builder(presenter.searchViewState)
            .setSearchResult(paginatedExpectedItems)
            .setLoadedAllPages(false)
            .setStateError(null)
            .build()

        assert(presenter.searchViewState == expectedViewState)

        verify(homeView).showDefaultQueryString(presenter.searchViewState.request.namesStartWith)
        verify(homeView, times(2)).showProgress()
        verify(homeView, times(2)).hideAllErrorState()
        verify(homeView).showNextPage(paginatedExpectedItems.results)
        verify(homeView, times(2)).hideProgress()
        verify(getCharactersUserCase).invoke(presenter.searchViewState.request)
    }

    @Test
    fun `show result not found state to a DC hero query`() {
        val typedQuery = "Super-man"
        val error = ResultNotFoundException(typedQuery)
        val expectedViewState = SearchViewState.Builder(presenter.searchViewState)
            .setLoadedAllPages(false)
            .setStateError(error)
            .build()

        given { stateStore.load<SearchViewState>(HomeUi::class) }.willReturn { null }
        given { homeView.search() }.willReturn { Observable.just(typedQuery) }
        given {
            getCharactersUserCase(presenter.searchViewState.request)
        }.willReturn { Single.error(error) }

        presenter.onCreate()

        assert(presenter.searchViewState.stateError == expectedViewState.stateError)

        verify(homeView, times(2)).showProgress()
        verify(homeView).showSearchError(typedQuery)
        verify(homeView).hideProgress()
    }

    @Test
    fun `show offline state while trying to do a search`() {
        val error = UnknownHostException()
        val expectedViewState = SearchViewState.Builder(presenter.searchViewState)
            .setLoadedAllPages(false)
            .setStateError(error)
            .build()

        given { stateStore.load<SearchViewState>(HomeUi::class) }.willReturn { null }
        given { homeView.search() }.willReturn { Observable.just("") }
        given {
            getCharactersUserCase(presenter.searchViewState.request)
        }.willReturn { Single.error(error) }

        presenter.onCreate()

        assert(presenter.searchViewState == expectedViewState)

        verify(homeView).showProgress()
        verify(homeView).showOfflineState()
        verify(homeView).hideProgress()
    }

    @Test
    fun `show a default error state while trying to do a search`() {
        val error = IllegalArgumentException("Any kind of error")
        val expectedViewState = SearchViewState.Builder(presenter.searchViewState)
            .setLoadedAllPages(false)
            .setStateError(error)
            .build()

        given { stateStore.load<SearchViewState>(HomeUi::class) }.willReturn { null }
        given { homeView.search() }.willReturn { Observable.just("") }
        given {
            getCharactersUserCase(presenter.searchViewState.request)
        }.willReturn { Single.error(error) }

        presenter.onCreate()

        assert(presenter.searchViewState == expectedViewState)

        verify(homeView).showProgress()
        verify(homeView).showDefaultError()
        verify(homeView).hideProgress()
    }

    @Test
    fun `success on retry a failed action`() {
        val expectedItems = CharacterDataContainer(results = listOf(Character(), Character()))

        given { stateStore.load<SearchViewState>(HomeUi::class) }.willReturn { null }
        given { homeView.search() }.willReturn { Observable.just("") }
        given {
            getCharactersUserCase(presenter.searchViewState.request)
        }.willReturn { Single.error(UnknownHostException()) }

        presenter.onCreate()

        given {
            getCharactersUserCase(presenter.searchViewState.request)
        }.willReturn { Single.just(expectedItems) }

        presenter.retryAction()

        val expectedViewState = SearchViewState.Builder(presenter.searchViewState)
            .setSearchResult(expectedItems)
            .setLoadedAllPages(false)
            .setStateError(null)
            .build()

        assert(presenter.searchViewState == expectedViewState)

        verify(homeView, times(2)).showProgress()
        verify(homeView).hideAllErrorState()
        verify(homeView).showSearchResult(expectedItems.results)
        verify(homeView, times(2)).hideProgress()
        verify(stateStore).load<SearchViewState>(HomeUi::class)
        verify(getCharactersUserCase, times(2)).invoke(presenter.searchViewState.request)
    }

    @Test
    fun `success on restore the last state`() {
        val expectedItems = CharacterDataContainer(results = listOf(Character(), Character()))
        val expectedViewState = SearchViewState.Builder(presenter.searchViewState)
            .setSearchResult(expectedItems)
            .setLoadedAllPages(false)
            .setStateError(null)
            .build()

        given { stateStore.load<SearchViewState>(HomeUi::class) }.willReturn { null }
        given { homeView.search() }.willReturn { Observable.just("") }
        given {
            getCharactersUserCase(presenter.searchViewState.request)
        }.willReturn { Single.just(expectedItems) }

        presenter.onCreate()
        presenter.onSaveState()

        given { stateStore.load<SearchViewState>(HomeUi::class) }.willReturn { expectedViewState }

        presenter.onCreate()

        assert(presenter.searchViewState == expectedViewState)

        verify(homeView).showProgress()
        verify(homeView, times(2)).hideAllErrorState()
        verify(homeView, times(2)).showSearchResult(expectedItems.results)
        verify(homeView, times(2)).hideProgress()
        verify(stateStore, times(2)).load<SearchViewState>(HomeUi::class)
        verify(getCharactersUserCase).invoke(presenter.searchViewState.request)
    }
}
