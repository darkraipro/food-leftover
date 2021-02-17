package com.hungames.cookingsocial.ui.dish

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.hungames.cookingsocial.data.model.Dishes
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


class DishDetailViewModel @AssistedInject constructor(
    @Assisted private val state: SavedStateHandle,
) : ViewModel() {
    val dish = state.get<Dishes>("dish")
}