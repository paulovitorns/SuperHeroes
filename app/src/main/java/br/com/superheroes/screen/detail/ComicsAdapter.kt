package br.com.superheroes.screen.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.superheroes.R
import br.com.superheroes.data.model.Comic
import br.com.superheroes.library.recyclerview.SimpleAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.hero_related_item.view.itemImage
import kotlinx.android.synthetic.main.hero_related_item.view.itemTitle

class ComicsAdapter(
    private val context: Context,
    comics: MutableList<Comic> = mutableListOf()
) : SimpleAdapter<Comic, ComicsAdapter.ViewHolder>(comics) {

    override fun onCreateItemViewHolder(parent: ViewGroup): ViewHolder {
        val searchItemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.hero_related_item, parent, false)
        return ViewHolder(searchItemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, item: Comic) {
        with(holder) {
            title.text = item.title

            val thumbnails =
                "${item.thumbnail.path}.${item.thumbnail.extension}".replace("http", "https")

            Glide.with(context)
                .load(thumbnails)
                .placeholder(R.drawable.hydra_placeholer)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(image)
        }
    }

    override fun addItems(newItems: List<Comic>) {
        val oldItems = itemList
        if (oldItems.isEmpty()) {
            itemList.addAll(newItems)
            notifyDataSetChanged()
        } else {
            itemList.clear()
            itemList.addAll(newItems)

            DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int = oldItems.size

                override fun getNewListSize(): Int = newItems.size

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val oldItem = oldItems.getOrNull(oldItemPosition)
                    val newItem = newItems[newItemPosition]

                    if (oldItem == null) return false

                    if (oldItem.id == newItem.id) {
                        return true
                    }

                    return false
                }

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean {
                    val oldItem = oldItems.getOrNull(oldItemPosition)
                    val newItem = newItems[newItemPosition]

                    return oldItem is Comic && oldItem.id == newItem.id
                }
            }, false).dispatchUpdatesTo(this)
        }
    }

    inner class ViewHolder(itemVIew: View) : RecyclerView.ViewHolder(itemVIew) {
        val image: ImageView = itemVIew.itemImage
        val title: TextView = itemVIew.itemTitle
    }
}
