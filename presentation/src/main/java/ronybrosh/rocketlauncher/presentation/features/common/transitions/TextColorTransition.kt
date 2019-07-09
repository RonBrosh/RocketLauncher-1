package ronybrosh.rocketlauncher.presentation.features.common.transitions

import android.animation.Animator
import android.animation.ValueAnimator
import android.transition.Transition
import android.transition.TransitionValues
import android.view.ViewGroup
import android.widget.TextView
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator


class TextColorTransition(private val endValue: Int) : Transition() {
    private companion object {
        const val TEXT_COLOR =
            "ronybrosh.rocketlauncher.presentation.features.common.transitions:TextColorTransition:textColor"
    }

    override fun captureStartValues(transitionValues: TransitionValues) {
        val view = transitionValues.view as TextView
        transitionValues.values[TEXT_COLOR] = view.currentTextColor
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        transitionValues.values[TEXT_COLOR] = endValue
    }

    override fun createAnimator(
        sceneRoot: ViewGroup?,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator {
        if (startValues == null || endValues == null)
            return super.createAnimator(sceneRoot, startValues, endValues)

        val textView = endValues.view as TextView
        val animator =
            ValueAnimator.ofObject(ArgbEvaluator(), startValues.values[TEXT_COLOR], endValues.values[TEXT_COLOR])
        animator.addUpdateListener { value ->
            textView.setTextColor(value.animatedValue as Int)
        }
        return animator
    }
}