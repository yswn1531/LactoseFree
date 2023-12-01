package com.yoon.lactosefree.common

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.yoon.lactosefree.databinding.DialogLoadingBinding

class LoadingDialog(context: Context) : Dialog(context) {

        private lateinit var loadingBinding: DialogLoadingBinding
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            loadingBinding = DialogLoadingBinding.inflate(layoutInflater).also {
                setContentView(it.root)
            }

            setCancelable(false) // 터치이벤트 막기
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))    //배경 투명하게
        }
    }
