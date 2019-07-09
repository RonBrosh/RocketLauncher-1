package ronybrosh.rocketlauncher.presentation.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ronybrosh.rocketlauncher.presentation.features.rocketlist.view.RocketListFragment
import ronybrosh.rocketlauncher.presentation.features.rocketdetails.view.RocketDetailsActivity
import ronybrosh.rocketlauncher.presentation.features.rocketlist.view.RocketListActivity
import ronybrosh.rocketlauncher.presentation.features.splash.view.SplashActivity

@Module
abstract class ContextModule {
    @ContributesAndroidInjector
    abstract fun contributeSplashActivity(): SplashActivity

    @ContributesAndroidInjector
    abstract fun contributeRocketListActivity(): RocketListActivity

    @ContributesAndroidInjector
    abstract fun contributeRocketDetailsActivity(): RocketDetailsActivity
}