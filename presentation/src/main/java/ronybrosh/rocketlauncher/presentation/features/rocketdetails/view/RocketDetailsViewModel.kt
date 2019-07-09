package ronybrosh.rocketlauncher.presentation.features.rocketdetails.view

import com.github.mikephil.charting.data.BarEntry
import io.reactivex.android.schedulers.AndroidSchedulers
import ronybrosh.rocketlauncher.domain.entities.Launch
import ronybrosh.rocketlauncher.domain.entities.ResultState
import ronybrosh.rocketlauncher.domain.usecases.LaunchListUseCase
import ronybrosh.rocketlauncher.presentation.R
import ronybrosh.rocketlauncher.presentation.features.common.view.BaseViewModel
import ronybrosh.rocketlauncher.presentation.features.rocketdetails.model.RecyclerViewEntry
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class RocketDetailsViewModel @Inject constructor(private val useCase: LaunchListUseCase) :
    BaseViewModel<List<RecyclerViewEntry>>() {
    private var currentResultState: ResultState<List<Launch>> = ResultState.Loading()
    private var rocketId: String = ""
    private var description: String = ""

    fun setRocketId(rocketId: String, description: String) {
        this.rocketId = rocketId
        this.description = description

        compositeDisposable.addAll(useCase.getLaunchList(rocketId).observeOn(AndroidSchedulers.mainThread()).subscribe { resultState ->
            Timber.d("resultState: ${resultState.javaClass.name}")
            currentResultState = resultState
            when (resultState) {
                is ResultState.Loading -> {
                    loading.value = true
                }
                is ResultState.Success -> {
                    loading.value = false
                    val dataGroupedByYear = resultState.data.groupBy { it.launchYear }
                    val barEntries = createBarChartEntries(dataGroupedByYear)
                    val infoEntry: RecyclerViewEntry.Info = RecyclerViewEntry.Info(barEntries, description)
                    result.value = createRecyclerViewData(infoEntry, dataGroupedByYear)
                }
                is ResultState.Error -> {
                    loading.value = false
                    error.value = resultState.throwable.localizedMessage
                }
            }
        })
    }

    fun refresh() {
        if (rocketId.isEmpty()) {
            Timber.e("Cant refresh launch list, rocket id is empty.")
            return
        }

        currentResultState.let { resultState ->
            // If we currently loading ignore this call.
            if (resultState is ResultState.Loading) {
                Timber.e("Cant refresh launch list, still loading.")
                return
            }

            val infoEntry: RecyclerViewEntry.Info = RecyclerViewEntry.Info(emptyList(), description)
            result.value = createRecyclerViewData(infoEntry, emptyMap())
            useCase.refreshLaunchList(rocketId)
        }
    }

    private fun createBarChartEntries(rawData: Map<Int, List<Launch>>): List<BarEntry> {
        val result: MutableList<BarEntry> = mutableListOf()
        rawData.forEach { entry ->
            result.add(BarEntry(entry.key.toFloat(), entry.value.size.toFloat()))
        }
        return result.toList()
    }

    private fun createRecyclerViewData(
        infoEntry: RecyclerViewEntry.Info,
        rawData: Map<Int, List<Launch>>
    ): List<RecyclerViewEntry> {
        val dateFormat = SimpleDateFormat.getDateInstance()
        val result: MutableList<RecyclerViewEntry> = mutableListOf()
        rawData.forEach { entry ->
            result.add(RecyclerViewEntry.Header(entry.key.toString()))
            result.addAll(entry.value.map { launch ->
                RecyclerViewEntry.Item(
                    missionName = launch.missionName,
                    launchDate = dateFormat.format(Date(launch.launchDateUnix * 1000)),
                    patchImageUrl = launch.patchImageUrl,
                    isSuccessfulTextResourceId = when (launch.isSuccessful) {
                        true -> {
                            R.string.rocket_details_screen_is_mission_successful_true
                        }
                        else -> {
                            R.string.rocket_details_screen_is_mission_successful_false
                        }
                    }
                )
            })
        }
        result.add(0, infoEntry)
        return result.toList()
    }
}