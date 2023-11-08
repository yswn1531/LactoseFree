package com.sesac.lactosefree.brand.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.sesac.lactosefree.R
import com.sesac.lactosefree.brand.Brand
import com.sesac.lactosefree.brand.BrandAdapter
import com.sesac.lactosefree.common.CustomDialog
import com.sesac.lactosefree.brand.Detail
import com.sesac.lactosefree.common.MenuDetailDialog
import com.sesac.lactosefree.common.ViewBindingBaseFragment
import com.sesac.lactosefree.databinding.FragmentBrandMenuBinding

class BrandMenuFragment : ViewBindingBaseFragment<FragmentBrandMenuBinding>(FragmentBrandMenuBinding::inflate){

    private var brandList = arrayListOf<Brand>()
    private var brandAdapter =  BrandAdapter(brandList,true)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBrandMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CustomDialog("대체 우유 : 오트밀크, 두유",requireContext()).show()
        setTabItemMargin(binding.menuTL,30)
        val brandRV = binding.brandMenuRV
        brandRV.layoutManager = GridLayoutManager(context,2, LinearLayoutManager.VERTICAL,false)
        brandRV.setHasFixedSize(true)
        brandList = setDataInList()
        brandAdapter = BrandAdapter(brandList,true)
        brandAdapter.setItemClickListener(object : BrandAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                val dialog = MenuDetailDialog(Detail(12.2f,2.4f,0f,0f,0.9f,204.2f),requireContext())
                dialog.show()
            }
        })
        brandRV.adapter = brandAdapter

        binding.brandMenuTB.setOnMenuItemClickListener{item ->
            when(item.itemId){
                R.id.filterCaffeineFree -> {
                    Toast.makeText(context,"카페인 프리",Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.filterLowKcal -> {
                    Toast.makeText(context,"저칼로리",Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.filterLowSugar -> {
                    Toast.makeText(context,"저당",Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        binding.brandMenuTB.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

    }




    private fun setTabItemMargin(tabLayout: TabLayout, marginEnd: Int) {
        val tabs = tabLayout.getChildAt(0) as ViewGroup
        for (i in 0 until tabs.childCount) {
            val tab = tabs.getChildAt(i)
            val lp = tab.layoutParams as LinearLayout.LayoutParams
            lp.marginEnd = marginEnd
            tab.layoutParams = lp
            tabLayout.requestLayout()
        }
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