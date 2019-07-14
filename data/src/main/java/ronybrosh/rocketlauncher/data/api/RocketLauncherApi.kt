package ronybrosh.rocketlauncher.data.api

import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query
import ronybrosh.rocketlauncher.data.api.model.LaunchApiData
import ronybrosh.rocketlauncher.data.api.model.RocketApiData

interface RocketLauncherApi {
    @GET("v3/rockets")
    fun getRocketList(): Flowable<List<RocketApiData>>

    @GET("v3/launches")
    fun getLaunchList(@Query("rocket_id") rocketId: String): Flowable<List<LaunchApiData>>
}