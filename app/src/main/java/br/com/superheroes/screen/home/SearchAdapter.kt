package br.com.superheroes.screen.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.superheroes.R
import br.com.superheroes.data.model.Character
import br.com.superheroes.library.recyclerview.SimpleAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.search_item.view.heroImage
import kotlinx.android.synthetic.main.search_item.view.superHero

class SearchAdapter(
    private val context: Context,
    characters: MutableList<Character> = mutableListOf()
) : SimpleAdapter<Character, SearchAdapter.ViewHolder>(characters) {

    override fun onCreateItemViewHolder(parent: ViewGroup): ViewHolder {
        val searchItemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_item, parent, false)
        return ViewHolder(searchItemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, item: Character) {
        with(holder) {
            superHero.text = item.name

            val thumbnails =
                "${item.thumbnail.path}.${item.thumbnail.extension}".replace("http", "https")

            Glide.with(context)
                .load(thumbnails)
                .placeholder(R.drawable.hydra_placeholer)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(heroImage)
        }
    }

    inner class ViewHolder(itemVIew: View) : RecyclerView.ViewHolder(itemVIew) {
        val heroImage: ImageView = itemVIew.heroImage
        val superHero: TextView = itemVIew.superHero
    }
}
