package ronybrosh.rocketlauncher.data.api.model

import com.google.gson.annotations.SerializedName

data class LaunchApiData(
    @SerializedName("flight_number") val flightNumber: Int,
    @SerializedName("mission_name") val missionName: String,
    @SerializedName("launch_year") val launchYear: String,
    @SerializedName("launch_success") val isSuccessful: Boolean,
    @SerializedName("launch_date_unix") val launchDateUnix: Long,
    @SerializedName("links") val patchImage: PatchImageApiData?
)