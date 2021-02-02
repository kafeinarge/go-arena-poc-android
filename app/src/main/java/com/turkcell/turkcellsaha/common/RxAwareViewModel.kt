package com.turkcell.turkcellsaha.common

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

/**
 * reference : https://github.com/googlesamples/android-architecture-components
 */
open class RxAwareViewModel : ViewModel() {
    val disposable = CompositeDisposable()

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }
}