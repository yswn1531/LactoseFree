package com.yoon.lactosefree.common

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.yoon.lactosefree.R
import com.yoon.lactosefree.databinding.DialogMenuBinding

class CustomDialog(private val message: String, context: Context) : Dialog(context) {

    private lateinit var dialogBinding: DialogMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogBinding = DialogMenuBinding.inflate(layoutInflater).also{
            context.setTheme(R.style.DialogStyle)
            setContentView(it.root)
        }
        dialogBinding.dialogText.text = message
    }
}