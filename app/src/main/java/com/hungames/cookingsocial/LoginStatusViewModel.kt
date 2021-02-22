package com.hungames.cookingsocial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hungames.cookingsocial.data.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginStatusViewModel @Inject constructor(private val preferencesManager: PreferencesManager) : ViewModel(){

    private val preferenceFlow = preferencesManager.preferenceFlow
    val statusFlow = preferenceFlow.conflate().flatMapLatest {
        flow<Boolean> {  it.loginStatus }
    }.asLiveData()

    private val _channel = Channel<StatusEvent>()
    val statusEvent = _channel.receiveAsFlow()

    fun sendEventLogin() = viewModelScope.launch {
        _channel.send(StatusEvent.Login)
        preferencesManager.setLoginStatus(true)
    }

    fun sendEventLogout() = viewModelScope.launch {
        _channel.send(StatusEvent.Logout)
    }

    sealed class StatusEvent{
        object Logout: StatusEvent()
        object Login: StatusEvent()
    }
}