package ronybrosh.rocketlauncher.presentation.features.splash.model

enum class SplashActivityAction(val duration: Long) {
    ANIMATE_ENTER(700),
    ANIMATE_WELCOME(5000),
    ANIMATE_EXIT(5000),
    ANIMATE_WELCOME_EXIT(0),
    SHOW_NEXT_SCREEN(750)
}