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
import kotlinx.android.synthetic.main.search_item.view.forks
import kotlinx.android.synthetic.main.search_item.view.ownerImage
import kotlinx.android.synthetic.main.search_item.view.ownerName
import kotlinx.android.synthetic.main.search_item.view.repository
import kotlinx.android.synthetic.main.search_item.view.stars

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
            repository.text = item.name
//            stars.text = item.stargazersCount.toString()
//            forks.text = item.forksCount.toString()

            val thumbnails =
                "${item.thumbnail.path}.${item.thumbnail.extension}".replace("http", "https")

            Glide.with(context)
                .load(thumbnails)
                .placeholder(R.drawable.hydra_placeholer)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(ownerImage)

//            ownerName.text = item.owner.login
        }
    }

    inner class ViewHolder(itemVIew: View) : RecyclerView.ViewHolder(itemVIew) {
        val ownerImage: ImageView = itemVIew.ownerImage
        val repository: TextView = itemVIew.repository
        val stars: TextView = itemVIew.stars
        val forks: TextView = itemVIew.forks
        val ownerName: TextView = itemVIew.ownerName
    }
}
