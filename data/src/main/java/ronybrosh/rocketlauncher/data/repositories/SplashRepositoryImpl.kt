package ronybrosh.rocketlauncher.data.repositories

import android.content.SharedPreferences
import ronybrosh.rocketlauncher.domain.repositories.SplashRepository

class SplashRepositoryImpl(private val sharedPreferences: SharedPreferences) : SplashRepository {
    companion object {
        const val IS_SHOW_WELCOME_FLAG = "IS_SHOW_WELCOME_FLAG"
    }

    override fun getIsShowWelcome(): Boolean {
        return sharedPreferences.getBoolean(IS_SHOW_WELCOME_FLAG, true)
    }

    override fun setIsShowWelcome(isShow: Boolean) {
        sharedPreferences.edit().putBoolean(IS_SHOW_WELCOME_FLAG, isShow).apply()
    }
}