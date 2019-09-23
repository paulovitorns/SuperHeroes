package br.com.superheroes.screen.detail

import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.superheroes.R
import br.com.superheroes.data.model.Comic
import br.com.superheroes.data.model.Series
import br.com.superheroes.library.extension.toast
import br.com.superheroes.screen.BaseActivity
import br.com.superheroes.screen.BaseUi
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.activity_hero.appToolbar
import kotlinx.android.synthetic.main.activity_hero.comicsRecycler
import kotlinx.android.synthetic.main.activity_hero.heroCover
import kotlinx.android.synthetic.main.activity_hero.heroDescription
import kotlinx.android.synthetic.main.activity_hero.progress
import kotlinx.android.synthetic.main.activity_hero.progressComics
import kotlinx.android.synthetic.main.activity_hero.progressSeries
import kotlinx.android.synthetic.main.activity_hero.sectionComics
import kotlinx.android.synthetic.main.activity_hero.sectionDescription
import kotlinx.android.synthetic.main.activity_hero.sectionSeries
import kotlinx.android.synthetic.main.activity_hero.seriesRecycler

interface HeroUi : BaseUi {
    fun showContentProgress()
    fun hideContentProgress()
    fun setHeroTitle(heroName: String)
    fun showHeroCover(imageUrl: String)
    fun showHeroDescription(description: String)
    fun showComicsProgress()
    fun hideComicsProgress()
    fun showComicsSection()
    fun showComics(comics: List<Comic>)
    fun showNextComicsPage(comics: List<Comic>)
    fun showAllComicsLoaded()
    fun showSeriesProgress()
    fun hideSeriesProgress()
    fun showSeriesSection()
    fun showSeries(series: List<Series>)
    fun showNextSeriesPage(series: List<Series>)
    fun showAllSeriesLoaded()
}

class HeroActivity : BaseActivity<HeroPresenter>(), HeroUi {

    override val layoutRes: Int? = R.layout.activity_hero
    private val comicsAdapter by lazy { ComicsAdapter(this) }
    private val seriesAdapter by lazy { SeriesAdapter(this) }

    override fun setupToolbar() {
        super.setupToolbar()
        setSupportActionBar(appToolbar)
        title = ""
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun showContentProgress() {
        progress.isVisible = true
    }

    override fun hideContentProgress() {
        progress.isVisible = false
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

    override fun showComicsProgress() {
        progressComics.isVisible = true
    }

    override fun hideComicsProgress() {
        progressComics.isVisible = false
    }

    override fun showComicsSection() {
        sectionComics.isVisible = true
    }

    override fun showComics(comics: List<Comic>) {
        comicsRecycler.isVisible = true

        with(comicsRecycler) {
            adapter = comicsAdapter
        }

        comicsRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val layoutManager = (recyclerView.layoutManager as LinearLayoutManager)
                val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()

                if (lastVisibleItemPosition == comicsAdapter.itemCount - 1) {
                    presenter.loadHeroComicsNextPage()
                }
            }
        })

        comicsAdapter.setItems(comics)
    }

    override fun showNextComicsPage(comics: List<Comic>) {
        val lastPosition = comicsAdapter.itemCount
        comicsAdapter.addItems(comics)
        val linearManager = comicsRecycler.layoutManager as LinearLayoutManager
        linearManager.scrollToPosition(lastPosition)
    }

    override fun showAllComicsLoaded() {
        toast(R.string.all_comics_loaded)
    }

    override fun showSeriesProgress() {
        progressSeries.isVisible = true
    }

    override fun hideSeriesProgress() {
        progressSeries.isVisible = false
    }

    override fun showSeriesSection() {
        sectionSeries.isVisible = true
    }

    override fun showSeries(series: List<Series>) {
        seriesRecycler.isVisible = true

        with(seriesRecycler) {
            adapter = seriesAdapter
        }

        seriesRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val layoutManager = (recyclerView.layoutManager as LinearLayoutManager)
                val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()

                if (lastVisibleItemPosition == seriesAdapter.itemCount - 1) {
                    presenter.loadHeroSeriesNextPage()
                }
            }
        })

        seriesAdapter.setItems(series)
    }

    override fun showNextSeriesPage(series: List<Series>) {
        val lastPosition = seriesAdapter.itemCount
        seriesAdapter.addItems(series)
        val linearManager = seriesRecycler.layoutManager as LinearLayoutManager
        linearManager.scrollToPosition(lastPosition)
    }

    override fun showAllSeriesLoaded() {
        toast(R.string.all_series_loaded)
    }
}
