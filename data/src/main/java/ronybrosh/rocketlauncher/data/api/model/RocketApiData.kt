package ronybrosh.rocketlauncher.data.api.model

import com.google.gson.annotations.SerializedName

data class RocketApiData(
    @SerializedName("rocket_id") val rocketId: String,
    @SerializedName("description") val description: String,
    @SerializedName("rocket_name") val name: String,
    @SerializedName("country") val country: String,
    @SerializedName("active") val isActive: Boolean,
    @SerializedName("flickr_images") val imageUrlList: List<String>,
    @SerializedName("engines") var engine: EngineApiData
)

