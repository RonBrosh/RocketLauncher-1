package ronybrosh.rocketlauncher.domain.usecases

import io.reactivex.Observable
import ronybrosh.rocketlauncher.domain.entities.ResultState
import ronybrosh.rocketlauncher.domain.entities.Rocket
import ronybrosh.rocketlauncher.domain.repositories.RocketRepository

class RocketListUseCase(private val repository: RocketRepository) {

    fun getRocketList(): Observable<ResultState<List<Rocket>>> {
        return repository.getRocketList()
    }

    fun refreshRocketList() {
        repository.refreshRocketList()
    }
}