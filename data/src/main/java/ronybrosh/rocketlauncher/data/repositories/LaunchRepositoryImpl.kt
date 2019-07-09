package ronybrosh.rocketlauncher.data.repositories

import android.annotation.SuppressLint
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import ronybrosh.rocketlauncher.data.api.RocketLauncherApi
import ronybrosh.rocketlauncher.data.api.model.LaunchApiData
import ronybrosh.rocketlauncher.data.db.dao.LaunchDao
import ronybrosh.rocketlauncher.data.db.model.LaunchRow
import ronybrosh.rocketlauncher.domain.entities.Launch
import ronybrosh.rocketlauncher.domain.entities.ResultState
import ronybrosh.rocketlauncher.domain.repositories.LaunchRepository

class LaunchRepositoryImpl(
    private val local: LaunchDao,
    private val remote: RocketLauncherApi
) : LaunchRepository {

    private val subject: PublishSubject<ResultState<List<Launch>>> = PublishSubject.create()
    private val compositeDisposable = CompositeDisposable()

    override fun getLaunchList(rocketId: String): Observable<ResultState<List<Launch>>> {
        return subject.doOnSubscribe {
            // When subscribing to this subject we need to:
            // 1. subscribe to local data events
            // 2. refresh the local data by calling the remote only for the first time if local is empty

            // Create local flowable.
            val localFlowable = local.getLaunchList(rocketId).flatMap<List<LaunchRow>> { list ->
                if (list.isEmpty()) {
                    refreshLaunchList(rocketId)
                    // Skip 1 so we won't get empty result before refresh rocket list finished.
                    Flowable.just(list).skip(1)
                } else {
                    Flowable.just(list)
                }
            }

            // Create local disposable.
            val localDisposable = localFlowable.subscribeOn(Schedulers.io()).subscribe({ list ->
                subject.onNext(ResultState.Success(convertLaunchRowListToLaunchList(list)))
            }, { throwable ->
                subject.onNext(ResultState.Error(throwable))
            })

            compositeDisposable.add(localDisposable)
        }.doOnDispose {
            compositeDisposable.clear()
        }
    }

    @SuppressLint("CheckResult")
    override fun refreshLaunchList(rocketId: String) {
        subject.onNext(ResultState.Loading())
        remote.getLaunchList(rocketId).take(1).subscribeOn(Schedulers.io()).subscribe({ list ->
            if (list.isEmpty())
                subject.onNext(ResultState.Success(emptyList()))
            else
                local.insertLaunchList(convertLaunchApiDataListToLaunchRowList(rocketId, list))
        }, { throwable ->
            subject.onNext(ResultState.Error(throwable))
        })
    }

    private fun convertLaunchApiDataListToLaunchRowList(rocketId: String, list: List<LaunchApiData>): List<LaunchRow> {
        return list
            .map { launchApiData ->
                LaunchRow(
                    flightNumber = launchApiData.flightNumber,
                    rocketId = rocketId,
                    missionName = launchApiData.missionName,
                    launchYear = launchApiData.launchYear,
                    patchImageUrl = launchApiData.patchImage?.smallImageURL ?: "",
                    launchDateUnix = launchApiData.launchDateUnix,
                    isSuccessful = launchApiData.isSuccessful
                )
            }.toList()
    }

    private fun convertLaunchRowListToLaunchList(list: List<LaunchRow>): List<Launch> {
        return list
            .map { launchRow ->
                Launch(
                    missionName = launchRow.missionName,
                    launchYear = launchRow.launchYear.toIntOrNull() ?: 0,
                    launchDateUnix = launchRow.launchDateUnix,
                    patchImageUrl = launchRow.patchImageUrl,
                    isSuccessful = launchRow.isSuccessful
                )
            }
    }
}