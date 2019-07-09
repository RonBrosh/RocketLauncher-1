package ronybrosh.rocketlauncher.data.api.model

import com.google.gson.annotations.SerializedName

data class PatchImageApiData(
    @SerializedName("mission_patch_small") val smallImageURL: String?
)