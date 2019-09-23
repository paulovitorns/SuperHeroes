package br.com.superheroes.screen.detail

import androidx.annotation.VisibleForTesting
import br.com.superheroes.data.comic.HeroComicsRequest
import br.com.superheroes.data.model.Character
import br.com.superheroes.data.model.ComicList
import br.com.superheroes.data.model.SeriesList
import br.com.superheroes.data.model.SignData
import br.com.superheroes.data.series.HeroSeriesRequest
import br.com.superheroes.domain.comic.GetHeroComicsUserCase
import br.com.superheroes.domain.config.EnvironmentConfig
import br.com.superheroes.domain.series.GetHeroSeriesUserCase
import br.com.superheroes.library.extension.currentDate
import br.com.superheroes.library.injector.ActivityScope
import br.com.superheroes.library.reactivex.addDisposableTo
import br.com.superheroes.library.state.StateStore
import br.com.superheroes.screen.BasePresenter
import br.com.superheroes.screen.BaseUi
import javax.inject.Inject

@ActivityScope
class HeroPresenter @Inject constructor(
    environmentConfig: EnvironmentConfig,
    private val stateStore: StateStore,
    private val getHeroComics: GetHeroComicsUserCase,
    private val getHeroSeries: GetHeroSeriesUserCase
) : BasePresenter<BaseUi>() {

    private val heroUi: HeroUi? get() = baseUi()

    @VisibleForTesting
    var heroViewState = HeroViewState.Builder(HeroViewState())
        .setComicRequest(
            HeroComicsRequest(
                signData = SignData(
                    apiKey = environmentConfig.apiKey,
                    pvtKey = environmentConfig.pvtKey,
                    ts = currentDate().time
                )
            )
        )
        .setSeriesRequest(
            HeroSeriesRequest(
                signData = SignData(
                    apiKey = environmentConfig.apiKey,
                    pvtKey = environmentConfig.pvtKey,
                    ts = currentDate().time
                )
            )
        )
        .build()

    override fun onCreate() {
        super.onCreate()

        val savedState: Any? = stateStore.load(HeroUi::class)

        if (savedState != null && savedState is HeroViewState) {
            restoreLastState(savedState)
        } else {
            heroViewState = HeroViewState.Builder(heroViewState)
                .setCharacter(savedState as Character)
                .build()

            setCharacterDetails()
        }
    }

    override fun onSaveState() {
        super.onSaveState()
        stateStore.save(HeroUi::class, heroViewState)
    }

    private fun restoreLastState(savedState: HeroViewState) {
        heroViewState = HeroViewState.Builder(savedState)
            .build()

        with(heroViewState.character) {

            heroUi?.setHeroTitle(name)
            heroUi?.showHeroCover(
                "${thumbnail.path}.${thumbnail.extension}".replace(
                    "http",
                    "https"
                )
            )

            if (description.isNotBlank()) {
                heroUi?.showHeroDescription(description)
            }

            tryRestoreComics(comics)
            tryRestoreSeries(series)
        }
    }

    private fun tryRestoreComics(comics: ComicList) {
        if (heroViewState.comicsResult.total > 0) {
            heroUi?.showComicsSection()
            heroUi?.showComics(heroViewState.comicsResult.results)
        } else if (comics.available > 0) {
            heroUi?.showComicsSection()
            heroUi?.showComicsProgress()
            loadHeroComics()
        }
    }

    private fun tryRestoreSeries(series: SeriesList) {
        if (heroViewState.seriesResult.total > 0) {
            heroUi?.showSeriesSection()
            heroUi?.showSeries(heroViewState.seriesResult.results)
        } else if (series.available > 0) {
            heroUi?.showSeriesSection()
            heroUi?.showSeriesProgress()
            loadHeroSeries()
        }
    }

    private fun setCharacterDetails() {
        with(heroViewState.character) {

            val comicsRequest = heroViewState.comicsRequest
            val seriesRequest = heroViewState.seriesRequest

            heroViewState = HeroViewState.Builder(heroViewState)
                .setComicRequest(comicsRequest.copy(heroId = id))
                .setSeriesRequest(seriesRequest.copy(heroId = id))
                .build()

            heroUi?.setHeroTitle(name)
            heroUi?.showHeroCover(
                "${thumbnail.path}.${thumbnail.extension}".replace(
                    "http",
                    "https"
                )
            )

            if (description.isNotBlank()) {
                heroUi?.showHeroDescription(description)
            }

            if (comics.available > 0) {
                heroUi?.showComicsSection()
                heroUi?.showComicsProgress()
                loadHeroComics()
            }

            if (series.available > 0) {
                heroUi?.showSeriesSection()
                heroUi?.showSeriesProgress()
                loadHeroSeries()
            }
        }
    }

    private fun loadHeroComics() {
        getHeroComics(heroViewState.comicsRequest)
            .subscribe({ comicDataContainer ->
                heroViewState = HeroViewState.Builder(heroViewState)
                    .setComicsResult(comicDataContainer)
                    .build()

                heroUi?.showComics(heroViewState.comicsResult.results)
                heroUi?.hideComicsProgress()
            }, {
                heroViewState = HeroViewState.Builder(heroViewState)
                    .setComicsError(it)
                    .build()
                heroUi?.hideComicsProgress()
            }).addDisposableTo(disposeBag)
    }

    fun loadHeroComicsNextPage() {
        if (heroViewState.hasLoadedAllComics) return

        if (heroViewState.comicsResult.count < heroViewState.comicsResult.limit) {
            heroViewState = HeroViewState.Builder(heroViewState)
                .setLoadedAllComics(true)
                .build()

            heroUi?.showAllComicsLoaded()
            return
        }

        heroUi?.showComicsProgress()

        // Increment the Request Object
        val limit = heroViewState.comicsRequest.limit
        val currentOffset = heroViewState.comicsRequest.offset
        val request =
            heroViewState.comicsRequest.copy(offset = currentOffset + limit)

        heroViewState = HeroViewState.Builder(heroViewState)
            .setComicRequest(request)
            .build()

        getHeroComics(heroViewState.comicsRequest)
            .subscribe({ comicDataContainer ->
                if (comicDataContainer.results.isEmpty()) {
                    heroUi?.showAllComicsLoaded()

                    heroViewState = HeroViewState.Builder(heroViewState)
                        .setLoadedAllComics(true)
                        .build()
                } else {
                    val newData = heroViewState.comicsResult.results.toMutableList()
                    newData.addAll(comicDataContainer.results)

                    heroViewState = HeroViewState.Builder(heroViewState)
                        .setComicsResult(comicDataContainer.copy(results = newData))
                        .setLoadedAllComics(false)
                        .setComicsError(null)
                        .build()

                    heroUi?.showNextComicsPage(heroViewState.comicsResult.results)
                }

                heroUi?.hideComicsProgress()
            }, {
                heroViewState = HeroViewState.Builder(heroViewState)
                    .setComicsError(it)
                    .build()
                heroUi?.hideComicsProgress()
            }).addDisposableTo(disposeBag)
    }

    private fun loadHeroSeries() {
        getHeroSeries(heroViewState.seriesRequest)
            .subscribe({ seriesDataContainer ->
                heroViewState = HeroViewState.Builder(heroViewState)
                    .setSeriesResult(seriesDataContainer)
                    .build()
                heroUi?.showSeries(heroViewState.seriesResult.results)
                heroUi?.hideSeriesProgress()
            }, {
                heroViewState = HeroViewState.Builder(heroViewState)
                    .setSeriesError(it)
                    .build()
                heroUi?.hideSeriesProgress()
            }).addDisposableTo(disposeBag)
    }

    fun loadHeroSeriesNextPage() {
        if (heroViewState.hasLoadedAllSeries) return

        if (heroViewState.seriesResult.count < heroViewState.seriesRequest.limit) {
            heroViewState = HeroViewState.Builder(heroViewState)
                .setLoadedAllSeries(true)
                .build()

            heroUi?.showAllSeriesLoaded()
            return
        }

        heroUi?.showSeriesProgress()

        // Increment the Request Object
        val limit = heroViewState.seriesRequest.limit
        val currentOffset = heroViewState.seriesRequest.offset
        val request =
            heroViewState.seriesRequest.copy(offset = currentOffset + limit)

        heroViewState = HeroViewState.Builder(heroViewState)
            .setSeriesRequest(request)
            .build()

        getHeroSeries(heroViewState.seriesRequest)
            .subscribe({ seriesDataContainer ->
                if (seriesDataContainer.results.isEmpty()) {
                    heroUi?.showAllSeriesLoaded()

                    heroViewState = HeroViewState.Builder(heroViewState)
                        .setLoadedAllSeries(true)
                        .build()
                } else {
                    val newData = heroViewState.seriesResult.results.toMutableList()
                    newData.addAll(seriesDataContainer.results)

                    heroViewState = HeroViewState.Builder(heroViewState)
                        .setSeriesResult(seriesDataContainer.copy(results = newData))
                        .setLoadedAllSeries(false)
                        .setSeriesError(null)
                        .build()

                    heroUi?.showNextSeriesPage(heroViewState.seriesResult.results)
                }

                heroUi?.hideSeriesProgress()
            }, {
                heroViewState = HeroViewState.Builder(heroViewState)
                    .setSeriesError(it)
                    .build()
                heroUi?.hideSeriesProgress()
            }).addDisposableTo(disposeBag)
    }
}
