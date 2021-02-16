package com.hungames.cookingsocial.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.hungames.cookingsocial.data.model.UserNeighbors
import com.hungames.cookingsocial.databinding.FragmentDetailBinding
import com.hungames.cookingsocial.util.IntentSignals
import com.hungames.cookingsocial.util.TAG_DISH
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

/* Add chipgroup to filter out dishes by NutritionType*/

@AndroidEntryPoint
class DetailFragment : Fragment() {

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
        val dishAdapter = DetailAdapter()
        binding.viewModel = detailViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recyclerViewDish.apply {
            adapter = dishAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(false)
        }
        // subscribeUi(dishAdapter)
        detailViewModel.spinner.observe(viewLifecycleOwner) { show ->
            binding.spinner.visibility = if (show) View.VISIBLE else View.GONE
        }
        detailViewModel.snackbar.observe(viewLifecycleOwner) { text ->
            text?.let {
                Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
                detailViewModel.onSnackbarShown()
            }
        }
        detailViewModel.dishesFlow.observe(viewLifecycleOwner){
            Snackbar.make(binding.root, "Finished loading: ${it.toString()}", Snackbar.LENGTH_SHORT).show()
        }

        return binding.root
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