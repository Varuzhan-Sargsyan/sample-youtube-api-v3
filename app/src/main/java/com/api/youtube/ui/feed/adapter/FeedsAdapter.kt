package com.api.youtube.ui.feed.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.api.youtube.R
import com.api.youtube.data.SearchItem
import com.api.youtube.databinding.ItemFeadListBinding
import com.api.youtube.utils.ImageLoader

class FeedsAdapter(private val hasNextPage: () -> Boolean,
                   private val downloadNextPage: () -> Unit,
                   private val onClick: (item: SearchItem) -> Unit) :
    RecyclerView.Adapter<FeedsAdapter.ItemViewHolder>() {
    private var items = mutableListOf<SearchItem>()
    private var imageLoader : ImageLoader? = null
    
    private fun getImageLoader(context: Context): ImageLoader {
        if (imageLoader == null) {
            imageLoader = ImageLoader(context)
        }
        return imageLoader!!
    }
    
    private fun getItem(position: Int): SearchItem {
        return items[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            imageLoader ?: getImageLoader(parent.context),
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_fead_list, parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position), onClick)
        if (hasNextPage() && position == items.size - 10) {
            downloadNextPage()
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return items.size
    }
    
    fun update(items: List<SearchItem>) {
        val diffResult = DiffUtil.calculateDiff(FeedsDiffCallback(this.items, items))
        this.items.clear()
        this.items.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }
    
    class ItemViewHolder(
        private val imageLoader: ImageLoader,
        private val binding: ItemFeadListBinding
    ) : RecyclerView.ViewHolder(binding.root) {
    
        fun bind(searchItem: SearchItem, onClick: (item: SearchItem) -> Unit) {
            binding.searchItem = searchItem
            imageLoader.loadImage(binding.imageView, searchItem.snippet?.thumbnails?.high?.url)
            binding.apply {
                viewClick.setOnClickListener { onClick(searchItem) }
            }
        }
    }
}

class FeedsDiffCallback(private val oldList: List<SearchItem>, private val newList: List<SearchItem>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }
    
    override fun getNewListSize(): Int {
        return newList.size
    }
    
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
            = oldList[oldItemPosition].id == newList[newItemPosition].id
            && oldList[oldItemPosition].etag == newList[newItemPosition].etag
    
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
            = areItemsTheSame(oldItemPosition, newItemPosition)
            && oldList[oldItemPosition].snippet?.channelTitle == newList[newItemPosition].snippet?.channelTitle
            && oldList[oldItemPosition].snippet?.thumbnails?.default == newList[newItemPosition].snippet?.thumbnails?.default
            && oldList[oldItemPosition].snippet?.thumbnails?.high == newList[newItemPosition].snippet?.thumbnails?.high
            && oldList[oldItemPosition].snippet?.thumbnails?.medium == newList[newItemPosition].snippet?.thumbnails?.medium
}