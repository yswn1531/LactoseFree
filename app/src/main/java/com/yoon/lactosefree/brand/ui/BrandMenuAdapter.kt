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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


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

                if (brandBeverage.includeMilk){
                    includeMilkEmotion.visibility = View.VISIBLE
                }
                else {
                    includeMilkEmotion.visibility = View.INVISIBLE
                }


                if (brandBeverage.favorite) {
                    favoriteButton.setImageResource(R.drawable.ic_favorite_24)
                } else {
                    favoriteButton.setImageResource(R.drawable.ic_favorite_border)
                }

                itemView.setOnClickListener { itemClickListener(brandBeverage) }

                favoriteButton
                    .flowClicks()
                    .throttleFirst(500L)
                    .onEach {
                        if (!brandBeverage.favorite) {
                            favoriteButton.setImageResource(R.drawable.ic_favorite_24)
                        } else {
                            favoriteButton.setImageResource(R.drawable.ic_favorite_border)
                        }
                        favoriteClickListener(brandBeverage)
                    }.launchIn(CoroutineScope(Dispatchers.Main))
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

private fun View.flowClicks(): Flow<Unit> = callbackFlow {
    setOnClickListener {
        trySend(Unit).isSuccess
    }
    awaitClose { setOnClickListener(null) }
}.buffer(0)

/**
 * 중복 클릭 방지
 *
 * @param T 타입
 * @param intervalTime 중복 클릭 방지 시간
 * @return flow의 값을 리턴
 * @author 윤성욱
 */
private fun <T> Flow<T>.throttleFirst(intervalTime: Long): Flow<T> = flow {
    var throttleTime = 0L
    collect { upStream ->
        val currentTime = System.currentTimeMillis()
        if ((currentTime - throttleTime) > intervalTime) {
            throttleTime = currentTime
            emit(upStream)
        }
    }
}