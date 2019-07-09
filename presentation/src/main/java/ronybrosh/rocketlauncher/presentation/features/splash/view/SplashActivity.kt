package ronybrosh.rocketlauncher.presentation.features.splash.view

import android.content.Intent
import android.graphics.drawable.AnimatedVectorDrawable
import android.net.Uri
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.Transition
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnticipateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.res.ResourcesCompat
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.container_welcome.*
import ronybrosh.rocketlauncher.presentation.R
import ronybrosh.rocketlauncher.presentation.features.common.listeners.AnimationListenerImpl
import ronybrosh.rocketlauncher.presentation.features.common.transitions.FadeTransition
import ronybrosh.rocketlauncher.presentation.features.common.transitions.ResizeTextTransition
import ronybrosh.rocketlauncher.presentation.features.common.transitions.TextColorTransition
import ronybrosh.rocketlauncher.presentation.features.common.transitions.TranslateTransition
import ronybrosh.rocketlauncher.presentation.features.common.view.ViewModelActivity
import ronybrosh.rocketlauncher.presentation.features.rocketlist.view.RocketListActivity
import ronybrosh.rocketlauncher.presentation.features.splash.model.SplashActivityAction
import ronybrosh.rocketlauncher.presentation.utils.observe
import ronybrosh.rocketlauncher.presentation.utils.setFullScreen
import ronybrosh.rocketlauncher.presentation.utils.startActivity

class SplashActivity : ViewModelActivity<SplashViewModel>(SplashViewModel::class.java) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setupViews()
        setupViewModel()
    }

    override fun onResume() {
        super.onResume()
        setFullScreen()
    }

    private fun setupViews() {
        isShowWelcomeSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onIsShowWelcomeStateChanges(isChecked)
        }
        continueButton.setOnClickListener {
            viewModel.onContinueButtonClick()
        }
        credits.setOnClickListener {
            startActivity(Intent.ACTION_VIEW, Uri.parse(getString(R.string.github_account_url)))
        }
    }

    private fun setupViewModel() {
        observe(viewModel.getSplashActivityAction()) { splashActivityAction ->
            when (splashActivityAction) {
                SplashActivityAction.ANIMATE_ENTER -> {
                    animateEnter()
                }
                SplashActivityAction.ANIMATE_WELCOME -> {
                    animateWelcome()
                }
                SplashActivityAction.ANIMATE_EXIT -> {
                    animateExit()
                }
                SplashActivityAction.ANIMATE_WELCOME_EXIT -> {
                    animateWelcomeExit()
                }
                SplashActivityAction.SHOW_NEXT_SCREEN -> {
                    startActivity<RocketListActivity>()
                    overridePendingTransition(R.anim.slide_up, R.anim.stay)
                    supportFinishAfterTransition()
                }
            }
        }
        viewModel.startAnimationFlow()
    }

    private fun animateEnter() {
        val fadeAnimation = FadeTransition(isFadeIn = true)
        fadeAnimation.addTarget(appLogo)
        fadeAnimation.addTarget(appName)
        fadeAnimation.addTarget(credits)
        fadeAnimation.duration = 1000
        fadeAnimation.interpolator = AccelerateDecelerateInterpolator()
        TransitionManager.beginDelayedTransition(rootView, fadeAnimation)

        (appLogo.drawable as AnimatedVectorDrawable).start()
    }

    private fun animateWelcome() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(this, R.layout.constraint_set_activity_splash_scene_welcome)

        val transitionSet = TransitionSet()
        transitionSet.ordering = TransitionSet.ORDERING_SEQUENTIAL
        transitionSet.addTransition(FadeTransition(isFadeIn = false).apply {
            addTarget(appLogo)
            duration = 400
            interpolator = AnticipateInterpolator()
            addListener(object : AnimationListenerImpl() {
                override fun onTransitionEnd(transition: Transition?) {
                    super.onTransitionEnd(transition)
                    (appLogo.drawable as AnimatedVectorDrawable).stop()
                }
            })
        })
        transitionSet.addTransition(TransitionSet().apply {
            duration = 700
            ordering = TransitionSet.ORDERING_TOGETHER
            addTransition(ChangeBounds().apply {
                addTarget(backgroundTip)
                addTarget(appName)
                interpolator = DecelerateInterpolator(3f)
            })
            addTransition(ResizeTextTransition(28f).apply {
                addTarget(appName)
                interpolator = DecelerateInterpolator(3f)
            })
            addTransition(TranslateTransition(-background.measuredHeight.toFloat()).apply {
                addTarget(background)
                duration = 1000
                interpolator = DecelerateInterpolator(3f)
            })
            addTransition(TextColorTransition(ResourcesCompat.getColor(resources, R.color.black_87, null)).apply {
                addTarget(credits)
                interpolator = DecelerateInterpolator(3f)
                addListener(object : AnimationListenerImpl() {
                    override fun onTransitionEnd(transition: Transition?) {
                        super.onTransitionEnd(transition)
                        credits.setTextColor(
                            ResourcesCompat.getColorStateList(resources, R.color.selector_text_button_black, null)
                        )
                    }
                })
            })
        })
        transitionSet.addTransition(TransitionSet().apply {
            duration = 500
            ordering = TransitionSet.ORDERING_TOGETHER
            addTransition(FadeTransition(isFadeIn = true).apply {
                addTarget(welcomeContainer)
                interpolator = AccelerateDecelerateInterpolator()
            })
            addTransition(TranslateTransition(0f).apply {
                addTarget(welcomeContainer)
                interpolator = DecelerateInterpolator(3f)
            })
            addListener(object : AnimationListenerImpl() {
                override fun onTransitionStart(transition: Transition?) {
                    super.onTransitionStart(transition)
                    welcomeContainer.visibility = View.VISIBLE
                }
            })
        })

        TransitionManager.beginDelayedTransition(rootView, transitionSet)
        constraintSet.applyTo(rootView)
    }

    private fun animateExit() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(this, R.layout.constraint_set_activity_splash_scene_exit)

        val transition = ChangeBounds().apply {
            addTarget(appLogo)
            addTarget(appName)
            addTarget(credits)
            duration = 700
            interpolator = DecelerateInterpolator(3f)
        }

        TransitionManager.beginDelayedTransition(rootView, transition)
        constraintSet.applyTo(rootView)
    }

    private fun animateWelcomeExit() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(this, R.layout.constraint_set_activity_splash_scene_welcome_exit)

        val transitionSet = TransitionSet()
        transitionSet.ordering = TransitionSet.ORDERING_TOGETHER
        transitionSet.duration = 700
        transitionSet.addTransition(ChangeBounds().apply {
            addTarget(backgroundTip)
            addTarget(appName)
            addTarget(credits)
            addTarget(welcomeContainer)
            interpolator = DecelerateInterpolator(3f)
        })
        transitionSet.addTransition(TranslateTransition(0f).apply {
            addTarget(background)
            interpolator = DecelerateInterpolator(3f)
        })

        TransitionManager.beginDelayedTransition(rootView, transitionSet)
        constraintSet.applyTo(rootView)
    }
}