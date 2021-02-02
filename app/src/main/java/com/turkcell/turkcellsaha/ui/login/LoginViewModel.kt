package com.turkcell.turkcellsaha.ui.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.turkcell.turkcellsaha.*
import com.turkcell.turkcellsaha.common.RxAwareViewModel
import com.turkcell.turkcellsaha.common.StatusViewState
import com.turkcell.turkcellsaha.data.model.LoginRequest
import com.turkcell.turkcellsaha.data.model.TokenResponse
import io.reactivex.android.schedulers.AndroidSchedulers

class LoginViewModel @ViewModelInject constructor(private val loginUseCase: LoginUseCase): RxAwareViewModel() {

    private val loginLiveData = MutableLiveData<TokenResponse>()
    val loginLiveData_: LiveData<TokenResponse> = loginLiveData

    private val status = MutableLiveData<StatusViewState>()
    val status_: LiveData<StatusViewState> = status

    fun login(username: String, password: String){
        loginUseCase
            .login(LoginRequest(username, password))
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                onTokenResultReady(it)
            }
            .subscribe({ resource ->
                onPTokenStatusResultReady(resource)
            }, {
            })
            .also { disposable += it }
    }

    private fun onPTokenStatusResultReady(resource: Resource<TokenResponse>) {
        val viewState = when (resource) {
            is Resource.Success -> StatusViewState(Status.Content)
            is Resource.Error -> StatusViewState(Status.Error(resource.exception))
            Resource.Loading -> StatusViewState(Status.Loading)
        }
        status.value = viewState
    }

    private fun onTokenResultReady(tokenResponse: TokenResponse) {
        loginLiveData.value = tokenResponse
    }

}