package br.com.superheroes.domain.comic

import br.com.superheroes.data.comic.HeroComicsRequest
import br.com.superheroes.data.model.ComicDataContainer
import br.com.superheroes.library.reactivex.SchedulerProvider
import br.com.superheroes.library.reactivex.applySingleSchedulers
import io.reactivex.Single
import javax.inject.Inject

class GetHeroComicsUserCase @Inject constructor(
    private val comicsRepository: ComicsRepository,
    private val schedulerProvider: SchedulerProvider
) {

    operator fun invoke(request: HeroComicsRequest): Single<ComicDataContainer> {
        if (request.heroId <= 0)
            return Single.error(IllegalArgumentException("Invalid heroId parameter"))

        return comicsRepository.fetchHeroComics(request)
            .map { it.data }
            .compose(applySingleSchedulers(schedulerProvider))
    }
}
