package br.com.superheroes.domain.favorite

import br.com.superheroes.data.model.Character
import br.com.superheroes.library.reactivex.SchedulerProvider
import br.com.superheroes.library.reactivex.applyFlowableSchedulers
import io.reactivex.Flowable
import javax.inject.Inject

class ListFavoritesUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val schedulerProvider: SchedulerProvider
) {

    operator fun invoke(): Flowable<List<Character>> = favoriteRepository
        .listFavorites()
        .map {
            val newList: MutableList<Character> = mutableListOf()
            it.forEach { item ->
                item.isFavorite = true
                newList.add(item)
            }
            newList
        }.compose(applyFlowableSchedulers(schedulerProvider))
}
