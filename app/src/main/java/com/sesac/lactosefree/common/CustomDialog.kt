package com.sesac.lactosefree.common

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.sesac.lactosefree.databinding.DialogMenuBinding

class CustomDialog(private val message: String,
                   private val context: Context)
    : Dialog(context) {
    private lateinit var dialogBinding: DialogMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogBinding = DialogMenuBinding.inflate(layoutInflater).also{
            setContentView(it.root)
        }
        dialogBinding.dialogText.text = message

    }

}