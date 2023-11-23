package com.yoon.lactosefree.brand.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.yoon.lactosefree.R
import com.yoon.lactosefree.brand.Brand
import com.yoon.lactosefree.common.ViewBindingBaseFragment
import com.yoon.lactosefree.databinding.FragmentBrandBinding

class BrandFragment : ViewBindingBaseFragment<FragmentBrandBinding>(FragmentBrandBinding::inflate){

    private var brandList = arrayListOf<Brand>()
    private var brandAdapter =  BrandAdapter(brandList,false)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBrandBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val brandRV = binding.brandRV
        brandRV.layoutManager = GridLayoutManager(context,2,LinearLayoutManager.VERTICAL,false)
        brandRV.setHasFixedSize(true)
        brandList = setDataInList()
        brandAdapter = BrandAdapter(brandList,false)
        brandAdapter.setItemClickListener(object : BrandAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val action = BrandFragmentDirections.actionBrandFragmentToBrandMenuFragment()
                findNavController().navigate(action)
            }
        })
        brandRV.adapter = brandAdapter
    }

    private fun setDataInList(): ArrayList<Brand> {
        val items: ArrayList<Brand> = ArrayList()
        items.add(Brand( "스타벅스",R.drawable.im_dummy_starbucks))
        return items
    }
}