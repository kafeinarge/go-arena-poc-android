package com.turkcell.turkcellsaha.ui.home.usecase

import com.turkcell.turkcellsaha.Resource
import com.turkcell.turkcellsaha.data.RemoteRepository
import com.turkcell.turkcellsaha.data.model.Summary
import com.turkcell.turkcellsaha.data.model.Wall
import com.turkcell.turkcellsaha.map
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