package ronybrosh.rocketlauncher.presentation.features.common.view.stickyheaders

import android.view.View

interface StickyHeaderListener {
    fun getHeaderPositionForItem(position: Int): Int
    fun getHeaderLayoutResourceId(position: Int): Int
    fun bindHeaderData(header: View, position: Int)
    fun isHeader(position: Int): Boolean
}