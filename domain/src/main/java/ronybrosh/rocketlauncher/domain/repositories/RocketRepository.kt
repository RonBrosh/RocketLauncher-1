package ronybrosh.rocketlauncher.domain.repositories

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import ronybrosh.rocketlauncher.domain.entities.ResultState
import ronybrosh.rocketlauncher.domain.entities.Rocket

interface RocketRepository {
    fun getRocketList(): Observable<ResultState<List<Rocket>>>

    fun refreshRocketList()
}