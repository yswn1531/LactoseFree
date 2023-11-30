package com.yoon.lactosefree.brand.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.activityViewModels
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.job
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

    private lateinit var brandMenuAdapter: BrandMenuAdapter
    private val viewModel: BrandViewModel by activityViewModels()
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
        viewModel.getIncludeMilk(brandArgs.brandName)
        insteadMilkDialog()
        viewModel.getBrandBeverageImageFromStorage(brandArgs.brandName)
        initView()
        //툴바에 뒤로가기 버튼
        binding.brandMenuTB.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        //툴바에 필터기능
        //filterSelectEvent()
    }


    fun initView() {
        binding.brandMenuTB.title = brandArgs.brandName
        tabAddFromData()
        tabSelectEvent()
        resultByCategory()
        initRecyclerView()
    }


    /**
     * 탭의 마진 설정
     *
     * @param tabLayout
     * @param marginEnd
     */
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
     * 탭 선택시 해당하는 category에 맞는 음료 목록을 viewModel에서 호출하는 함수
     *
     */
    fun tabSelectEvent() {
        binding.menuTL.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    if (it.isSelected) {
                        Log.e("selectTab", "${it.text}")
                        viewModel.getBrandBeverage("${it.text}")
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

    }

    /**
     * RecyclerView 초기 설정 함수
     *
     */
    private fun initRecyclerView() {
        val brandRV = binding.brandMenuRV
        brandRV.layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
    }

    /**
     * 카테고리에 맞는 음료 호출 한 결과값을 recyclerview에 적용하는 함수
     *
     */

    private fun resultByCategory() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.brandBeverage.collect {
                it?.let {
                    if (!::brandMenuAdapter.isInitialized) {
                        brandMenuAdapter = BrandMenuAdapter()
                        recyclerItemClickEvent()
                        favoriteButtonClickEvent()
                        brandMenuAdapter.addBrandBeverages(emptyList())
                        binding.brandMenuRV.adapter = brandMenuAdapter

                    }
                    if (it.isNotEmpty()) {
                        brandMenuAdapter.addBrandBeverages(it)
                    }
                }
            }

        }

    }


    /**
     * Recycler item click시 해당 영양성분을 보여주는 Dialog 표시
     *
     */
    fun recyclerItemClickEvent() {
        brandMenuAdapter.setItemClickListener(object : BrandMenuAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                val getBeverage = brandMenuAdapter.getProductList()[position]
                MenuDetailDialog(
                    MenuDetail(
                        getBeverage.beverageKcal,
                        getBeverage.beverageSodium,
                        getBeverage.beverageSugar,
                        getBeverage.beverageFat,
                        getBeverage.beverageProtein,
                        getBeverage.beverageCaffeine,
                        getBeverage.includeMilk
                    ), requireContext()
                ).show()
            }
        })
    }

    /**
     * Favorite button 클릭시 해당 음료가 좋아하는 목록에 추가, 삭제
     *
     */
    fun favoriteButtonClickEvent() {
        brandMenuAdapter.favoriteButtonClickListener(object :
            BrandMenuAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                if (brandMenuAdapter.getProductList()[position].favorite) {
                    Log.e("FAVORITE1",brandMenuAdapter.getProductList()[position].favorite.toString())
                    viewModel.deleteFavoriteBeverage(brandMenuAdapter.getProductList()[position].beverageName)
                    brandMenuAdapter.getProductList()[position].favorite = false
                    viewModel.setBrandBeverage(
                        brandMenuAdapter.getProductList()[position].beverageName,
                        false
                    )
                    /*
                 *  할일
                 *  1. 현재 보여지는 값 변경
                 *  2. RoomDB에 현재 position에 음료의 값들을 가지고 insert
                 */
                } else {
                    Log.e("FAVORITE2",brandMenuAdapter.getProductList()[position].favorite.toString())
                    brandMenuAdapter.getProductList()[position].favorite = true
                    viewModel.insertFavoriteBeverage(brandMenuAdapter.getProductList()[position])
                    viewModel.setBrandBeverage(
                        brandMenuAdapter.getProductList()[position].beverageName, true
                    )
                }
            }

        })
    }

    /**
     * Tab에 가져온 Category를 추가하는 함수
     *
     */
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

    /**
     * 대체 우유 정보를 보여주는 Dialog
     *
     */
    private fun insteadMilkDialog() {
        lifecycleScope.launch {
            viewModel.brandInsteadMilk.collect { brand ->
                brand?.let {
                    Log.e("TEST-DIALOG", "DIALOG")
                    CustomDialog("대체 우유 : ${it[0].insteadMilk}", requireContext()).show()
                }
            }
        }
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

}