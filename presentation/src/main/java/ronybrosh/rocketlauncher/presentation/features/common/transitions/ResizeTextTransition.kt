package ronybrosh.rocketlauncher.presentation.features.common.transitions

import android.animation.Animator
import android.animation.ValueAnimator
import android.transition.Transition
import android.transition.TransitionValues
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.TextView


class ResizeTextTransition(private val endValue: Float) : Transition() {
    private companion object {
        const val FONT_SIZE = "ronybrosh.rocketlauncher.presentation.features.common.transitions:ResizeTextTransition:textSize"
    }

    override fun captureStartValues(transitionValues: TransitionValues) {
        val view = transitionValues.view as TextView
        transitionValues.values[FONT_SIZE] = (view.textSize / view.context.resources.displayMetrics.scaledDensity)
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        transitionValues.values[FONT_SIZE] = endValue
    }

    override fun createAnimator(
        sceneRoot: ViewGroup?,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator {
        if (startValues == null || endValues == null)
            return super.createAnimator(sceneRoot, startValues, endValues)

        val textView = endValues.view as TextView
        val animator = ValueAnimator.ofFloat(
            startValues.values[FONT_SIZE] as Float,
            endValues.values[FONT_SIZE] as Float
        )
        animator.addUpdateListener { value ->
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, value.animatedValue as Float)
        }
        return animator
    }
}