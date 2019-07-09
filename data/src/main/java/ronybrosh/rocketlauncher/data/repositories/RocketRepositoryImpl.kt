package ronybrosh.rocketlauncher.data.repositories

import android.annotation.SuppressLint
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import ronybrosh.rocketlauncher.data.api.RocketLauncherApi
import ronybrosh.rocketlauncher.data.api.model.RocketApiData
import ronybrosh.rocketlauncher.data.db.dao.RocketDao
import ronybrosh.rocketlauncher.data.db.model.RocketRow
import ronybrosh.rocketlauncher.domain.entities.ResultState
import ronybrosh.rocketlauncher.domain.entities.Rocket
import ronybrosh.rocketlauncher.domain.repositories.RocketRepository

class RocketRepositoryImpl(
    private val local: RocketDao,
    private val remote: RocketLauncherApi
) : RocketRepository {
    private val subject: PublishSubject<ResultState<List<Rocket>>> = PublishSubject.create()
    private val compositeDisposable = CompositeDisposable()

    override fun getRocketList(): Observable<ResultState<List<Rocket>>> {
        return subject.doOnSubscribe {
            // When subscribing to this subject we need to:
            // 1. subscribe to local data events
            // 2. refresh the local data by calling the remote only for the first time if local is empty

            // Create local flowable.
            val localFlowable = local.getRocketList().flatMap<List<RocketRow>> { list ->
                if (list.isEmpty()) {
                    refreshRocketList()
                    // Skip 1 so we won't get empty result before refresh rocket list finished.
                    Flowable.just(list).skip(1)
                } else {
                    Flowable.just(list)
                }
            }

            // Create local disposable.
            val localDisposable = localFlowable.subscribeOn(Schedulers.io()).subscribe({ list ->
                subject.onNext(ResultState.Success(convertRocketRowListToRocketList(list)))
            }, { throwable ->
                subject.onNext(ResultState.Error(throwable))
            })

            compositeDisposable.add(localDisposable)
        }.doOnDispose {
            compositeDisposable.clear()
        }
    }

    @SuppressLint("CheckResult")
    override fun refreshRocketList() {
        subject.onNext(ResultState.Loading())
        remote.getRocketList().take(1).subscribeOn(Schedulers.io()).subscribe({ list ->
            local.insertRocketList(convertRocketApiDataListToRocketRowList(list))
        }, { throwable ->
            subject.onNext(ResultState.Error(throwable))
        })
    }

    private fun convertRocketApiDataListToRocketRowList(list: List<RocketApiData>): List<RocketRow> {
        return list
            .map { rocketApiData ->
                RocketRow(
                    id = rocketApiData.rocketId,
                    description = rocketApiData.description,
                    name = rocketApiData.name,
                    country = rocketApiData.country,
                    isActive = rocketApiData.isActive,
                    imageUrlList = rocketApiData.imageUrlList,
                    enginesCount = rocketApiData.engine.enginesCount
                )
            }.toList()
    }

    private fun convertRocketRowListToRocketList(list: List<RocketRow>): List<Rocket> {
        return list
            .map { rocketRow ->
                Rocket(
                    id = rocketRow.id,
                    description = rocketRow.description,
                    name = rocketRow.name,
                    country = rocketRow.country,
                    isActive = rocketRow.isActive,
                    imageUrlList = rocketRow.imageUrlList,
                    enginesCount = rocketRow.enginesCount
                )
            }
    }
}