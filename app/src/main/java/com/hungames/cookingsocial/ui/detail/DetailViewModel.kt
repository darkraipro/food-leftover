package com.hungames.cookingsocial.ui.detail

import android.os.Bundle
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.hungames.cookingsocial.data.DishesRepository
import com.hungames.cookingsocial.data.model.Dishes
import com.hungames.cookingsocial.data.model.UserNeighbors
import com.hungames.cookingsocial.util.IntentSignals
import com.hungames.cookingsocial.util.TAG_MAP
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class DetailViewModel @AssistedInject constructor(private val dishRepository: DishesRepository, @Assisted private val user: UserNeighbors?) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "${user?.email}"
    }
    val text: LiveData<String> = _text

    // TODO: Use user id to get the associated table entity for food products

    init {
        Timber.tag(TAG_MAP).i("Init DetailVM. Getting foods.")

    }

    fun getDishes(uid: String) = viewModelScope.launch {
        dishRepository.getDishes(uid)
    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(user: UserNeighbors?): DetailViewModel
    }

    companion object{
        fun provideFactory(assistedFactory: AssistedFactory, user: UserNeighbors?): ViewModelProvider.Factory = object : ViewModelProvider.Factory{

            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(user) as T
            }
        }
    }


}