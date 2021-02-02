package com.turkcell.turkcellsaha.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.turkcell.turkcellsaha.Resource
import com.turkcell.turkcellsaha.Status
import com.turkcell.turkcellsaha.common.RxAwareViewModel
import com.turkcell.turkcellsaha.common.StatusViewState
import com.turkcell.turkcellsaha.data.model.Wall
import com.turkcell.turkcellsaha.doOnSuccess
import com.turkcell.turkcellsaha.plusAssign
import com.turkcell.turkcellsaha.ui.home.usecase.DeleteContentUseCase
import com.turkcell.turkcellsaha.ui.home.usecase.FetchWallContentUseCase
import io.reactivex.android.schedulers.AndroidSchedulers

class HomeViewModel @ViewModelInject
constructor(
    private val fetchWallContentUseCase: FetchWallContentUseCase,
    val deleteContentUseCase: DeleteContentUseCase
) : RxAwareViewModel() {

    private val wallContentLiveData = MutableLiveData<Wall>()
    val wallContentLiveData_: LiveData<Wall> = wallContentLiveData


    private val status = MutableLiveData<StatusViewState>()
    val status_: LiveData<StatusViewState> = status

    fun fetchWallContent() {
        fetchWallContentUseCase
            .fetchWallContent(0, 10)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                onWallContentResultReady(it)
            }
            .subscribe({ resource ->
                onWallStatusResultReady(resource)
            }, {
            })
            .also { disposable += it }
    }

    fun deletePost(id: Int){
        deleteContentUseCase
            .deletePost(id)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {

            }
            .subscribe({ _ ->
                fetchWallContent()
            }, {
            })
            .also { disposable += it }
    }

    private fun onWallStatusResultReady(resource: Resource<Wall>) {
        val viewState = when (resource) {
            is Resource.Success -> StatusViewState(Status.Content)
            is Resource.Error -> StatusViewState(Status.Error(resource.exception))
            Resource.Loading -> StatusViewState(Status.Loading)
        }
        status.value = viewState
    }

    private fun onWallContentResultReady(wall: Wall) {
        wallContentLiveData.value = wall
    }
}