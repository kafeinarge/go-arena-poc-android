package com.turkcell.turkcellsaha.data

import com.turkcell.turkcellsaha.Resource
import com.turkcell.turkcellsaha.data.api.RestInterface
import com.turkcell.turkcellsaha.data.model.*
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject

class RemoteRepository @Inject constructor(private val api: RestInterface) {

    fun login(request: LoginRequest): Observable<Resource<TokenResponse>> {
        return api.login(request)
            .map<Resource<TokenResponse>> {
                Resource.Success(it)
            }.onErrorReturn { throwable ->
                Resource.Error(throwable)

            }.subscribeOn(Schedulers.io())
    }

    fun fetchSummaries(month: Int?): Observable<Resource<Summary>> {
        return api.fetchSummaries(month)
            .map<Resource<Summary>> {
                Resource.Success(it)
            }.onErrorReturn { throwable ->
                Resource.Error(throwable)

            }.subscribeOn(Schedulers.io())
    }

    fun fetchWallContent(pageNo: Int?, pageSize: Int?): Observable<Resource<Wall>> {
        return api.fetchWallContent(pageNo, pageSize)
            .map<Resource<Wall>> {
                Resource.Success(it)
            }.onErrorReturn { throwable ->
                Resource.Error(throwable)

            }.subscribeOn(Schedulers.io())
    }

    fun submitPost(photo: MultipartBody.Part, text: String): Observable<Resource<WallContent>> {
        return api.submitPost(photo, text)
            .map<Resource<WallContent>> {
                Resource.Success(it)
            }.onErrorReturn { throwable ->
                Resource.Error(throwable)

            }.subscribeOn(Schedulers.io())
    }

    fun updatePost(
     request: PostUpdateRequest,
     id :Int
    ): Observable<Resource<WallContent>> {
        return api.updatePost(request,id)
            .map<Resource<WallContent>> {
                Resource.Success(it)
            }.onErrorReturn { throwable ->
                Resource.Error(throwable)

            }.subscribeOn(Schedulers.io())
    }

    fun deletePost(id: Int): Observable<Resource<Any>> {
        return api.deletePost(id)
            .map<Resource<Any>> {
                Resource.Success(it)
            }.onErrorReturn { throwable ->
                Resource.Error(throwable)

            }.subscribeOn(Schedulers.io())
    }
}