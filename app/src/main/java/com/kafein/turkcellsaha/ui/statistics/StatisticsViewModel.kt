package com.kafein.turkcellsaha.ui.statistics

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kafein.turkcellsaha.Resource
import com.kafein.turkcellsaha.Status
import com.kafein.turkcellsaha.common.RxAwareViewModel
import com.kafein.turkcellsaha.common.StatusViewState
import com.kafein.turkcellsaha.domain.KeyValue
import com.kafein.turkcellsaha.data.model.Summary
import com.kafein.turkcellsaha.doOnSuccess
import com.kafein.turkcellsaha.plusAssign
import com.kafein.turkcellsaha.ui.statistics.usecase.FetchSummaryUseCase
import io.reactivex.android.schedulers.AndroidSchedulers

class StatisticsViewModel @ViewModelInject constructor(private val fetchSummaryUseCase: FetchSummaryUseCase): RxAwareViewModel() {

    private val summaryLiveData = MutableLiveData<Summary>()
    val summaryLiveData_: LiveData<Summary> = summaryLiveData

    private val status = MutableLiveData<StatusViewState>()
    val status_: LiveData<StatusViewState> = status

    fun fetchSummaries(month : Int?){
        fetchSummaryUseCase
            .fetchSummaries(month)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                onSummaryResultReady(it)
            }
            .subscribe({ resource ->
                onSummaryStatusResultReady(resource)
            }, {
            })
            .also { disposable += it }
    }

    private fun onSummaryStatusResultReady(resource: Resource<Summary>) {
        val viewState = when (resource) {
            is Resource.Success -> StatusViewState(Status.Content)
            is Resource.Error -> StatusViewState(Status.Error(resource.exception))
            Resource.Loading -> StatusViewState(Status.Loading)
        }
        status.value = viewState
    }

    private fun onSummaryResultReady(summary: Summary) {
        summaryLiveData.value = summary
    }

    fun fetchMonthData() : List<KeyValue>{
        val keyValueList = mutableListOf<KeyValue>()
        keyValueList.add(KeyValue(1,"Ocak"))
        keyValueList.add(KeyValue(2,"Şubat"))
        keyValueList.add(KeyValue(3,"Mart"))
        keyValueList.add(KeyValue(4,"Nisan"))
        keyValueList.add(KeyValue(5,"Mayıs"))
        keyValueList.add(KeyValue(6,"Haziran"))
        keyValueList.add(KeyValue(7,"Temmuz"))
        keyValueList.add(KeyValue(8,"Ağustos"))
        keyValueList.add(KeyValue(9,"Eylül"))
        keyValueList.add(KeyValue(10,"Ekim"))
        keyValueList.add(KeyValue(11,"Kasım"))
        keyValueList.add(KeyValue(12,"Aralık"))

        return keyValueList
    }
}