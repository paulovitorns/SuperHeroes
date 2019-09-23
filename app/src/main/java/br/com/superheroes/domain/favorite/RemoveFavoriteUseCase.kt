package br.com.superheroes.domain.favorite

import br.com.superheroes.data.model.Character
import br.com.superheroes.library.reactivex.SchedulerProvider
import br.com.superheroes.library.reactivex.applyCompletableSchedulers
import io.reactivex.Completable
import javax.inject.Inject

class RemoveFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val schedulerProvider: SchedulerProvider
) {

    operator fun invoke(character: Character): Completable =
        favoriteRepository.removeFavorite(character)
            .compose(applyCompletableSchedulers(schedulerProvider))
}
