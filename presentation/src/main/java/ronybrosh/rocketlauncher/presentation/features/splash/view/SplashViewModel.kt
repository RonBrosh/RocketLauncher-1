package ronybrosh.rocketlauncher.presentation.features.splash.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import ronybrosh.rocketlauncher.domain.usecases.SplashUseCase
import ronybrosh.rocketlauncher.presentation.features.splash.model.SplashActivityAction
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SplashViewModel @Inject constructor(private val useCase: SplashUseCase) : ViewModel() {
    private val splashActivityAction: MutableLiveData<SplashActivityAction> = MutableLiveData()
    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        Timber.d("OnCleared")
    }

    fun getSplashActivityAction(): LiveData<SplashActivityAction> {
        return splashActivityAction
    }

    fun startAnimationFlow() {
        val actions: List<SplashActivityAction> = if (useCase.getIsShowWelcome()) {
            listOf(
                SplashActivityAction.ANIMATE_ENTER,
                SplashActivityAction.ANIMATE_WELCOME
            )
        } else {
            listOf(
                SplashActivityAction.ANIMATE_ENTER,
                SplashActivityAction.ANIMATE_EXIT,
                SplashActivityAction.SHOW_NEXT_SCREEN
            )
        }
        compositeDisposable.addAll(
            Observable.fromIterable(actions).concatMap { action ->
                Observable.interval(action.duration, TimeUnit.MILLISECONDS).map { action }.take(1)
            }.observeOn(AndroidSchedulers.mainThread()).subscribe { action ->
                splashActivityAction.value = action
            })
    }

    fun onIsShowWelcomeStateChanges(isShow: Boolean) {
        useCase.setIsShowWelcome(isShow)
    }

    fun onContinueButtonClick() {
        val actions: List<SplashActivityAction> = listOf(
            SplashActivityAction.ANIMATE_WELCOME_EXIT,
            SplashActivityAction.SHOW_NEXT_SCREEN
        )
        compositeDisposable.addAll(
            Observable.fromIterable(actions).concatMap { action ->
                Observable.interval(action.duration, TimeUnit.MILLISECONDS).map { action }.take(1)
            }.observeOn(AndroidSchedulers.mainThread()).subscribe { action ->
                splashActivityAction.value = action
            })
    }
}