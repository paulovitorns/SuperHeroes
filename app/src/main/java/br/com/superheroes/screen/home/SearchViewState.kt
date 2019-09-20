package br.com.superheroes.screen.home

import br.com.superheroes.data.model.CharacterDataContainer
import br.com.superheroes.data.search.SearchCharactersRequest

data class SearchViewState(
    // stored the request model
    val request: SearchCharactersRequest = SearchCharactersRequest(apiKey = "", pvtKey = ""),
    // indicates that's has loaded all the pages
    val hasLoadedAllPages: Boolean = false,
    // used to store data result from search repositories or loaded a next repositories page
    val searchResult: CharacterDataContainer = CharacterDataContainer(),
    // indicates that's occurs some error while loading the next page
    val stateError: Throwable? = null
) {

    fun builder(): Builder {
        return Builder(this)
    }

    class Builder(searchViewState: SearchViewState) {
        private var request = searchViewState.request
        private var hasLoadedAllPages = searchViewState.hasLoadedAllPages
        private var searchResult: CharacterDataContainer = searchViewState.searchResult
        private var stateError: Throwable? = searchViewState.stateError

        fun setRequest(request: SearchCharactersRequest): Builder {
            this.request = request
            return this
        }

        fun setLoadedAllPages(flag: Boolean): Builder {
            this.hasLoadedAllPages = flag
            return this
        }

        fun setSearchResult(characterDataContainer: CharacterDataContainer): Builder {
            this.searchResult = characterDataContainer
            return this
        }

        fun setStateError(error: Throwable?): Builder {
            this.stateError = error
            return this
        }

        fun build(): SearchViewState {
            return SearchViewState(
                request,
                hasLoadedAllPages,
                searchResult,
                stateError
            )
        }
    }
}
