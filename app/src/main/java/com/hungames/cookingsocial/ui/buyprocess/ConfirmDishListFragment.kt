package com.hungames.cookingsocial.ui.buyprocess

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.hungames.cookingsocial.databinding.ConfirmDishListFragmentBinding
import com.hungames.cookingsocial.ui.detail.DetailViewModel
import com.hungames.cookingsocial.util.IntentSignals
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ConfirmDishListFragment : Fragment() {

    private val viewModel: ConfirmDishListViewModel by viewModels()

    @Inject
    lateinit var detailViewModelFactory: DetailViewModel.AssistedFactory

    private val detailViewModel: DetailViewModel by activityViewModels {
        DetailViewModel.provideFactory(
            detailViewModelFactory,
            requireActivity().intent.extras?.getParcelable(IntentSignals.USER_DATA)
        )
    }

    companion object {
        fun newInstance() = ConfirmDishListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ConfirmDishListFragmentBinding.inflate(inflater, container, false)
        binding.apply {
            detailVM = detailViewModel
            confirmDishVM = viewModel
        }
        return binding.root
    }


}