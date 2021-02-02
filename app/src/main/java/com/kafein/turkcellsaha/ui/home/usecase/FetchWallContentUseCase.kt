package com.kafein.turkcellsaha.ui.home.usecase

import com.kafein.turkcellsaha.Resource
import com.kafein.turkcellsaha.data.RemoteRepository
import com.kafein.turkcellsaha.data.model.Wall
import com.kafein.turkcellsaha.map
import io.reactivex.Observable
import javax.inject.Inject

class FetchWallContentUseCase @Inject constructor(private var repository: RemoteRepository) {

    fun fetchWallContent(pageNo: Int?,pageSize : Int?): Observable<Resource<Wall>> {
        return repository.fetchWallContent(pageNo,pageSize)
            .map { res ->
                res.map {
                    it
                }
            }.startWith(Resource.Loading)
    }
}