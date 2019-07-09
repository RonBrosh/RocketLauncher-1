package ronybrosh.rocketlauncher.domain.usecases

import io.reactivex.Observable
import ronybrosh.rocketlauncher.domain.entities.Launch
import ronybrosh.rocketlauncher.domain.entities.ResultState
import ronybrosh.rocketlauncher.domain.repositories.LaunchRepository

class LaunchListUseCase(private val repository: LaunchRepository) {
    fun getLaunchList(rocketId: String): Observable<ResultState<List<Launch>>> {
        return repository.getLaunchList(rocketId)
    }

    fun refreshLaunchList(rocketId: String) {
        repository.refreshLaunchList(rocketId)
    }
}