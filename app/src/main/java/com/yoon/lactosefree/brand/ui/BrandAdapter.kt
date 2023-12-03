package com.yoon.lactosefree.brand.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yoon.lactosefree.R
import com.yoon.lactosefree.brand.Brand
import com.yoon.lactosefree.databinding.ItemRecyclerviewBrandBinding

class BrandAdapter(private val itemClickListener: (Brand) -> Unit) :
    ListAdapter<Brand, BrandAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(val binding: ItemRecyclerviewBrandBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(brand: Brand) {
            with(binding) {
                brandName.text = brand.brandName
                Glide.with(brandImage.context)
                    .load(brand.brandLogoImage)
                    .into(brandImage)

            }
            itemView.setOnClickListener {
                itemClickListener(brand)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRecyclerviewBrandBinding.inflate(
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
        val diffUtil = object : DiffUtil.ItemCallback<Brand>() {
            override fun areItemsTheSame(oldItem: Brand, newItem: Brand): Boolean {
                return oldItem.brandName == newItem.brandName
            }

            override fun areContentsTheSame(oldItem: Brand, newItem: Brand): Boolean {
                return oldItem == newItem
            }

        }
    }

}
