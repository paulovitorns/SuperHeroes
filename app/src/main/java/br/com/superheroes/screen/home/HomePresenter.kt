package br.com.superheroes.screen.home

import androidx.annotation.VisibleForTesting
import br.com.superheroes.data.model.Character
import br.com.superheroes.data.model.CharacterDataContainer
import br.com.superheroes.data.search.ResultNotFoundException
import br.com.superheroes.data.search.SearchCharactersRequest
import br.com.superheroes.domain.config.EnvironmentConfig
import br.com.superheroes.domain.search.GetCharactersUserCase
import br.com.superheroes.library.extension.currentDate
import br.com.superheroes.library.injector.ActivityScope
import br.com.superheroes.library.reactivex.SchedulerProvider
import br.com.superheroes.library.reactivex.addDisposableTo
import br.com.superheroes.library.state.StateStore
import br.com.superheroes.screen.BasePresenter
import br.com.superheroes.screen.BaseUi
import io.reactivex.Single
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@ActivityScope
class HomePresenter @Inject constructor(
    environmentConfig: EnvironmentConfig,
    private val getCharactersUserCase: GetCharactersUserCase,
    private val schedulerProvider: SchedulerProvider,
    private val stateStore: StateStore
) : BasePresenter<BaseUi>() {

    private val homeUi: HomeUi? get() = baseUi()

    @VisibleForTesting
    var searchViewState = SearchViewState
        .Builder(SearchViewState())
        .setRequest(
            SearchCharactersRequest(
                apiKey = environmentConfig.apiKey,
                pvtKey = environmentConfig.pvtKey,
                ts = currentDate().time
            )
        )
        .build()

    override fun onCreate() {
        super.onCreate()
        homeUi?.showDefaultQueryString(searchViewState.request.namesStartWith)
        restoreStateOrLoadDefaultQuery()
        bindIntents()
    }

    override fun onSaveState() {
        super.onSaveState()
        stateStore.save(HomeUi::class, searchViewState)
    }

    private fun bindIntents() {
        homeUi?.search()!!
            .debounce(1, TimeUnit.SECONDS, schedulerProvider.postWorkerThread())
            .filter { it.trim().isNotEmpty() }
            .filter { typed -> typed != searchViewState.request.namesStartWith }
            .subscribe({ queryString ->
                homeUi?.showProgress()

                val request = searchViewState.request.copy(
                    offset = 0,
                    namesStartWith = queryString,
                    ts = currentDate().time
                )
                searchViewState = SearchViewState.Builder(searchViewState)
                    .setRequest(request)
                    .build()

                fetchSuperHeroes().subscribe({
                    handleFirstPageResult(it)
                }, {
                    handleError(it)
                }).addDisposableTo(disposeBag)
            }, { }).addDisposableTo(disposeBag)
    }

    fun retryAction() {
        if (searchViewState.stateError !is UnknownHostException) return

        homeUi?.showProgress()

        val request = searchViewState.request.copy(ts = currentDate().time)
        searchViewState = SearchViewState.Builder(searchViewState)
            .setRequest(request)
            .build()

        fetchSuperHeroes()
            .subscribe({
                when {
                    searchViewState.request.offset == 0 -> handleFirstPageResult(it)
                    else -> handlePaginatedResult(it)
                }
            }, {
                handleError(it)
            }).addDisposableTo(disposeBag)
    }

    fun loadNextPage() {
        if (searchViewState.hasLoadedAllPages) return

        if (searchViewState.searchResult.count < searchViewState.searchResult.limit) {
            searchViewState = SearchViewState.Builder(searchViewState)
                .setLoadedAllPages(true)
                .build()

            homeUi?.showAllItemsLoaded()
            return
        }

        homeUi?.showProgress()

        // Increment the Request Object
        val limit = searchViewState.request.limit
        val currentOffset = searchViewState.request.offset
        val request =
            searchViewState.request.copy(offset = currentOffset + limit, ts = currentDate().time)

        searchViewState = SearchViewState.Builder(searchViewState)
            .setRequest(request)
            .build()

        fetchSuperHeroes()
            .subscribe({
                handlePaginatedResult(it)
            }, {
                handleError(it)
            }).addDisposableTo(disposeBag)
    }

    fun onCharacterSelected(character: Character) {
        homeUi?.openCharacterDetail(character)
    }

    private fun restoreStateOrLoadDefaultQuery() {
        val savedState = stateStore.load<SearchViewState>(HomeUi::class)
        savedState?.let { restoreLastState(it) } ?: loadDefaultQuery()
    }

    private fun restoreLastState(lastState: SearchViewState) {
        searchViewState = lastState
        if (lastState.stateError != null) {
            handleError(lastState.stateError)
        } else {
            handleFirstPageResult(lastState.searchResult)
        }
    }

    private fun loadDefaultQuery() {
        homeUi?.showProgress()
        fetchSuperHeroes().subscribe(
            { handleFirstPageResult(it) },
            { handleError(it) }
        ).addDisposableTo(disposeBag)
    }

    private fun fetchSuperHeroes(): Single<CharacterDataContainer> {
        return getCharactersUserCase(searchViewState.request)
    }

    private fun handleFirstPageResult(characterDataContainer: CharacterDataContainer) {
        homeUi?.hideAllErrorState()

        if (characterDataContainer.results.isEmpty()) {
            homeUi?.showSearchError(searchViewState.request.namesStartWith)
            searchViewState = SearchViewState.Builder(searchViewState)
                .setLoadedAllPages(true)
                .build()
        } else {
            searchViewState = SearchViewState.Builder(searchViewState)
                .setSearchResult(characterDataContainer)
                .setLoadedAllPages(false)
                .setStateError(null)
                .build()

            homeUi?.showSearchResult(searchViewState.searchResult.results)
        }
        homeUi?.hideProgress()
    }

    private fun handlePaginatedResult(characterDataContainer: CharacterDataContainer) {
        homeUi?.hideAllErrorState()

        if (characterDataContainer.results.isEmpty()) {
            homeUi?.showAllItemsLoaded()

            searchViewState = SearchViewState.Builder(searchViewState)
                .setLoadedAllPages(true)
                .build()
        } else {
            val newData = searchViewState.searchResult.results.toMutableList()
            newData.addAll(characterDataContainer.results)

            searchViewState = SearchViewState.Builder(searchViewState)
                .setSearchResult(characterDataContainer.copy(results = newData))
                .setLoadedAllPages(false)
                .setStateError(null)
                .build()

            homeUi?.showNextPage(searchViewState.searchResult.results)
        }
        homeUi?.hideProgress()
    }

    private fun handleError(error: Throwable) {
        when (error) {
            is ResultNotFoundException -> homeUi?.showSearchError(error.queryString)
            is UnknownHostException -> homeUi?.showOfflineState()
            else -> homeUi?.showDefaultError()
        }

        searchViewState = SearchViewState.Builder(searchViewState)
            .setStateError(error)
            .build()

        homeUi?.hideProgress()
    }
}