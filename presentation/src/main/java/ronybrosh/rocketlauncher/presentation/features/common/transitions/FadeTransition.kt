package ronybrosh.rocketlauncher.presentation.features.common.transitions

import android.animation.Animator
import android.animation.ValueAnimator
import android.transition.Transition
import android.transition.TransitionValues
import android.view.ViewGroup


class FadeTransition(val isFadeIn: Boolean) : Transition() {
    private companion object {
        const val ALPHA =
            "ronybrosh.rocketlauncher.presentation.features.common.transitions:FadeTransition:alpha"
    }

    override fun captureStartValues(transitionValues: TransitionValues) {
        transitionValues.values[ALPHA] = if (isFadeIn) 0f else 1f
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        transitionValues.values[ALPHA] = if (isFadeIn) 1f else 0f
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
            startValues.values[ALPHA] as Float,
            endValues.values[ALPHA] as Float
        )
        animator.addUpdateListener { value ->
            view.alpha = value.animatedValue as Float
        }
        return animator
    }
}