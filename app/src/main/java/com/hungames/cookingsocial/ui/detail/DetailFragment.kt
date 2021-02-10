package com.hungames.cookingsocial.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.hungames.cookingsocial.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint

/* Add chipgroup to filter out dishes by NutritionType*/

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private val detailViewModel: DetailViewModel by viewModels()
    private val detailFragmentArgs: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDetailBinding.inflate(inflater, container, false)
        val textView: TextView = binding.userNameInfo

        val user = detailFragmentArgs.user
        detailViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })

        return binding.root
    }
}