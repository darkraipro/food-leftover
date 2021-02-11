package com.hungames.cookingsocial.ui.dish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hungames.cookingsocial.databinding.FragmentDishDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DishDetailFragment : Fragment() {

    companion object {
        fun newInstance() = DishDetailFragment()
    }

    private val viewModel: DishDetailViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentDishDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}