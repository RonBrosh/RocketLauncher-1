package ronybrosh.rocketlauncher.presentation.features.rocketlist.view

import io.reactivex.android.schedulers.AndroidSchedulers
import ronybrosh.rocketlauncher.domain.entities.ResultState
import ronybrosh.rocketlauncher.domain.entities.Rocket
import ronybrosh.rocketlauncher.domain.usecases.RocketListUseCase
import ronybrosh.rocketlauncher.presentation.R
import ronybrosh.rocketlauncher.presentation.features.common.model.PresentableRocket
import ronybrosh.rocketlauncher.presentation.features.common.view.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

class RocketListViewModel @Inject constructor(private val useCase: RocketListUseCase) :
    BaseViewModel<List<PresentableRocket>>() {

    private var currentResultState: ResultState<List<Rocket>> = ResultState.Loading()
    private var isFilterOn = false

    init {
        compositeDisposable.addAll(useCase.getRocketList().observeOn(AndroidSchedulers.mainThread()).subscribe { resultState ->
            Timber.d("resultState: ${resultState.javaClass.name}")
            currentResultState = resultState
            when (resultState) {
                is ResultState.Loading -> {
                    loading.value = true
                }
                is ResultState.Success -> {
                    loading.value = false
                    result.value = resultState.data.map { rocket ->
                        PresentableRocket(
                            id = rocket.id,
                            description = rocket.description,
                            country = rocket.country,
                            name = rocket.name,
                            imageUrl = rocket.imageUrlList[0],
                            enginesCount = rocket.enginesCount,
                            isActive = rocket.isActive
                        )
                    }
                }
                is ResultState.Error -> {
                    Timber.e(resultState.throwable.localizedMessage)
                    loading.value = false
                    error.value = R.string.error_fetching_rocket_list
                }
            }
        })
    }

    fun toggleFilter() {
        currentResultState.let { resultState ->
            // If current result state is not successful there nothing to filter.
            if (resultState !is ResultState.Success) {
                Timber.e("Cant toggle filter, current result is not successful.")
                return
            }

            isFilterOn = !isFilterOn
            result.value = resultState.data
                .filter { rocket ->
                    if (isFilterOn)
                        rocket.isActive
                    else
                        true
                }
                .map { rocket ->
                    PresentableRocket(
                        id = rocket.id,
                        description = rocket.description,
                        country = rocket.country,
                        name = rocket.name,
                        imageUrl = rocket.imageUrlList[0],
                        enginesCount = rocket.enginesCount,
                        isActive = rocket.isActive
                    )
                }
        }
    }

    fun refresh() {
        currentResultState.let { resultState ->
            // If we currently loading ignore this call.
            if (resultState is ResultState.Loading) {
                Timber.e("Cant refresh rocket list, still loading.")
                return
            }

            isFilterOn = false
            result.value = emptyList()
            useCase.refreshRocketList()
        }
    }
}