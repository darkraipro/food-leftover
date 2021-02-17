package com.hungames.cookingsocial.ui.dish

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hungames.cookingsocial.data.model.Dishes
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


class DishDetailViewModel @AssistedInject constructor(
    @Assisted private val state: SavedStateHandle,
) : ViewModel() {
    val dish = state.get<Dishes>("dish")

    private val channel = Channel<DishDetailEvent>()
    val channelFlow = channel.receiveAsFlow()

    fun onBackButtonClicked() = viewModelScope.launch {
        channel.send(DishDetailEvent.NavigateToDetailFragment)
    }

    sealed class DishDetailEvent{
        object NavigateToDetailFragment: DishDetailEvent()
    }
}