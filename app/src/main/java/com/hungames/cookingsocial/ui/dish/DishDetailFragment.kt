package com.hungames.cookingsocial.ui.dish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.hungames.cookingsocial.databinding.FragmentDishDetailBinding
import com.hungames.cookingsocial.util.TAG_DISH
import com.hungames.cookingsocial.util.exhaustive
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class DishDetailFragment : Fragment() {

    companion object {
        fun newInstance() = DishDetailFragment()
    }

    private val viewModel: DishDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDishDetailBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        Timber.tag(TAG_DISH)
            .i("Fragment Manager backStackEntryCount: ${parentFragmentManager.backStackEntryCount}")
        Timber.tag(TAG_DISH).i(parentFragmentManager.fragments.toString())

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.channelFlow.collect { event ->
                when (event) {
                    is DishDetailViewModel.DishDetailEvent.NavigateToDetailFragment -> findNavController().navigate(
                        DishDetailFragmentDirections.actionDishDetailFragmentToDetailFragment()
                    )
                }.exhaustive
            }
        }
        return binding.root
    }


}