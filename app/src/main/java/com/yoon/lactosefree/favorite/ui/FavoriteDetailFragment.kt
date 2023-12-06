package com.yoon.lactosefree.favorite.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding4.view.clicks
import com.yoon.lactosefree.R
import com.yoon.lactosefree.common.ViewBindingBaseFragment
import com.yoon.lactosefree.databinding.FragmentFavoriteDetailBinding
import com.yoon.lactosefree.favorite.Favorite
import com.yoon.lactosefree.favorite.FavoriteViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class FavoriteDetailFragment : ViewBindingBaseFragment<FragmentFavoriteDetailBinding>(
    FragmentFavoriteDetailBinding::inflate){

    private val viewModel : FavoriteViewModel by viewModels()
    private val args : FavoriteDetailFragmentArgs by navArgs()
    private var tempUri = Uri.EMPTY
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        actionbarEvent()
    }

    @SuppressLint("ResourceAsColor")
    private fun initView(){
        viewModel.findFavorite(args.beverageName)
        viewModel.getSearchResults().observe(viewLifecycleOwner){ favorite ->
            favorite.let {
                if (it.isNotEmpty()){
                    with(binding){
                        favoriteMenuTB.title = it[0].brandBeverageName
                        tempUri = it[0].imageUri
                        Glide.with(favoriteDetailImage.context)
                            .load(it[0].imageUri)
                            .into(favoriteDetailImage)
                        favoriteDetailRatingBar.rating = it[0].rating
                        favoriteDetailText.setText(it[0].content)
                        favoriteDetailText.isEnabled = favoriteDetailText.text?.isEmpty() == true
                        favoriteDetailText.setTextColor(R.color.PrimaryText)
                        compositeDisposable.add(
                            favoriteDetailEditButton
                                .clicks()
                                .observeOn(Schedulers.io())
                                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    favoriteDetailText.isEnabled = !favoriteDetailText.isEnabled
                                    Toast.makeText(context, resources.getString(R.string.updateMode), Toast.LENGTH_SHORT).show()
                                }, {
                                    Log.e("RX_ERROR", compositeDisposable.toString())
                                })
                        )
                    }
                }else  Log.e("EMPTY-FAVORITE","EMPTY-FAVORITE")
            }
        }
    }

    private fun actionbarEvent(){
        with(binding){
            favoriteMenuTB.setNavigationOnClickListener {
                viewModel.updateFavorite(
                    Favorite(
                    brandName = args.brandName,
                    brandBeverageName = args.beverageName,
                    rating = favoriteDetailRatingBar.rating,
                    content = favoriteDetailText.text.toString(),
                        imageUri = tempUri
                    )
                )
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

}