package com.hungames.cookingsocial.ui.map

import android.widget.Toast
import androidx.lifecycle.*
import com.hungames.cookingsocial.data.NeighborUserRepository
import com.hungames.cookingsocial.data.Result
import com.hungames.cookingsocial.data.model.UserNeighbors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val repository: NeighborUserRepository): ViewModel() {

    private val _pointOfInterests = MutableLiveData<List<UserNeighbors>>()
    val pointOfInterests: LiveData<List<UserNeighbors>> = _pointOfInterests

    private val _hasFetchedData = MutableLiveData<Boolean>()
    val hasFetchedData: LiveData<Boolean> = _hasFetchedData

    init {
        //
        _hasFetchedData.value = false
    }

    fun getNearbyUsers(location: String){
        viewModelScope.launch {
            val result = repository.getNearbyUsers(location)
            if (result is Result.Success){
                _hasFetchedData.value = true
                _pointOfInterests.value = result.data
            }
        }

    }

}