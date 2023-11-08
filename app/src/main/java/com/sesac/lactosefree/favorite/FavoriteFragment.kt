package com.sesac.lactosefree.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sesac.lactosefree.R
import com.sesac.lactosefree.brand.Brand
import com.sesac.lactosefree.common.ViewBindingBaseFragment
import com.sesac.lactosefree.databinding.FragmentFavoriteBinding

class FavoriteFragment : ViewBindingBaseFragment<FragmentFavoriteBinding>(FragmentFavoriteBinding::inflate){

    private var favoriteList = arrayListOf<Brand>()
    private var favoriteAdapter =  FavoriteAdapter(favoriteList)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val favoriteRV = binding.favoriteRV
        favoriteRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        favoriteRV.setHasFixedSize(true)
        favoriteList = setDataInList()
        favoriteAdapter = FavoriteAdapter(favoriteList)
        favoriteAdapter.setItemClickListener(object : FavoriteAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                val action = FavoriteFragmentDirections.actionFavoriteFragmentToFavoriteDetailFragment()
                findNavController().navigate(action)
            }
        })
        favoriteRV.adapter = favoriteAdapter
    }

    private fun setDataInList(): ArrayList<Brand> {
        val items: ArrayList<Brand> = ArrayList()
        items.add(Brand( "에스프레소",R.drawable.im_dummy_esprosso))
        items.add(Brand( "콜드브루",R.drawable.im_dummy_coldbrew))
        items.add(Brand( "에스프레소",R.drawable.im_dummy_esprosso))
        items.add(Brand( "콜드브루",R.drawable.im_dummy_coldbrew))
        items.add(Brand( "에스프레소",R.drawable.im_dummy_esprosso))
        items.add(Brand( "콜드브루",R.drawable.im_dummy_coldbrew))
        items.add(Brand( "에스프레소",R.drawable.im_dummy_esprosso))
        items.add(Brand( "콜드브루",R.drawable.im_dummy_coldbrew))
        return items
    }

}