package com.yoon.lactosefree.brand.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.yoon.lactosefree.brand.BrandBeverage
import com.yoon.lactosefree.brand.BrandViewModel
import com.yoon.lactosefree.brand.MenuDetail
import com.yoon.lactosefree.common.CustomDialog
import com.yoon.lactosefree.common.ViewBindingBaseFragment
import com.yoon.lactosefree.databinding.FragmentBrandMenuBinding
import kotlinx.coroutines.launch

/**
 *  해야할 일
 *  1. 대체 우유 정보
 *  2. 음료 정보
 *  3. argument로 받은 값으로 브랜드 요청
 *
 */
class BrandMenuFragment :
    ViewBindingBaseFragment<FragmentBrandMenuBinding>(FragmentBrandMenuBinding::inflate) {

    private var brandBeverageList = ArrayList<BrandBeverage>()
    private var brandAdapter = BrandMenuAdapter(mutableListOf())
    private val viewModel: BrandViewModel by viewModels()
    private lateinit var categoryList: List<String>
    private val brandArgs: BrandMenuFragmentArgs by navArgs()
    /*
    private var filterCaffeine = false
    private var filterSugar = false
    private var filterKcal = false
    */

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
        //CustomDialog("대체 우유 : 두유 ",requireContext()).show()
        insteadMilkDialog()
        initView()

        //툴바에 뒤로가기 버튼
        binding.brandMenuTB.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        //툴바에 필터기능
        //filterSelectEvent()
    }


    /*fun filterSelectEvent() {
        binding.brandMenuTB.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.filterCaffeineFree -> {
                    filterCaffeine = !filterCaffeine
                    item.isChecked = !item.isChecked
                    Log.e("boolean",filterCaffeine.toString())
                    Toast.makeText(context, "카페인 프리", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.filterLowKcal -> {
                    Toast.makeText(context, "저칼로리", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.filterLowSugar -> {
                    Toast.makeText(context, "저당", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }
        }
    }*/

    fun initView() {
        binding.brandMenuTB.title = brandArgs.brandName
        tabAddFromData()
        tabSelectEvent()
        initRecyclerView()
    }


    private fun setTabItemMargin(tabLayout: TabLayout, marginEnd: Int) {
        val tabs = tabLayout.getChildAt(0) as ViewGroup
        for (i in 0 until tabs.childCount) {
            val tab = tabs.getChildAt(i)
            val layoutParams = tab.layoutParams as LinearLayout.LayoutParams
            layoutParams.marginEnd = marginEnd
            tab.layoutParams = layoutParams
            tabLayout.requestLayout()
        }
    }


    /**
     * List에서 category 값만 set에 저장 후 나오는 것만 탭에 추가
     *
     */

    fun tabSelectEvent() {
        binding.menuTL.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    if (it.isSelected) {
                        Log.e("selectTab","${it.text}")
                        viewModel.getBrandBeverage(brandArgs.brandName, "${it.text}")
                        resultByCategory()
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

    }


    private fun initRecyclerView() {
        val brandRV = binding.brandMenuRV
        brandRV.layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
        recyclerItemClickEvent()
        favoriteBtnClickEvent()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun resultByCategory() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.brandBeverage.collect {
                it?.let {
                    Log.e("TagBMF", it.toString())
                    brandAdapter = BrandMenuAdapter(it.toMutableList())
                    binding.brandMenuRV.adapter = brandAdapter
                    binding.brandMenuRV.adapter?.notifyDataSetChanged()
                }
            }
        }
    }


    fun recyclerItemClickEvent() {
        brandAdapter.setItemClickListener(object : BrandMenuAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                Toast.makeText(requireContext(), position.toString(), Toast.LENGTH_SHORT).show()
                MenuDetailDialog(
                    MenuDetail(
                        brandBeverageList[position].beverageKcal,
                        brandBeverageList[position].beverageSodium,
                        brandBeverageList[position].beverageSugar,
                        brandBeverageList[position].beverageFat,
                        brandBeverageList[position].beverageProtein,
                        brandBeverageList[position].beverageCaffeine,
                        brandBeverageList[position].includeMilk
                    ), requireContext()
                ).show()
            }
        })
    }

    fun favoriteBtnClickEvent() {
        brandAdapter.favoriteButtonClickListener(object : BrandMenuAdapter.OnItemClickListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onClick(v: View, position: Int) {
                if (brandAdapter.getList[position].favorite == true) {
                    brandAdapter.getList[position].favorite = false

                    /*
                     *  할일
                     *  1. List의 가져온 position에 favorite 값을 바꾸고 firebase에도 바꿔줘야함? firebase에 바꾸면 알아서 들어가지 않을까?
                     *      1.1 list의 값을 바꿀 필요가 있는가?
                     *      1.2 firebase에만 바꾸면 알아서 되는가?
                     *  2. RoomDB에 현재 position에 음료의 값들을 가지고 insert
                     */
                    viewModel.setBrandBeverage(brandAdapter.getList[position].beverageName, false)
                    binding.brandMenuRV.adapter?.notifyDataSetChanged()
                } else {
                    brandAdapter.getList[position].favorite = true
                    viewModel.setBrandBeverage(brandAdapter.getList[position].beverageName, true)
                    binding.brandMenuRV.adapter?.notifyDataSetChanged()
                }
            }

        })
    }

    private fun tabAddFromData() {
        viewModel.getBrandCategory(brandArgs.brandName)
        with(binding) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.brandBeverageCategory.collect {
                    it?.let {
                        categoryList = it
                        it.forEach {
                            menuTL.addTab(menuTL.newTab().setText(it))
                            setTabItemMargin(menuTL, 30)
                        }
                    }
                }
            }
        }
    }

    private fun insteadMilkDialog() {
        viewModel.getIncludeMilk(brandArgs.brandName)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.brandInsteadMilk.collect {
                it?.let {
                    it.forEach { brand ->
                        Log.e("TTT", brand.insteadMilk.toString())
                        CustomDialog("대체 우유 : ${brand.insteadMilk}", requireContext()).show()
                    }
                }
            }
        }
    }
}