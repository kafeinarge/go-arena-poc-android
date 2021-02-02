package com.turkcell.turkcellsaha.ui.login

import com.turkcell.turkcellsaha.Resource
import com.turkcell.turkcellsaha.data.RemoteRepository
import com.turkcell.turkcellsaha.data.model.LoginRequest
import com.turkcell.turkcellsaha.data.model.TokenResponse
import com.turkcell.turkcellsaha.map
import io.reactivex.Observable
import javax.inject.Inject

class LoginUseCase @Inject constructor(private var repository: RemoteRepository) {

    fun login(request: LoginRequest): Observable<Resource<TokenResponse>> {
        return repository.login(request)
            .map { res ->
                res.map {
                    it
                }
            }.startWith(Resource.Loading)
    }
}