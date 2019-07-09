package ronybrosh.rocketlauncher.domain.entities

data class Launch(
    val missionName: String,
    val launchYear: Int,
    val launchDateUnix: Long,
    val patchImageUrl: String?,
    val isSuccessful: Boolean
)