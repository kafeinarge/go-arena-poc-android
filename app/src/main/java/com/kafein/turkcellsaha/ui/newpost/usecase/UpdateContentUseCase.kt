package com.kafein.turkcellsaha.ui.newpost.usecase

import com.kafein.turkcellsaha.Resource
import com.kafein.turkcellsaha.data.RemoteRepository
import com.kafein.turkcellsaha.data.model.*
import com.kafein.turkcellsaha.map
import io.reactivex.Observable
import javax.inject.Inject

class UpdateContentUseCase @Inject constructor(private var repository: RemoteRepository) {

    fun updatePost(
        request: PostUpdateRequest,
        id : Int
    ): Observable<Resource<WallContent>> {
        return repository.updatePost(request,id)
            .map { res ->
                res.map {
                    it
                }
            }.startWith(Resource.Loading)
    }
}