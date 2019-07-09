package ronybrosh.rocketlauncher.presentation.features.common.view

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import dagger.android.support.DaggerFragment
import ronybrosh.rocketlauncher.presentation.di.DaggerViewModelFactory
import javax.inject.Inject

abstract class ViewModelFragment<T : ViewModel>(private val viewModelType: Class<T>) : DaggerFragment() {

    @Inject
    lateinit var viewModeFactory: DaggerViewModelFactory

    protected lateinit var viewModel: T

    override fun onDestroy() {
        super.onDestroy()
        viewModel.let {
            if (it is BaseViewModel<*>)
                it.onClear()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModeFactory)[viewModelType]
    }
}