package com.yoon.lactosefree.brand.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import com.yoon.lactosefree.R
import com.yoon.lactosefree.brand.MenuDetail
import com.yoon.lactosefree.databinding.DialogMenuDetailBinding


class MenuDetailDialog(
    private val detail: MenuDetail,
     context: Context)
    : Dialog(context) {

    private lateinit var dialogBinding: DialogMenuDetailBinding
    private lateinit var onClickListener : OnClickListener

    interface OnClickListener {
        fun onClick(v: View)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogBinding = DialogMenuDetailBinding.inflate(layoutInflater).also{
            context.setTheme(R.style.DialogStyle)
            setContentView(it.root)
        }

        with(dialogBinding){
            size.text = detail.size.toString()
            kcal.text = detail.kcal.toString()
            salt.text = detail.sodium.toString()
            sugar.text = detail.sugar.toString()
            fat.text = detail.fat.toString()
            protein.text = detail.protein.toString()
            caffeine.text = detail.caffeine.toString()
            if (detail.includeMilk){
                noticeMilkText.setText(R.string.includeMilk)
                menuDetailStomach.setImageResource(R.drawable.ic_stomach_48)
            }
            else{
                noticeMilkText.visibility = View.GONE
            }
        }

    }

    fun onClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

}

