package com.yoon.lactosefree.favorite.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yoon.lactosefree.common.ViewBindingBaseFragment
import com.yoon.lactosefree.databinding.FragmentFavoriteBinding
import com.yoon.lactosefree.favorite.Favorite
import com.yoon.lactosefree.favorite.FavoriteViewModel

class FavoriteFragment : ViewBindingBaseFragment<FragmentFavoriteBinding>(FragmentFavoriteBinding::inflate){

    private lateinit var favoriteAdapter: FavoriteAdapter
    private val viewModel : FavoriteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observerSetup()
    }

    private fun observerSetup(){
        viewModel.getAllResult()?.observe(viewLifecycleOwner){favorite ->
            favorite?.let {
                    Log.e("SET-RESULT", it.toString())
                    favoriteAdapter.setProductList(it)
            }
        }
    }

    fun initRecyclerView(){
        with(binding.favoriteRV){
            favoriteAdapter = FavoriteAdapter()
            favoriteAdapter.setProductList(emptyList())
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = favoriteAdapter
            favoriteAdapter.setItemClickListener(object : FavoriteAdapter.OnItemClickListener {
                override fun onClick(v: View, position: Int) {
                    val brandNameArgs = favoriteAdapter.getProductList()[position].brandName
                    val beverageNameArgs = favoriteAdapter.getProductList()[position].brandBeverageName
                    Log.e("ARGUMENT","${brandNameArgs},${beverageNameArgs}")
                    val action = FavoriteFragmentDirections.actionFavoriteFragmentToFavoriteDetailFragment(brandNameArgs,beverageNameArgs)
                    findNavController().navigate(action)
                }
            })

        }
    }
}