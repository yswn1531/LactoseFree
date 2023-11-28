package com.yoon.lactosefree.brand.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.yoon.lactosefree.brand.BrandViewModel
import com.yoon.lactosefree.common.ViewBindingBaseFragment
import com.yoon.lactosefree.databinding.FragmentBrandBinding
import kotlinx.coroutines.launch

/**
 * Brand fragment
 *
 * 필요한 것
 * 1. Brand 이름, Brand 이미지
 */
class BrandFragment : ViewBindingBaseFragment<FragmentBrandBinding>(FragmentBrandBinding::inflate){

    private val viewModel : BrandViewModel by viewModels()
    private var brandAdapter =  BrandAdapter(listOf())


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
        initRecyclerView()

    }

    private fun initRecyclerView(){
        viewModel.getBrand()
        val brandRV = binding.brandRV
        brandRV.layoutManager = GridLayoutManager(context,2,LinearLayoutManager.VERTICAL,false)
        brandRV.setHasFixedSize(true)
        viewLifecycleOwner.lifecycleScope.launch {
                viewModel.brand.collect{
                    it?.let {
                        brandAdapter = BrandAdapter(it)
                        brandAdapter.setItemClickListener(object : BrandAdapter.OnItemClickListener {
                            override fun onClick(v: View, position: Int) {
                                val action = BrandFragmentDirections.actionBrandFragmentToBrandMenuFragment(it[position].brandName)
                                findNavController().navigate(action)
                            }
                        })
                    }
                }
            }
        brandRV.adapter = brandAdapter
    }
}