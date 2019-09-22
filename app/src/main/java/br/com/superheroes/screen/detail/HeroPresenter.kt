package br.com.superheroes.screen.detail

import br.com.superheroes.data.model.Character
import br.com.superheroes.library.injector.ActivityScope
import br.com.superheroes.library.state.StateStore
import br.com.superheroes.screen.BasePresenter
import br.com.superheroes.screen.BaseUi
import javax.inject.Inject

@ActivityScope
class HeroPresenter @Inject constructor(
    private val stateStore: StateStore
) : BasePresenter<BaseUi>() {

    private val heroUi: HeroUi? get() = baseUi()
    private var character: Character? = null

    override fun onCreate() {
        super.onCreate()

        character = stateStore.load(HeroUi::class)

        character?.let { hero ->
            heroUi?.setHeroTitle(hero.name)
            heroUi?.showHeroCover(
                "${hero.thumbnail.path}.${hero.thumbnail.extension}".replace(
                    "http",
                    "https"
                )
            )

            if (hero.description.isNotBlank()) {
                heroUi?.showHeroDescription(hero.description)
            }

            if (hero.comics.available > 0) {
                heroUi?.showComics()
            }

            if (hero.series.available > 0) {
                heroUi?.showSeries()
            }
        }
    }

    override fun onSaveState() {
        super.onSaveState()
        character?.let { stateStore.save(HeroUi::class, it) }
    }
}
