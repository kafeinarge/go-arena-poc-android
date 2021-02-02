package com.kafein.turkcellsaha.ui.home.usecase

import com.kafein.turkcellsaha.Resource
import com.kafein.turkcellsaha.data.RemoteRepository
import com.kafein.turkcellsaha.map
import io.reactivex.Observable
import javax.inject.Inject

class DeleteContentUseCase @Inject constructor(private var repository: RemoteRepository) {

    fun deletePost(id: Int): Observable<Resource<Any>> {
        return repository.deletePost(id)
            .map { res ->
                res.map {
                    it
                }
            }.startWith(Resource.Loading)
    }
}