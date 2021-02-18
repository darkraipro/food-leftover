package com.hungames.cookingsocial.ui.detail

import android.widget.EditText
import androidx.lifecycle.*
import com.google.android.material.checkbox.MaterialCheckBox
import com.hungames.cookingsocial.data.DishesRepository
import com.hungames.cookingsocial.data.model.Dishes
import com.hungames.cookingsocial.data.model.Order
import com.hungames.cookingsocial.data.model.UserNeighbors
import com.hungames.cookingsocial.util.TAG_DISH
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

const val TOO_MUCH_TO_ORDER = "Quantity is over the limit"
const val NO_DISH_SELECTED = "Select at least one dish"

class DetailViewModel @AssistedInject constructor(
    private val dishRepository: DishesRepository,
    @Assisted private val user: UserNeighbors?
) : ViewModel() {

    private val _spinner = MutableLiveData<Boolean>()
    val spinner: LiveData<Boolean> = _spinner

    private val _snackbar = MutableLiveData<String?>()
    val snackbar: LiveData<String?> = _snackbar

    val dishesFlow: LiveData<List<Dishes>> = dishRepository.getDishesFlow(user!!.uid)
        .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _userDesc = MutableLiveData<String>()
    val userDesc: LiveData<String> = _userDesc

    private val dishEventChannel = Channel<DishEvent>()
    val dishEvent = dishEventChannel.receiveAsFlow()

    val _dishList = HashMap<Long, Pair<Dishes, Int>>()

    private val _totalPrice = MutableLiveData<Float>()
    val totalPrice: LiveData<Float> = _totalPrice

    init {
        Timber.tag(TAG_DISH).i("Init DetailVM. Getting foods with user: $user")
        user?.let {
            _email.value = it.email
            _userDesc.value = it.userDesc
        }
        _totalPrice.value = 0f
        Timber.tag(TAG_DISH)
            .i("Is flow stream done processing after init call? If null, then no: ${dishesFlow.value}")
    }

    fun onSnackbarShown() {
        _snackbar.value = null
    }

    fun onDishClicked(dish: Dishes) = viewModelScope.launch {
        dishEventChannel.send(DishEvent.NavigateToDishDetailScreen(dish))
    }

    fun onDishCheckBoxClicked(
        dish: Dishes,
        quantity: Int,
        checkBox: MaterialCheckBox,
        editText: EditText
    ) {
        if (quantity > dish.quantity) {
            displayTextMessage(TOO_MUCH_TO_ORDER)
            checkBox.isChecked = false
        } else {
            _dishList[dish.id] = Pair(dish, quantity)
            updateTotalPrice(dish.dishPrice * quantity)
            editText.isEnabled = false
        }
    }

    fun onDishCheckBoxUnClicked(dish: Dishes, editText: EditText) {
        _dishList.remove(dish.id)
        val quantity = editText.text.toString().toInt()
        updateTotalPrice(-dish.dishPrice * quantity)
        editText.isEnabled = true
    }

    private fun updateTotalPrice(price: Float){
        _totalPrice.value = _totalPrice.value?.plus(price)
    }

    fun onBuyFloatButtonClicked() = viewModelScope.launch {
        if (_dishList.isNotEmpty()) {
            dishEventChannel.send(DishEvent.NavigateToConfirmBuyOrder)
        } else {
            displayTextMessage(NO_DISH_SELECTED)
        }
    }

    fun displayTextMessage(msg: String) {
        _snackbar.value = msg
    }

    fun convertDishMapToOrderList(): List<Order>{
        val result = mutableListOf<Order>()
        _dishList.forEach { entry ->
            result.add(Order(entry.value.second, entry.value.first))
        }
        return result
    }

    fun onProceedCheckoutClicked() = viewModelScope.launch {
        dishEventChannel.send(DishEvent.NavigateToBuy)
    }

    sealed class DishEvent {
        data class NavigateToDishDetailScreen(val dish: Dishes) : DishEvent()
        object NavigateToConfirmBuyOrder: DishEvent()
        object NavigateToBuy: DishEvent()
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