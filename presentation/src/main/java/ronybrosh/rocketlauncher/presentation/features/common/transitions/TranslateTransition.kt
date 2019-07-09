package ronybrosh.rocketlauncher.presentation.features.common.transitions

import android.animation.Animator
import android.animation.ValueAnimator
import android.transition.Transition
import android.transition.TransitionValues
import android.view.ViewGroup


class TranslateTransition(private val endValue: Float) : Transition() {
    private companion object {
        const val TRANSLATEION_Y =
            "ronybrosh.rocketlauncher.presentation.features.common.transitions:TranslateTransition:translationY"
    }

    override fun captureStartValues(transitionValues: TransitionValues) {
        transitionValues.values[TRANSLATEION_Y] = transitionValues.view.translationY
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        transitionValues.values[TRANSLATEION_Y] = endValue
    }

    override fun createAnimator(
        sceneRoot: ViewGroup?,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator {
        if (startValues == null || endValues == null)
            return super.createAnimator(sceneRoot, startValues, endValues)

        val view = endValues.view
        val animator = ValueAnimator.ofFloat(
            startValues.values[TRANSLATEION_Y] as Float,
            endValues.values[TRANSLATEION_Y] as Float
        )
        animator.addUpdateListener { value ->
            view.translationY = value.animatedValue as Float
        }
        return animator
    }
}