package com.yoon.lactosefree.brand.ui

import android.os.Bundle
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
import com.yoon.lactosefree.brand.BrandViewModel
import com.yoon.lactosefree.common.LoadingDialog
import com.yoon.lactosefree.common.ViewBindingBaseFragment
import com.yoon.lactosefree.databinding.FragmentBrandBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Brand fragment
 *
 */
class BrandFragment : ViewBindingBaseFragment<FragmentBrandBinding>(FragmentBrandBinding::inflate) {

    private val viewModel: BrandViewModel by activityViewModels()
    private lateinit var loading: LoadingDialog
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
        loading = LoadingDialog(requireContext())
        viewModel.getBrand()
        initRecyclerView()
        collectResult()
        //loading.show()
    }

    private fun initRecyclerView() {
        with(binding.brandRV){
            layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
            brandAdapter = BrandAdapter {
                val action =
                    BrandFragmentDirections.actionBrandFragmentToBrandMenuFragment(
                        it.brandName
                    )
                findNavController().navigate(action)
            }
            adapter = brandAdapter
        }

    }

    private fun collectResult(){
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.brand.collect {
                    it?.let {
                        brandAdapter.submitList(it)
                    }
                }
            }
        }
    }

}