package com.sesac.lactosefree.brand.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sesac.lactosefree.R
import com.sesac.lactosefree.brand.Brand


class BrandAdapter(private val arrayList: ArrayList<Brand>, private val favoriteVisible : Boolean) :
    RecyclerView.Adapter<BrandAdapter.ItemHolder>() {

    private lateinit var itemClickListener : OnItemClickListener

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        var images: ImageView = itemView.findViewById<ImageView>(R.id.recyclerImage)
        var names: TextView = itemView.findViewById<TextView>(R.id.recyclerName)
        var favoriteButton : ImageButton = itemView.findViewById(R.id.favoriteButton)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder =
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_recyclerview_menu, viewGroup, false)
        return ItemHolder(itemHolder)
    }

    override fun onBindViewHolder(itemHolder: ItemHolder, position: Int) {
        itemHolder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
        itemHolder.names.text = arrayList[position].name
        itemHolder.images.setImageResource(arrayList[position].image)
        if(!favoriteVisible) itemHolder.favoriteButton.visibility = View.GONE
    }

    override fun getItemCount() = arrayList.size

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
}