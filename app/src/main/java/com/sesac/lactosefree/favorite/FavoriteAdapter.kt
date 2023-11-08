package com.sesac.lactosefree.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sesac.lactosefree.R
import com.sesac.lactosefree.brand.Brand

class FavoriteAdapter(private val arrayList: ArrayList<Brand>) :
    RecyclerView.Adapter<FavoriteAdapter.ItemHolder>() {

    private lateinit var itemClickListener : OnItemClickListener

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        var images: ImageView = itemView.findViewById<ImageView>(R.id.favoriteImage)
        var names: TextView = itemView.findViewById<TextView>(R.id.favoriteName)
        var rating : RatingBar = itemView.findViewById(R.id.favoriteRating)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_recyclerview_favorite, viewGroup, false)
        return ItemHolder(itemHolder)
    }

    override fun onBindViewHolder(itemHolder: ItemHolder, position: Int) {
        itemHolder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
        itemHolder.names.text = arrayList[position].name
        itemHolder.images.setImageResource(arrayList[position].image)
        itemHolder.rating.rating = 3f

    }

    override fun getItemCount() = arrayList.size

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
}