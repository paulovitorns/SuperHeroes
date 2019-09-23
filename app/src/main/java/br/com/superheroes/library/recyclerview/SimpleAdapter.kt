package br.com.superheroes.library.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder

abstract class SimpleAdapter<I, VH : ViewHolder>(
    protected val itemList: MutableList<I> = mutableListOf()
) : Adapter<ViewHolder>() {

    var onItemClicked: ((item: I) -> Unit)? = null

    override fun getItemCount(): Int = itemList.size

    abstract fun onCreateItemViewHolder(parent: ViewGroup): VH

    abstract fun onBindViewHolder(holder: VH, position: Int, item: I)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return onCreateItemViewHolder(parent)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val childItem = itemList[position]

        holder.itemView.setOnClickListener {
            onItemClicked?.invoke(childItem)
        }

        onBindViewHolder(holder as VH, position, childItem)
    }

    protected fun inflateView(
        @LayoutRes layoutResource: Int,
        rootView: ViewGroup,
        attachToRoot: Boolean = false
    ): View {
        val inflater = LayoutInflater.from(rootView.context)
        return inflater.inflate(layoutResource, rootView, attachToRoot)
    }

    open fun setItems(newItems: List<I>) {
        itemList.clear()
        itemList.addAll(newItems)
        notifyDataSetChanged()
    }

    abstract fun addItems(newItems: List<I>)

    open fun clearAll() {
        itemList.clear()
        notifyDataSetChanged()
    }

    fun isEmpty(): Boolean = itemList.isEmpty()
}
