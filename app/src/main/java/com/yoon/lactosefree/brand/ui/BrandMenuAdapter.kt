package com.yoon.lactosefree.brand.ui

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yoon.lactosefree.R
import com.yoon.lactosefree.brand.BrandBeverage
import com.yoon.lactosefree.databinding.ItemRecyclerviewMenuBinding
import com.yoon.lactosefree.favorite.Favorite


class BrandMenuAdapter(
    private val itemClickListener: (BrandBeverage) -> Unit,
    private val favoriteClickListener: (BrandBeverage) -> Unit
) :
    ListAdapter<BrandBeverage, BrandMenuAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(val binding: ItemRecyclerviewMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(brandBeverage: BrandBeverage) {
            with(binding) {
                brandMenuName.text = brandBeverage.beverageName
                Glide.with(brandMenuImage.context)
                    .load(brandBeverage.beverageImage)
                    .into(brandMenuImage)

                if (brandBeverage.favorite) {
                    favoriteButton.setImageResource(R.drawable.ic_favorite_24)
                } else {
                    favoriteButton.setImageResource(R.drawable.ic_favorite_border)
                }

                itemView.setOnClickListener { itemClickListener(brandBeverage) }

                favoriteButton.setOnClickListener {
                    if (!brandBeverage.favorite) {
                        favoriteButton.setImageResource(R.drawable.ic_favorite_24)
                    } else {
                        favoriteButton.setImageResource(R.drawable.ic_favorite_border)

                    }
                    favoriteClickListener(brandBeverage)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRecyclerviewMenuBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<BrandBeverage>() {
            override fun areItemsTheSame(
                oldItem: BrandBeverage,
                newItem: BrandBeverage
            ): Boolean {
                return oldItem.beverageName == newItem.beverageName
            }

            override fun areContentsTheSame(
                oldItem: BrandBeverage,
                newItem: BrandBeverage
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}



//class BrandMenuAdapter() :
//    RecyclerView.Adapter<BrandMenuAdapter.ItemHolder>() {
//
//    private lateinit var itemClickListener : OnItemClickListener
//    private lateinit var favoriteClickListener : OnItemClickListener
//    private lateinit var brandBeverageList: List<BrandBeverage>
//
//
//    interface OnItemClickListener {
//        fun onClick(v: View, position: Int)
//    }
//
//    fun addBrandBeverages(items: List<BrandBeverage>){
//        brandBeverageList = items
//        notifyItemChanged(0, brandBeverageList.size - 1)
//    }
//
//
//    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
//        var images: ImageView = itemView.findViewById(R.id.brandMenuImage)
//        var names: TextView = itemView.findViewById(R.id.brandMenuName)
//        var favoriteButton : ImageButton = itemView.findViewById(R.id.favoriteButton)
//
//    }
//
//    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ItemHolder {
//        val itemHolder =
//            LayoutInflater.from(viewGroup.context)
//                .inflate(R.layout.item_recyclerview_menu, viewGroup, false)
//        return ItemHolder(itemHolder)
//    }
//
//    override fun onBindViewHolder(itemHolder: ItemHolder, position: Int) {
//        itemHolder.names.text = brandBeverageList[position].beverageName
//        Glide.with(itemHolder.itemView.context)
//            .load(brandBeverageList[position].beverageImage)
//            .into(itemHolder.images)
//
//        //itemView 클릭이벤트
//        itemHolder.itemView.setOnClickListener {
//            itemClickListener.onClick(it, position)
//        }
//
//        initFavoriteButton(itemHolder, position)
//
//        //favoriteButton 클릭이벤트
//        itemHolder.favoriteButton.setOnClickListener {
//            if(brandBeverageList[position].favorite == false){
//                itemHolder.favoriteButton.setImageResource(R.drawable.ic_favorite_24)
//            }
//            else{
//                itemHolder.favoriteButton.setImageResource(R.drawable.ic_favorite_border)
//            }
//            favoriteClickListener.onClick(it,position)
//        }
//    }
//
//
//
//
//    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
//        this.itemClickListener = onItemClickListener
//    }
//    fun favoriteButtonClickListener(onFavoriteClickListener: OnItemClickListener){
//        this.favoriteClickListener = onFavoriteClickListener
//    }
//
//    private fun initFavoriteButton(itemHolder: ItemHolder, position: Int){
//        if (brandBeverageList[position].favorite == true){
//            itemHolder.favoriteButton.setImageResource(R.drawable.ic_favorite_24)
//        }else{
//            itemHolder.favoriteButton.setImageResource(R.drawable.ic_favorite_border)
//        }
//    }
//
//    fun getProductList() : List<BrandBeverage>{
//        return brandBeverageList
//    }
//
//    override fun getItemCount() =  brandBeverageList.size
//
//}