package com.yoon.lactosefree.brand.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.yoon.lactosefree.brand.Brand
import com.yoon.lactosefree.brand.BrandViewModel
import com.yoon.lactosefree.common.ViewBindingBaseFragment
import com.yoon.lactosefree.databinding.FragmentBrandBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Brand fragment
 *
 * 필요한 것
 * 1. Brand 이름, Brand 이미지
 */
class BrandFragment : ViewBindingBaseFragment<FragmentBrandBinding>(FragmentBrandBinding::inflate) {

    private val viewModel: BrandViewModel by activityViewModels()
    private lateinit var brandAdapter: BrandAdapter
    private var tempList : List<Brand> = emptyList()

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
        Log.e("BRANDOPEN","BRANDOPEN")
        lifecycleScope.launch {
            viewModel.getBrand()
        }
        initRecyclerView()

    }


    private fun initRecyclerView() {
        val brandRV = binding.brandRV
        brandRV.layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
        //brandRV.setHasFixedSize(true)
        lifecycleScope.launch {
                viewModel.brand.observe(viewLifecycleOwner) {
                    it?.let {
                        if (!::brandAdapter.isInitialized) {
                            brandAdapter = BrandAdapter()
                            brandAdapter.addBrands(emptyList())
                            brandRV.adapter = brandAdapter
                            brandAdapter.setItemClickListener(object :
                                BrandAdapter.OnItemClickListener {
                                override fun onClick(v: View, position: Int) {
                                    val action =
                                        BrandFragmentDirections.actionBrandFragmentToBrandMenuFragment(
                                            it[position].brandName
                                        )
                                    findNavController().navigate(action)
                                }
                            })
                        }
                        if (it.isNotEmpty()) {
                            Log.e("RETURN_BRAND", it.toString())
                            binding.brandRV.adapter = brandAdapter
                            brandAdapter.addBrands(it)
                        }
                    }
                }

        }
    }
}