package ronybrosh.rocketlauncher.presentation.features.rocketdetails.model

import com.github.mikephil.charting.data.BarEntry

sealed class RecyclerViewEntry {
    data class Info(
        val barEntries: List<BarEntry>,
        val description: String
    ) : RecyclerViewEntry()

    data class Item(
        val missionName: String,
        val launchDate: String,
        val isSuccessfulTextResourceId: Int,
        val patchImageUrl: String?
    ) : RecyclerViewEntry()

    data class Header(val title: String) : RecyclerViewEntry()
}