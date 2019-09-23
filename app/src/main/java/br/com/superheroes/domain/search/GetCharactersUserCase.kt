package br.com.superheroes.domain.search

import br.com.superheroes.data.model.CharacterDataContainer
import br.com.superheroes.data.search.SearchCharactersRequest
import br.com.superheroes.domain.favorite.FavoriteRepository
import br.com.superheroes.library.reactivex.SchedulerProvider
import br.com.superheroes.library.reactivex.applySingleSchedulers
import io.reactivex.Single
import javax.inject.Inject

class GetCharactersUserCase @Inject constructor(
    private val charactersRepository: CharactersRepository,
    private val favoriteRepository: FavoriteRepository,
    private val schedulerProvider: SchedulerProvider
) {

    operator fun invoke(charactersRequest: SearchCharactersRequest): Single<CharacterDataContainer> {
        return charactersRepository.fetchCharacters(charactersRequest)
            .flatMap { item ->
                Single.fromCallable {
                    val items = item.data.results.toMutableList()
                    for (character in item.data.results) {
                        if (favoriteRepository.isFavoriteSaved(character)) {
                            val itemIndex = items.indexOf(character)
                            character.isFavorite = true
                            items.removeAt(itemIndex)
                            items.add(itemIndex, character)
                        }
                    }
                    item.data.copy(results = items)
                }.compose(applySingleSchedulers(schedulerProvider))
            }.compose(applySingleSchedulers(schedulerProvider))
    }
}
