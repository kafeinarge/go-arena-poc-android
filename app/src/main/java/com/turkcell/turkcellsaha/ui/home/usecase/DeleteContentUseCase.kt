package com.turkcell.turkcellsaha.ui.home.usecase

import com.turkcell.turkcellsaha.Resource
import com.turkcell.turkcellsaha.data.RemoteRepository
import com.turkcell.turkcellsaha.data.model.PostRequest
import com.turkcell.turkcellsaha.data.model.Summary
import com.turkcell.turkcellsaha.data.model.Wall
import com.turkcell.turkcellsaha.data.model.WallContent
import com.turkcell.turkcellsaha.map
import io.reactivex.Observable
import okhttp3.MultipartBody
import java.io.File
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