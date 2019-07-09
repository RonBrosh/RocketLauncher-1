package ronybrosh.rocketlauncher.app

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import ronybrosh.rocketlauncher.data.di.DataModule
import ronybrosh.rocketlauncher.domain.di.UseCaseModule
import ronybrosh.rocketlauncher.presentation.di.ContextModule
import ronybrosh.rocketlauncher.presentation.di.ViewModelModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ContextModule::class,
        DataModule::class,
        UseCaseModule::class,
        ViewModelModule::class,
        AndroidInjectionModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}