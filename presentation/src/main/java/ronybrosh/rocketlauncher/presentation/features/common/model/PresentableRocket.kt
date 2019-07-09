package ronybrosh.rocketlauncher.presentation.features.common.model

import java.io.Serializable

data class PresentableRocket(
    val id: String,
    val description: String,
    val country: String,
    val name: String,
    val imageUrl: String,
    val enginesCount: Int,
    val isActive: Boolean
) : Serializable