package com.hungames.cookingsocial.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.hungames.cookingsocial.data.model.Dishes
import com.hungames.cookingsocial.databinding.FragmentDetailBinding
import com.hungames.cookingsocial.util.IntentSignals
import com.hungames.cookingsocial.util.TAG_DISH
import com.hungames.cookingsocial.util.exhaustive
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Inject

// TODO: Add chipgroup to filter out dishes by NutritionType

@AndroidEntryPoint
class DetailFragment : Fragment(), DetailAdapter.OnItemClickListener {


    @Inject
    lateinit var detailViewModelFactory: DetailViewModel.AssistedFactory

    private val detailViewModel: DetailViewModel by viewModels() {
        DetailViewModel.provideFactory(
            detailViewModelFactory,
            requireActivity().intent.extras?.getParcelable(IntentSignals.USER_DATA)
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDetailBinding.inflate(inflater, container, false)
        val dishAdapter = DetailAdapter(this)
        binding.viewModel = detailViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recyclerViewDish.apply {
            adapter = dishAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(false)
        }
        // subscribeUi(dishAdapter)
        detailViewModel.snackbar.observe(viewLifecycleOwner) { text ->
            text?.let {
                Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
                detailViewModel.onSnackbarShown()
            }
        }
        Timber.tag(TAG_DISH).i("Frag Manager backStackEntryCount: ${parentFragmentManager.backStackEntryCount}")
        Timber.tag(TAG_DISH).i(parentFragmentManager.fragments.toString())
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            detailViewModel.dishEvent.collect { event ->
                when (event){
                    is DetailViewModel.DishEvent.NavigateToDishDetailScreen -> {
                        findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToDishDetailFragment(event.dish))
                    }
                }.exhaustive
            }
        }

        return binding.root
    }

    override fun onItemClick(dish: Dishes) {
        detailViewModel.onDishClicked(dish)
    }


    private fun subscribeUi(adapter: DetailAdapter) {
        detailViewModel.dishesFlow.observe(viewLifecycleOwner) { list ->
            if (list != null) {
                adapter.submitList(list)
            } else {
                adapter.submitList(emptyList())
            }
        }
    }
}