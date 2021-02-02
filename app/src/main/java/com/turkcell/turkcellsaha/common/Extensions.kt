package com.turkcell.turkcellsaha

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

fun <T> Observable<Resource<T>>.doOnSuccess(
    onSuccess: (T) -> (Unit)
): Observable<Resource<T>> {
    return this.doOnNext {
        if (it is Resource.Success) {
            onSuccess(it.data)
        }
    }

}

fun <T> Observable<T>.remote(): Observable<Resource<T>> =
    map<Resource<T>> { Resource.Success(it) }
        .onErrorReturn { throwable ->
            Resource.Error(throwable)
        }
        .subscribeOn(Schedulers.io())
        .startWith(Resource.Loading)

fun <T> LiveData<T>.observeNonNull(owner: LifecycleOwner, observer: (t: T) -> Unit) {
    this.observe(owner, Observer {
        it?.let(observer)
    })
}

operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    add(disposable)
}

