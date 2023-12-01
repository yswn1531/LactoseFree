package com.yoon.lactosefree.brand.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yoon.lactosefree.R
import com.yoon.lactosefree.brand.Brand


class BrandAdapter() :
    RecyclerView.Adapter<BrandAdapter.ItemHolder>() {

    private lateinit var itemClickListener : OnItemClickListener
    private lateinit var brandList: List<Brand>

    fun addBrands(items: List<Brand>){
        brandList = items
        notifyItemChanged(0, brandList.size - 1)
    }


    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        val names: TextView = itemView.findViewById(R.id.brandName)
        val images: ImageView = itemView.findViewById(R.id.brandImage)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder =
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_recyclerview_brand, viewGroup, false)
        return ItemHolder(itemHolder)
    }

    override fun onBindViewHolder(itemHolder: ItemHolder, position: Int) {
        itemHolder.names.text = brandList[position].brandName
        Glide.with(itemHolder.itemView.context)
            .load(brandList[position].brandLogoImage)
            .into(itemHolder.images)
        itemHolder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    override fun getItemCount() = brandList.size
}