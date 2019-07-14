package ronybrosh.rocketlauncher.presentation.utils

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import ronybrosh.rocketlauncher.presentation.R

object SnackbarUtil {
    fun showWithDismissAction(text: String, view: View) {
        val snackbar = createSnackbar(text, view)
        snackbar.setAction(view.context.getString(R.string.error_dismiss_button)) {
            snackbar.dismiss()
        }
        snackbar.show()
    }

    fun showWithRetryAction(text: String, view: View, retryCallback: () -> Unit) {
        val snackbar = createSnackbar(text, view)
        snackbar.setAction(view.context.getString(R.string.error_retry_button)) {
            snackbar.dismiss()
            retryCallback()
        }
        snackbar.show()
    }

    private fun createSnackbar(text: String, view: View): Snackbar {
        val snackbar = Snackbar.make(view, text, Snackbar.LENGTH_INDEFINITE)
        snackbar.setActionTextColor(ContextCompat.getColor(view.context, R.color.pink_light))
        snackbar.view.setBackgroundResource(R.drawable.blue_round_corners_background)
        val margin: Int = view.context.resources.getDimensionPixelSize(R.dimen.spacing_normal)
        val params = snackbar.view.layoutParams as ViewGroup.MarginLayoutParams
        params.bottomMargin = margin
        params.leftMargin = margin
        params.rightMargin = margin
        return snackbar
    }
}