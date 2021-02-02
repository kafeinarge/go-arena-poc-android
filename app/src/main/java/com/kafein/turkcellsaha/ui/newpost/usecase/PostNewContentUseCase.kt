package com.kafein.turkcellsaha.ui.newpost.usecase

import com.kafein.turkcellsaha.Resource
import com.kafein.turkcellsaha.data.RemoteRepository
import com.kafein.turkcellsaha.data.model.WallContent
import com.kafein.turkcellsaha.map
import io.reactivex.Observable
import okhttp3.MultipartBody
import javax.inject.Inject

class PostNewContentUseCase @Inject constructor(private var repository: RemoteRepository) {

    fun submitPost(photo: MultipartBody.Part, text: String): Observable<Resource<WallContent>> {
        return repository.submitPost(photo, text)
            .map { res ->
                res.map {
                    it
                }
            }.startWith(Resource.Loading)
    }
}