package br.com.superheroes.screen.detail

import androidx.core.view.isVisible
import br.com.superheroes.R
import br.com.superheroes.screen.BaseActivity
import br.com.superheroes.screen.BaseUi
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.activity_hero.appToolbar
import kotlinx.android.synthetic.main.activity_hero.comics
import kotlinx.android.synthetic.main.activity_hero.heroCover
import kotlinx.android.synthetic.main.activity_hero.heroDescription
import kotlinx.android.synthetic.main.activity_hero.sectionComics
import kotlinx.android.synthetic.main.activity_hero.sectionDescription
import kotlinx.android.synthetic.main.activity_hero.sectionSeries
import kotlinx.android.synthetic.main.activity_hero.series

interface HeroUi : BaseUi {
    fun setHeroTitle(heroName: String)
    fun showHeroCover(imageUrl: String)
    fun showHeroDescription(description: String)
    fun showComics()
    fun showSeries()
}

class HeroActivity : BaseActivity<HeroPresenter>(), HeroUi {

    override val layoutRes: Int? = R.layout.activity_hero

    override fun setupToolbar() {
        super.setupToolbar()
        setSupportActionBar(appToolbar)
        title = ""
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun setHeroTitle(heroName: String) {
        title = heroName
    }

    override fun showHeroCover(imageUrl: String) {
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.hydra_placeholer)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(heroCover)
    }

    override fun showHeroDescription(description: String) {
        sectionDescription.isVisible = true
        heroDescription.isVisible = true
        heroDescription.text = description
    }

    override fun showComics() {
        sectionComics.isVisible = true
        comics.isVisible = true
    }

    override fun showSeries() {
        sectionSeries.isVisible = true
        series.isVisible = true
    }
}
