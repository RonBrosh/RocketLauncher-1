package ronybrosh.rocketlauncher.data.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ronybrosh.rocketlauncher.data.api.RocketLauncherApi
import ronybrosh.rocketlauncher.data.db.RocketLauncherDatabase
import ronybrosh.rocketlauncher.data.db.dao.LaunchDao
import ronybrosh.rocketlauncher.data.db.dao.RocketDao
import ronybrosh.rocketlauncher.data.repositories.LaunchRepositoryImpl
import ronybrosh.rocketlauncher.data.repositories.RocketRepositoryImpl
import ronybrosh.rocketlauncher.data.repositories.SplashRepositoryImpl
import ronybrosh.rocketlauncher.domain.repositories.LaunchRepository
import ronybrosh.rocketlauncher.domain.repositories.RocketRepository
import ronybrosh.rocketlauncher.domain.repositories.SplashRepository
import javax.inject.Singleton

@Module
class DataModule {
    @Singleton
    @Provides
    fun provideRocketLauncherApi(): RocketLauncherApi {
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

    @Singleton
    @Provides
    fun provideSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences(
            "SHARED_PREFERENCES_FILE_NAME",
            Context.MODE_PRIVATE
        )
    }

    @Singleton
    @Provides
    fun provideRocketLauncherDatabase(application: Application): RocketLauncherDatabase {
        return RocketLauncherDatabase.getInstance(application)
    }

    @Singleton
    @Provides
    fun provideRocketDao(database: RocketLauncherDatabase): RocketDao {
        return database.getRocketDao()
    }

    @Singleton
    @Provides
    fun provideLaunchDao(database: RocketLauncherDatabase): LaunchDao {
        return database.getLaunchDao()
    }

    @Provides
    fun provideSplashRepository(sharedPreferences: SharedPreferences): SplashRepository {
        return SplashRepositoryImpl(sharedPreferences)
    }

    @Provides
    fun provideRocketRepository(local: RocketDao, remote: RocketLauncherApi): RocketRepository {
        return RocketRepositoryImpl(local, remote)
    }

    @Provides
    fun provideLaunchRepository(local: LaunchDao, remote: RocketLauncherApi): LaunchRepository {
        return LaunchRepositoryImpl(local, remote)
    }
}