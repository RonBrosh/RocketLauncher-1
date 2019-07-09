package ronybrosh.rocketlauncher.presentation.features.common.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel<T> : ViewModel() {
    protected val compositeDisposable = CompositeDisposable()

    protected val loading: MutableLiveData<Boolean> = MutableLiveData()
    protected val error: MutableLiveData<String> = MutableLiveData()
    protected val result: MutableLiveData<T> = MutableLiveData()

    fun getLoading(): LiveData<Boolean> {
        return loading
    }

    fun getError(): LiveData<String> {
        return error
    }

    fun getResult(): LiveData<T> {
        return result
    }

    fun onClear() {
        compositeDisposable.clear()
    }
}