package br.com.superheroes.domain.search

import br.com.superheroes.data.model.Character
import br.com.superheroes.data.search.SearchCharactersRequest
import br.com.superheroes.domain.config.EnvironmentConfig
import br.com.superheroes.library.reactivex.SchedulerProvider
import br.com.superheroes.library.reactivex.applySingleSchedulers
import io.reactivex.Single
import javax.inject.Inject

class GetCharactersUserCase @Inject constructor(
    private val charactersRepository: CharactersRepository,
    private val environmentConfig: EnvironmentConfig,
    private val schedulerProvider: SchedulerProvider
) {

    operator fun invoke(charactersRequest: SearchCharactersRequest): Single<List<Character>> {
        if (charactersRequest.namesStartWith.isBlank())
            return Single.error(IllegalArgumentException("Missing name parameter"))

        val request = charactersRequest.copy(apiKey = environmentConfig.apiKey)
        return charactersRepository.fetchCharacters(request)
            .map { it.data.results }
            .compose(applySingleSchedulers(schedulerProvider))
    }
}
