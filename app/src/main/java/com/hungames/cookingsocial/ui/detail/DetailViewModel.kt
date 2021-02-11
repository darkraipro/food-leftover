package com.hungames.cookingsocial.ui.detail

import androidx.lifecycle.*
import com.hungames.cookingsocial.data.DishesRepository
import com.hungames.cookingsocial.data.model.Dishes
import com.hungames.cookingsocial.data.model.UserNeighbors
import com.hungames.cookingsocial.util.TAG_DISH
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import timber.log.Timber

class DetailViewModel @AssistedInject constructor(private val dishRepository: DishesRepository, @Assisted private val user: UserNeighbors?) : ViewModel() {


    private val _dishes = MutableLiveData<List<Dishes>?>()
    val dishes: LiveData<List<Dishes>?> = _dishes

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _userDesc = MutableLiveData<String>()
    val userDesc: LiveData<String> = _userDesc

    init {
        Timber.tag(TAG_DISH).i("Init DetailVM. Getting foods.")
        Timber.tag(TAG_DISH).i("user is: $user")
        user?.let {
            _email.value = it.email
            _userDesc.value = it.userDesc
            getDishes(it.uid)
            Timber.tag(TAG_DISH).i("dishes: ${dishes.value}")
        }
    }

    private fun getDishes(uid: String) = viewModelScope.launch {
        _dishes.value = dishRepository.getDishes(user!!.uid)
    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(user: UserNeighbors?): DetailViewModel
    }

    companion object{
        fun provideFactory(assistedFactory: AssistedFactory, user: UserNeighbors?): ViewModelProvider.Factory = object : ViewModelProvider.Factory{

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(user) as T
            }
        }
    }


}