package br.com.superheroes.screen.detail

import br.com.superheroes.data.comic.HeroComicsRequest
import br.com.superheroes.data.model.Character
import br.com.superheroes.data.model.ComicDataContainer
import br.com.superheroes.data.model.SeriesDataContainer
import br.com.superheroes.data.model.SignData
import br.com.superheroes.data.series.HeroSeriesRequest

data class HeroViewState(
    // stored the hero
    val character: Character = Character(),
    // stored the comics request model
    val comicsRequest: HeroComicsRequest = HeroComicsRequest(signData = SignData("", "")),
    // stored the series request model
    val seriesRequest: HeroSeriesRequest = HeroSeriesRequest(signData = SignData("", "")),
    // indicates that's has loaded all comics
    val hasLoadedAllComics: Boolean = false,
    // indicates that's has loaded all series
    val hasLoadedAllSeries: Boolean = false,
    // used to store data result from search hero's comics
    val comicsResult: ComicDataContainer = ComicDataContainer(),
    // used to store data result from search hero's series
    val seriesResult: SeriesDataContainer = SeriesDataContainer(),
    // indicates that's occurs some error while loading comics
    val comicsStateError: Throwable? = null,
    // indicates that's occurs some error while loading series
    val seriesStateError: Throwable? = null
) {

    fun builder(): Builder {
        return Builder(this)
    }

    class Builder(heroViewState: HeroViewState) {
        private var character = heroViewState.character
        private var comicsRequest = heroViewState.comicsRequest
        private var seriesRequest = heroViewState.seriesRequest
        private var hasLoadedAllComics = heroViewState.hasLoadedAllComics
        private var hasLoadedAllSeries = heroViewState.hasLoadedAllSeries
        private var comicsResult: ComicDataContainer = heroViewState.comicsResult
        private var seriesResult: SeriesDataContainer = heroViewState.seriesResult
        private var comicsStateError: Throwable? = heroViewState.comicsStateError
        private var seriesStateError: Throwable? = heroViewState.seriesStateError

        fun setCharacter(character: Character): Builder {
            this.character = character
            return this
        }

        fun setComicRequest(request: HeroComicsRequest): Builder {
            this.comicsRequest = request
            return this
        }

        fun setSeriesRequest(request: HeroSeriesRequest): Builder {
            this.seriesRequest = request
            return this
        }

        fun setLoadedAllComics(flag: Boolean): Builder {
            this.hasLoadedAllComics = flag
            return this
        }

        fun setLoadedAllSeries(flag: Boolean): Builder {
            this.hasLoadedAllSeries = flag
            return this
        }

        fun setComicsResult(comicsResult: ComicDataContainer): Builder {
            this.comicsResult = comicsResult
            return this
        }

        fun setSeriesResult(seriesResult: SeriesDataContainer): Builder {
            this.seriesResult = seriesResult
            return this
        }

        fun setComicsError(error: Throwable?): Builder {
            this.comicsStateError = error
            return this
        }

        fun setSeriesError(error: Throwable?): Builder {
            this.seriesStateError = error
            return this
        }

        fun build(): HeroViewState {
            return HeroViewState(
                character,
                comicsRequest,
                seriesRequest,
                hasLoadedAllComics,
                hasLoadedAllSeries,
                comicsResult,
                seriesResult,
                comicsStateError,
                seriesStateError
            )
        }
    }
}
