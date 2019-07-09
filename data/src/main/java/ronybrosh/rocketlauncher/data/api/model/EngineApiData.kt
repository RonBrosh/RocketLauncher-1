package ronybrosh.rocketlauncher.data.api.model

import com.google.gson.annotations.SerializedName

data class EngineApiData(
    @SerializedName("number") val enginesCount: Int
)