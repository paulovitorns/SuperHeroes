package br.com.superheroes.domain.comic

import br.com.superheroes.data.comic.HeroComicsRequest
import br.com.superheroes.data.model.ComicDataWrapper
import io.reactivex.Single

interface ComicsRepository {
    fun fetchHeroComics(request: HeroComicsRequest): Single<ComicDataWrapper>
}
