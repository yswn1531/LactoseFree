package com.yoon.lactosefree.brand.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yoon.lactosefree.R
import com.yoon.lactosefree.brand.Brand
import com.yoon.lactosefree.brand.BrandBeverage


class BrandMenuAdapter(private var arrayList: MutableList<BrandBeverage>) :
    RecyclerView.Adapter<BrandMenuAdapter.ItemHolder>() {

    private lateinit var itemClickListener : OnItemClickListener
    private lateinit var favoriteClickListener : OnItemClickListener
    var getList = arrayList

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }



    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        var images: ImageView = itemView.findViewById(R.id.brandMenuImage)
        var names: TextView = itemView.findViewById(R.id.brandMenuName)
        var favoriteButton : ImageButton = itemView.findViewById(R.id.favoriteButton)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder =
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_recyclerview_menu, viewGroup, false)
        return ItemHolder(itemHolder)
    }

    override fun onBindViewHolder(itemHolder: ItemHolder, position: Int) {
        itemHolder.names.text = arrayList[position].beverageName
        Glide.with(itemHolder.itemView.context)
            .load(arrayList[position].beverageImage)
            .into(itemHolder.images)
        //itemView 클릭이벤트
        itemHolder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
        if (arrayList[position].favorite == true){
            itemHolder.favoriteButton.setImageResource(R.drawable.ic_favorite_24)
        }else{
            itemHolder.favoriteButton.setImageResource(R.drawable.ic_favorite_border)
        }

        itemHolder.favoriteButton.setOnClickListener {
            if(arrayList[position].favorite == false){
                itemHolder.favoriteButton.setImageResource(R.drawable.ic_favorite_24)
            }
            else{
                itemHolder.favoriteButton.setImageResource(R.drawable.ic_favorite_border)
            }
            favoriteClickListener.onClick(it,position)
        }
    }


    override fun getItemCount() = arrayList.size

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    fun favoriteButtonClickListener(onFavoriteClickListener: OnItemClickListener){
        this.favoriteClickListener = onFavoriteClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setBrandBeverageList(brandBeverage: List<BrandBeverage>) {
        arrayList = brandBeverage.toMutableList()
        notifyDataSetChanged()
    }
}