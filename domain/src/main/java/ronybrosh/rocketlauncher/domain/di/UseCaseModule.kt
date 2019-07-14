package ronybrosh.rocketlauncher.domain.di

import dagger.Module
import dagger.Provides
import ronybrosh.rocketlauncher.domain.repositories.LaunchRepository
import ronybrosh.rocketlauncher.domain.repositories.RocketRepository
import ronybrosh.rocketlauncher.domain.repositories.SplashRepository
import ronybrosh.rocketlauncher.domain.usecases.LaunchListUseCase
import ronybrosh.rocketlauncher.domain.usecases.RocketListUseCase
import ronybrosh.rocketlauncher.domain.usecases.SplashUseCase

@Module
class UseCaseModule {

    @Provides
    fun provideSplashUseCase(repository: SplashRepository): SplashUseCase {
        return SplashUseCase(repository)
    }

    @Provides
    fun provideRocketListUseCase(repository: RocketRepository): RocketListUseCase {
        return RocketListUseCase(repository)
    }

    @Provides
    fun provideLaunchListUseCase(repository: LaunchRepository): LaunchListUseCase {
        return LaunchListUseCase(repository)
    }
}