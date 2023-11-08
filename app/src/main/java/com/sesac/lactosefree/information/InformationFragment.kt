package com.sesac.lactosefree.information

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sesac.lactosefree.common.ViewBindingBaseFragment
import com.sesac.lactosefree.databinding.FragmentInformationBinding

class InformationFragment : ViewBindingBaseFragment<FragmentInformationBinding>(FragmentInformationBinding::inflate){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}