package ronybrosh.rocketlauncher.domain.repositories

import io.reactivex.Observable
import ronybrosh.rocketlauncher.domain.entities.Launch
import ronybrosh.rocketlauncher.domain.entities.ResultState

interface LaunchRepository {
    fun getLaunchList(rocketId: String): Observable<ResultState<List<Launch>>>

    fun refreshLaunchList(rocketId: String)
}