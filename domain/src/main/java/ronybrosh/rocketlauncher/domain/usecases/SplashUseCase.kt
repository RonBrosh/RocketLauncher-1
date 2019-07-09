package ronybrosh.rocketlauncher.domain.usecases

import ronybrosh.rocketlauncher.domain.repositories.SplashRepository

class SplashUseCase(private val repository: SplashRepository) {
    fun getIsShowWelcome(): Boolean {
        return repository.getIsShowWelcome()
    }

    fun setIsShowWelcome(isShow: Boolean) {
        repository.setIsShowWelcome(isShow)
    }
}