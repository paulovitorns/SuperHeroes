package br.com.superheroes.screen.favorites

import android.util.Log
import br.com.superheroes.data.model.Character
import br.com.superheroes.domain.favorite.AddFavoriteUseCase
import br.com.superheroes.domain.favorite.ListFavoritesUseCase
import br.com.superheroes.domain.favorite.RemoveFavoriteUseCase
import br.com.superheroes.library.injector.ActivityScope
import br.com.superheroes.library.reactivex.addDisposableTo
import br.com.superheroes.library.state.StateStore
import br.com.superheroes.screen.BasePresenter
import br.com.superheroes.screen.BaseUi
import br.com.superheroes.screen.detail.HeroUi
import io.reactivex.Flowable
import javax.inject.Inject

@ActivityScope
class FavoritesPresenter @Inject constructor(
    private val listFavoritesUseCase: ListFavoritesUseCase,
    private val addFavorite: AddFavoriteUseCase,
    private val removeFavorite: RemoveFavoriteUseCase,
    private val stateStore: StateStore
) : BasePresenter<BaseUi>() {

    private val favoritesUi: FavoritesUi? get() = baseUi()

    private var favList: List<Character> = emptyList()

    override fun onCreate() {
        super.onCreate()
        restoreStateOrLoadDefaultQuery()
    }

    override fun onSaveState() {
        super.onSaveState()
        stateStore.save(FavoritesUi::class, favList)
    }

    fun onCharacterSelected(character: Character) {
        // TODO:: load details from API
        stateStore.save(HeroUi::class, character)
        favoritesUi?.openCharacterDetail()
    }

    fun tapOnFavoriteButton(character: Character) {
        favoritesUi?.showProgress()
        if (character.isFavorite) {
            removeFavorite(character)
                .subscribe({
                    val items = favList.toMutableList()
                    val itemIndex = items.indexOf(character)
                    character.isFavorite = false
                    items.removeAt(itemIndex)

                    favList = items

                    handlePageResult(favList)
                }, {
                    Log.e("Error_fav", it.message)
                }).addDisposableTo(disposeBag)
        } else {
            addFavorite(character)
                .subscribe({
                    val items = favList.toMutableList()
                    val itemIndex = items.indexOf(character)
                    character.isFavorite = true
                    items.removeAt(itemIndex)
                    items.add(itemIndex, character)

                    favList = items

                    handlePageResult(favList)
                }, {
                    Log.e("Error_fav", it.message)
                }).addDisposableTo(disposeBag)
        }
    }

    private fun restoreStateOrLoadDefaultQuery() {
        val savedState = stateStore.load<List<Character>>(FavoritesUi::class)
        savedState?.let { restoreLastState(it) } ?: loadFavorites()
    }

    private fun restoreLastState(list: List<Character>) {
        favList = list
        handlePageResult(favList)
    }

    private fun loadFavorites() {
        favoritesUi?.showProgress()
        fetchFavoriteCharacters().subscribe(
            {
                favList = it
                handlePageResult(favList)
            }, {
                favoritesUi?.showEmptyFavorites()
                favoritesUi?.hideProgress()
            }
        ).addDisposableTo(disposeBag)
    }

    private fun fetchFavoriteCharacters(): Flowable<List<Character>> {
        return listFavoritesUseCase()
    }

    private fun handlePageResult(characters: List<Character>) {
        favoritesUi?.showSearchResult(characters)
        if (characters.isEmpty()) {
            favoritesUi?.showEmptyFavorites()
        }
        favoritesUi?.hideProgress()
    }
}
