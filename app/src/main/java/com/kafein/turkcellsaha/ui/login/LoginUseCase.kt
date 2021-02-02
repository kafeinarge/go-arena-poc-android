package com.kafein.turkcellsaha.ui.login

import com.kafein.turkcellsaha.Resource
import com.kafein.turkcellsaha.data.RemoteRepository
import com.kafein.turkcellsaha.data.model.LoginRequest
import com.kafein.turkcellsaha.data.model.TokenResponse
import com.kafein.turkcellsaha.map
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