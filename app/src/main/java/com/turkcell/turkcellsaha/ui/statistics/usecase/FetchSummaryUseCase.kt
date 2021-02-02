package com.turkcell.turkcellsaha.ui.statistics.usecase

import com.turkcell.turkcellsaha.Resource
import com.turkcell.turkcellsaha.data.RemoteRepository
import com.turkcell.turkcellsaha.data.model.Summary
import com.turkcell.turkcellsaha.map
import io.reactivex.Observable
import javax.inject.Inject

class FetchSummaryUseCase @Inject constructor(private var repository: RemoteRepository) {

    fun fetchSummaries(month : Int?): Observable<Resource<Summary>> {
        return repository.fetchSummaries(month)
            .map { res ->
                res.map {
                    it
                }
            }.startWith(Resource.Loading)
    }
}