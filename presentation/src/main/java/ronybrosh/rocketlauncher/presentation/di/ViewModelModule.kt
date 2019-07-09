package ronybrosh.rocketlauncher.presentation.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ronybrosh.rocketlauncher.presentation.features.rocketdetails.view.RocketDetailsViewModel
import ronybrosh.rocketlauncher.presentation.features.rocketlist.view.RocketListViewModel
import ronybrosh.rocketlauncher.presentation.features.splash.view.SplashViewModel

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: DaggerViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(viewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RocketListViewModel::class)
    abstract fun bindRocketListViewModel(viewModel: RocketListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RocketDetailsViewModel::class)
    abstract fun bindRocketDetailsViewModel(viewModel: RocketDetailsViewModel): ViewModel
}