package com.kafein.turkcellsaha.ui.statistics.usecase

import com.kafein.turkcellsaha.Resource
import com.kafein.turkcellsaha.data.RemoteRepository
import com.kafein.turkcellsaha.data.model.Summary
import com.kafein.turkcellsaha.map
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