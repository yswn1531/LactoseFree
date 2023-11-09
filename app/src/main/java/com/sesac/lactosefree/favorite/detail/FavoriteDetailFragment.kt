package com.sesac.lactosefree.favorite.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.sesac.lactosefree.R
import com.sesac.lactosefree.common.ViewBindingBaseFragment
import com.sesac.lactosefree.databinding.FragmentFavoriteDetailBinding

class FavoriteDetailFragment : ViewBindingBaseFragment<FragmentFavoriteDetailBinding>(
    FragmentFavoriteDetailBinding::inflate){

    var editMode = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            favoriteDetailImage.setImageResource(R.drawable.im_dummy_esprosso)
            favoriteDetailText.setText(R.string.tempContext)
            favoriteDetailText.setTextColor(resources.getColor(R.color.PrimaryText))
            favoriteDetailEditButton.setOnClickListener {
                if (favoriteDetailText.text?.isNotEmpty() == true) {
                    favoriteDetailText.isEnabled = !favoriteDetailText.isEnabled
                    Toast.makeText(context, "수정모드", Toast.LENGTH_SHORT).show()
                }
            }

            favoriteMenuTB.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}