package com.turkcell.turkcellsaha.ui.newpost.usecase

import com.turkcell.turkcellsaha.Resource
import com.turkcell.turkcellsaha.data.RemoteRepository
import com.turkcell.turkcellsaha.data.model.*
import com.turkcell.turkcellsaha.map
import io.reactivex.Observable
import okhttp3.MultipartBody
import java.io.File
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