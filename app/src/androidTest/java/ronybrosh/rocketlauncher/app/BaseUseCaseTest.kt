package ronybrosh.rocketlauncher.app

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ronybrosh.rocketlauncher.data.api.RocketLauncherApi
import ronybrosh.rocketlauncher.data.db.RocketLauncherDatabase
import ronybrosh.rocketlauncher.data.repositories.RocketRepositoryImpl
import ronybrosh.rocketlauncher.domain.entities.ResultState
import ronybrosh.rocketlauncher.domain.usecases.RocketListUseCase


abstract class BaseUseCaseTest {
    private val TAG = "BaseUseCaseTest"

    private fun provideApi(): RocketLauncherApi {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("https://api.spacexdata.com/")
            .client(OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build())
            .build()
        return retrofit.create(RocketLauncherApi::class.java)
    }

    fun <T> logResultState(resultState: ResultState<T>) {
        when (resultState) {
            is ResultState.Loading -> {
                Log.d(TAG, "Loading")
            }
            is ResultState.Error -> {
                when (resultState.throwable) {
                    is HttpException -> {
                        Log.d(TAG, "Error: http code =  ${(resultState.throwable as HttpException).code()}")
                    }
                    else -> {
                        Log.d(TAG, "Error: ${resultState.throwable.message}")
                    }
                }
            }
            is ResultState.Success -> {
                Log.d(TAG, "Success: ${resultState.data}")
            }
        }
    }

    fun logError(throwable: Throwable) {
        Log.d(TAG, "Throwable: ${throwable.message}")
    }

    fun provideRocketListUseCase(): RocketListUseCase {
        val local = RocketLauncherDatabase.getInstance(InstrumentationRegistry.getInstrumentation().targetContext)
            .getRocketDao()
        val remote = provideApi()
        return RocketListUseCase(RocketRepositoryImpl(local, remote))
    }
}