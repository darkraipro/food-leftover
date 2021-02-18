package com.hungames.cookingsocial.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.checkbox.MaterialCheckBox
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
import javax.inject.Singleton

// TODO: Add chipgroup to filter out dishes by NutritionType

@AndroidEntryPoint
class DetailFragment : Fragment(), DetailAdapter.OnItemClickListener {


    @Inject
    lateinit var detailViewModelFactory: DetailViewModel.AssistedFactory

    private val detailViewModel: DetailViewModel by activityViewModels {
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
                // TODO: remove the option to select the dish to display more information. Instead change itemholder to display all information. Refactor DetailAdapter and VM
                when (event){
                    is DetailViewModel.DishEvent.NavigateToDishDetailScreen -> {

                    }
                    is DetailViewModel.DishEvent.NavigateToConfirmBuyOrder -> {
                        findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToConfirmFoodList())
                    }
                    else -> {

                    }
                }.exhaustive
            }
        }

        return binding.root
    }

    override fun onItemClick(dish: Dishes) {
        detailViewModel.onDishClicked(dish)
    }

    override fun onCheckBoxUnClick(dish: Dishes, editText: EditText) {
        detailViewModel.onDishCheckBoxUnClicked(dish, editText)
    }

    override fun onCheckBoxClick(dish: Dishes, quantity: Int, checkboxWantToBuy: MaterialCheckBox, editText: EditText) {
        detailViewModel.onDishCheckBoxClicked(dish, quantity, checkboxWantToBuy, editText)
    }

    override fun onErrorCheck(msg: String) {
        detailViewModel.displayTextMessage(msg)
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