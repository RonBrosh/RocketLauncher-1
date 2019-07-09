package ronybrosh.rocketlauncher.presentation.features.common.listeners

import android.transition.Transition
import android.view.animation.Animation

abstract class AnimationListenerImpl : Animation.AnimationListener, Transition.TransitionListener {
    // Animation.AnimationListener
    override fun onAnimationRepeat(animation: Animation?) {
    }

    override fun onAnimationEnd(animation: Animation?) {
    }

    override fun onAnimationStart(animation: Animation?) {
    }

    // Transition.TransitionListener
    override fun onTransitionEnd(transition: Transition?) {
    }

    override fun onTransitionResume(transition: Transition?) {
    }

    override fun onTransitionPause(transition: Transition?) {
    }

    override fun onTransitionCancel(transition: Transition?) {
    }

    override fun onTransitionStart(transition: Transition?) {
    }
}