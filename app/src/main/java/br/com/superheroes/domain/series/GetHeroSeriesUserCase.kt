package br.com.superheroes.domain.series

import br.com.superheroes.data.model.SeriesDataContainer
import br.com.superheroes.data.series.HeroSeriesRequest
import br.com.superheroes.library.reactivex.SchedulerProvider
import br.com.superheroes.library.reactivex.applySingleSchedulers
import io.reactivex.Single
import javax.inject.Inject

class GetHeroSeriesUserCase @Inject constructor(
    private val seriesRepository: SeriesRepository,
    private val schedulerProvider: SchedulerProvider
) {

    operator fun invoke(request: HeroSeriesRequest): Single<SeriesDataContainer> {
        if (request.heroId <= 0)
            return Single.error(IllegalArgumentException("Invalid heroId parameter"))

        return seriesRepository.fetchHeroSeries(request)
            .map { it.data }
            .compose(applySingleSchedulers(schedulerProvider))
    }
}
