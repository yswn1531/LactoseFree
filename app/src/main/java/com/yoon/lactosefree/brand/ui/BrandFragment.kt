package com.yoon.lactosefree.brand.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
 */
class BrandFragment : ViewBindingBaseFragment<FragmentBrandBinding>(FragmentBrandBinding::inflate) {

    private val viewModel: BrandViewModel by viewModels()
    private lateinit var brandAdapter: BrandAdapter


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
        viewModel.getBrandImageFromStorage()
        viewModel.getBrand()
        initRecyclerView()
    }


    private fun initRecyclerView() {
        val brandRV = binding.brandRV
        brandRV.layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
        //brandRV.setHasFixedSize(true)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel.brand.collect{
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
                            brandAdapter.addBrands(it)
                            binding.brandRV.adapter = brandAdapter
                        }
                    }
                }
            }
        }
    }
}