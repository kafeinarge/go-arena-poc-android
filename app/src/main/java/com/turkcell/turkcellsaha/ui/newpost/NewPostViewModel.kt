package com.turkcell.turkcellsaha.ui.newpost

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.turkcell.turkcellsaha.Resource
import com.turkcell.turkcellsaha.Status
import com.turkcell.turkcellsaha.common.RxAwareViewModel
import com.turkcell.turkcellsaha.common.StatusViewState
import com.turkcell.turkcellsaha.data.model.PostUpdateRequest
import com.turkcell.turkcellsaha.data.model.WallContent
import com.turkcell.turkcellsaha.doOnSuccess
import com.turkcell.turkcellsaha.plusAssign
import com.turkcell.turkcellsaha.ui.newpost.usecase.PostNewContentUseCase
import com.turkcell.turkcellsaha.ui.newpost.usecase.UpdateContentUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import okhttp3.MultipartBody

class NewPostViewModel @ViewModelInject
constructor(
    private val postNewContentUseCase: PostNewContentUseCase,
    private val updateContentUseCase: UpdateContentUseCase
) :
    RxAwareViewModel() {


    private val newPostLiveData = MutableLiveData<WallContent>()
    val newPostLiveData_: LiveData<WallContent> = newPostLiveData

    private val status = MutableLiveData<StatusViewState>()
    val status_: LiveData<StatusViewState> = status

    fun submitPost(body: MultipartBody, text: String) {
        postNewContentUseCase
            .submitPost(body.part(0), text)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                onPostResultReady(it)
            }
            .subscribe({ resource ->
                onPostStatusResultReady(resource)
            }, {
            })
            .also { disposable += it }
    }

    fun updatePost(id: Int, text: String) {
        updateContentUseCase
            .updatePost(PostUpdateRequest(text), id)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                onPostResultReady(it)
            }
            .subscribe({ resource ->
                onPostStatusResultReady(resource)
            }, {
            })
            .also { disposable += it }
    }

    private fun onPostStatusResultReady(resource: Resource<WallContent>) {
        val viewState = when (resource) {
            is Resource.Success -> StatusViewState(Status.Content)
            is Resource.Error -> StatusViewState(Status.Error(resource.exception))
            Resource.Loading -> StatusViewState(Status.Loading)
        }
        status.value = viewState
    }

    private fun onPostResultReady(content: WallContent) {
        newPostLiveData.value = content
    }
}