package ronybrosh.rocketlauncher.domain.entities

data class Rocket(
    val id: String,
    val description: String,
    val country: String,
    val name: String,
    val isActive: Boolean,
    val imageUrlList: List<String>,
    val enginesCount: Int
)