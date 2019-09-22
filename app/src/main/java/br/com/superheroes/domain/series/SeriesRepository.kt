package br.com.superheroes.domain.series

import br.com.superheroes.data.model.SeriesDataWrapper
import br.com.superheroes.data.series.HeroSeriesRequest
import io.reactivex.Single

interface SeriesRepository {
    fun fetchHeroSeries(request: HeroSeriesRequest): Single<SeriesDataWrapper>
}
