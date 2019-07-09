package ronybrosh.rocketlauncher.domain.repositories

interface SplashRepository {
    fun getIsShowWelcome(): Boolean

    fun setIsShowWelcome(isShow: Boolean)
}