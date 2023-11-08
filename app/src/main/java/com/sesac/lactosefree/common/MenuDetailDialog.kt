package com.sesac.lactosefree.common

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import com.sesac.lactosefree.brand.Detail
import com.sesac.lactosefree.databinding.DialogMenuDetailBinding


class MenuDetailDialog(
    private val detail: Detail,
    private val context: Context)
    : Dialog(context) {

    private lateinit var dialogBinding: DialogMenuDetailBinding
    private lateinit var onClickListener : OnClickListener

    interface OnClickListener {
        fun onClick(v: View)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogBinding = DialogMenuDetailBinding.inflate(layoutInflater).also{
            setContentView(it.root)
        }
        with(dialogBinding){
            kcal.text = detail.kcal.toString()
            salt.text = detail.salt.toString()
            sugar.text = detail.sugar.toString()
            fat.text = detail.fat.toString()
            protein.text = detail.protein.toString()
            caffeine.text = detail.caffeine.toString()
        }

    }

    fun onClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

}

