package com.hungames.cookingsocial.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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

    private val detailViewModel: DetailViewModel by viewModels(){
        DetailViewModel.provideFactory(detailViewModelFactory, requireActivity().intent.extras?.getParcelable(IntentSignals.USER_DATA))
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDetailBinding.inflate(inflater, container, false)
        val textView: TextView = binding.userNameInfo
        val intentExtras = requireActivity().intent.extras ?: return binding.root
        val user = intentExtras.getParcelable<UserNeighbors>(IntentSignals.USER_DATA)
        Timber.tag(TAG_DISH).i("Fragment - intentExtras: ${intentExtras.getParcelable<UserNeighbors>(IntentSignals.USER_DATA)}")
        detailViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })

        return binding.root
    }
}