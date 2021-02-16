package com.hungames.cookingsocial.ui.detail

import androidx.lifecycle.*
import com.hungames.cookingsocial.data.DishesRepository
import com.hungames.cookingsocial.data.model.Dishes
import com.hungames.cookingsocial.data.model.UserNeighbors
import com.hungames.cookingsocial.util.TAG_DISH
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class DetailViewModel @AssistedInject constructor(
    private val dishRepository: DishesRepository,
    @Assisted private val user: UserNeighbors?
) : ViewModel() {

    private val listOfFood = listOf<Dishes>(
        Dishes(10, "test", "Testfood", "testDesc", 10.50f, quantity = 4),
        Dishes(11, "test2", "Testfood2", "testDesc", 9.45f, quantity = 11)
    )
    private val _spinner = MutableLiveData<Boolean>()
    val spinner: LiveData<Boolean> = _spinner

    private val _snackbar = MutableLiveData<String?>()
    val snackbar: LiveData<String?> = _snackbar

    private val _dishes = MutableLiveData<List<Dishes>>()
    val dishes: LiveData<List<Dishes>> = _dishes

    val dishesFlow: LiveData<List<Dishes>> = dishRepository.getDishesFlow(user!!.uid).asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

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
            _dishes.value = listOfFood
            Timber.tag(TAG_DISH).i("Viewmodel init fun dishes: ${dishesFlow.value}")
        }
    }


    private fun getDishes(uid: String) = viewModelScope.launch {
        try {
            val listResult = dishRepository.getDishes(user!!.uid)
            Timber.tag(TAG_DISH).i("Getting dishes from Repository done. Result: $listResult")
            if (listResult != null) {
                _dishes.value = listResult!!
            } else {
                _dishes.value = ArrayList()
            }
        } catch (e: Exception) {
            _dishes.value = ArrayList()
        }
    }

    fun onSnackbarShown() {
        _snackbar.value = null
    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(user: UserNeighbors?): DetailViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            user: UserNeighbors?
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(user) as T
            }
        }
    }


}