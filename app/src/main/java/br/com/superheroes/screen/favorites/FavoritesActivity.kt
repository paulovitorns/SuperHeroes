package br.com.superheroes.screen.favorites

import android.content.Intent
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.superheroes.R
import br.com.superheroes.data.model.Character
import br.com.superheroes.screen.BaseActivity
import br.com.superheroes.screen.BaseUi
import br.com.superheroes.screen.detail.HeroActivity
import br.com.superheroes.screen.home.SearchAdapter
import kotlinx.android.synthetic.main.favorites_layout.progress
import kotlinx.android.synthetic.main.favorites_layout.recyclerView
import kotlinx.android.synthetic.main.favorites_layout.searchNotFound
import kotlinx.android.synthetic.main.favorites_layout.toolbar
import kotlinx.android.synthetic.main.search_not_found_state.notFoundDescription
import kotlinx.android.synthetic.main.search_not_found_state.notFoundTitle

interface FavoritesUi : BaseUi {
    fun showProgress()
    fun hideProgress()
    fun showSearchResult(characters: List<Character>)
    fun openCharacterDetail()
    fun showEmptyFavorites()
}

class FavoritesActivity : BaseActivity<FavoritesPresenter>(), FavoritesUi {

    override val layoutRes: Int = R.layout.favorites_layout

    private val gridLayoutManage by lazy {
        GridLayoutManager(
            this,
            2,
            GridLayoutManager.VERTICAL,
            false
        )
    }
    private val searchAdapter by lazy { SearchAdapter(this) }
    private var lastRecyclerPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            lastRecyclerPosition = savedInstanceState.getInt(LAST_RECYCLER_POSITION)
        }
    }

    override fun setupViews() {
        super.setupViews()

        setSupportActionBar(toolbar)
        setTitle(R.string.favorites_title)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        searchAdapter.apply {
            onItemClicked = { character ->
                presenter.onCharacterSelected(character)
            }

            onFavoriteClicked = { character ->
                presenter.tapOnFavoriteButton(character)
            }
        }

        with(recyclerView) {
            layoutManager = gridLayoutManage
            adapter = searchAdapter
        }
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val linearManager = recyclerView.layoutManager as LinearLayoutManager
        val scrollPosition = linearManager.findFirstVisibleItemPosition()
        outState.putInt(LAST_RECYCLER_POSITION, scrollPosition)
        super.onSaveInstanceState(outState)
    }

    override fun showProgress() {
        progress.isVisible = true
    }

    override fun hideProgress() {
        progress.isVisible = false
    }

    override fun showSearchResult(characters: List<Character>) {
        showRecyclerIfNeeded()
        searchAdapter.setItems(characters)

        if (lastRecyclerPosition > 0) {
            val linearManager = recyclerView.layoutManager as LinearLayoutManager
            linearManager.scrollToPositionWithOffset(lastRecyclerPosition, 0)
        }
    }

    private fun showRecyclerIfNeeded() {
        if (!recyclerView.isVisible) recyclerView.isVisible = true
    }

    override fun openCharacterDetail() {
        Intent(this, HeroActivity::class.java).also {
            startActivity(it)
        }
    }

    override fun showEmptyFavorites() {
        notFoundTitle.setText(R.string.empty_favorite_title)
        notFoundDescription.setText(R.string.empty_favorito_description)
        searchNotFound.isVisible = true
    }
}

private const val LAST_RECYCLER_POSITION = "last_recycler_position"
