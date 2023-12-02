package com.yoon.lactosefree.favorite.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yoon.lactosefree.R
import com.yoon.lactosefree.brand.Brand
import com.yoon.lactosefree.favorite.Favorite

class FavoriteAdapter() :
    RecyclerView.Adapter<FavoriteAdapter.ItemHolder>() {

    private lateinit var itemClickListener : OnItemClickListener
    private lateinit var favoriteList: List<Favorite>

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setProductList(favorites: List<Favorite>){
        if (favorites.isNotEmpty()){
            favoriteList = favorites
            notifyItemChanged(0, favorites.size - 1)
        }else {
            favoriteList = emptyList()
            notifyItemChanged(0)
        }

    }

    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        var images: ImageView = itemView.findViewById<ImageView>(R.id.favoriteImage)
        var brand : TextView = itemView.findViewById(R.id.favoriteBrand)
        var beverageName: TextView = itemView.findViewById<TextView>(R.id.favoriteName)
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
        favoriteList.let { list ->
            itemHolder.brand.text = list[position].brandName
            itemHolder.beverageName.text = list[position].brandBeverageName
            itemHolder.rating.rating = list[position].rating
            Glide.with(itemHolder.itemView.context)
                .load(list[position].imageUri)   //이미지를 어디서 가져오느냐?
                .into(itemHolder.images)
        }

    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    fun getProductList() : List<Favorite>{
        return favoriteList
    }
    override fun getItemCount(): Int =  favoriteList.size
}