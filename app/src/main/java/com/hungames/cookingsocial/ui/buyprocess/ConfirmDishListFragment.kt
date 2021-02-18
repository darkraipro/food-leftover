package com.hungames.cookingsocial.ui.buyprocess

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.hungames.cookingsocial.databinding.ConfirmDishListFragmentBinding
import com.hungames.cookingsocial.ui.detail.DetailViewModel
import com.hungames.cookingsocial.util.IntentSignals
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class ConfirmDishListFragment : Fragment() {


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
        val adapter = ConfirmDishAdapter()
        binding.lifecycleOwner = viewLifecycleOwner
        binding.apply {
            detailVM = detailViewModel
            recyclerviewOrder.adapter = adapter
            recyclerviewOrder.layoutManager = LinearLayoutManager(requireContext())
            recyclerviewOrder.setHasFixedSize(true)
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            detailViewModel.dishEvent.collect{ event ->
                if (event is DetailViewModel.DishEvent.NavigateToBuy){
                    Snackbar.make(binding.root, "Navigated to Secure Checkout", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
        return binding.root
    }


}