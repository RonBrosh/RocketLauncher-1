package ronybrosh.rocketlauncher.presentation.features.common.view.stickyheaders

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class StickyHeaderItemDecoration(private val drawable: Drawable?, private val listener: StickyHeaderListener) :
    RecyclerView.ItemDecoration() {
    private var headerHeight: Int = 0
    private val bounds: Rect = Rect()

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (drawable == null)
            return

        canvas.save()
        val left: Int = when (parent.clipToPadding) {
            true -> {
                parent.paddingLeft
            }
            else -> {
                0
            }
        }
        val right: Int = when (parent.clipToPadding) {
            true -> {
                parent.width - parent.paddingRight
            }
            else -> {
                parent.width
            }
        }

        // Iterate all children but the last one as we don't want to draw divider for the last child.
        for (index: Int in 0 until parent.childCount - 1) {
            // If this child or the next is a header, don't draw the divider.
            if (isChildHeader(index, parent) || isChildHeader(index + 1, parent))
                continue

            parent.getChildAt(index)?.let { child ->
                parent.getDecoratedBoundsWithMargins(child, bounds)
                val bottom: Int = bounds.bottom + child.translationY.roundToInt()
                val top: Int = bottom - drawable.intrinsicHeight
                drawable.setBounds(left, top, right, bottom)
                drawable.draw(canvas)
            }
        }
        canvas.restore()
    }

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(canvas, parent, state)
        parent.getChildAt(0)?.let { topChild ->
            val topChildPosition: Int = parent.getChildAdapterPosition(topChild)
            if (topChildPosition == RecyclerView.NO_POSITION)
                return

            if (topChildPosition == 0)
                return

            val headerPosition: Int = listener.getHeaderPositionForItem(topChildPosition)
            val currentHeader: View = getHeaderViewForItem(headerPosition, parent)
            fixLayoutSize(parent, currentHeader)

            val contactPoint: Int = currentHeader.bottom
            val childInContact: View? = getChildInContact(parent, contactPoint, headerPosition)
            if (childInContact != null && listener.isHeader(parent.getChildAdapterPosition(childInContact))) {
                moveHeader(canvas, currentHeader, childInContact)
                return
            }

            drawHeader(canvas, currentHeader)
        }
    }

    private fun getHeaderViewForItem(position: Int, parent: RecyclerView): View {
        val header: View =
            LayoutInflater.from(parent.context).inflate(listener.getHeaderLayoutResourceId(position), parent, false)
        listener.bindHeaderData(header, position)
        return header
    }

    private fun drawHeader(canvas: Canvas, header: View) {
        canvas.save()
        canvas.translate(0f, 0f)
        header.draw(canvas)
        canvas.restore()
    }

    private fun moveHeader(canvas: Canvas, currentHeader: View, nextHeader: View) {
        canvas.save()
        canvas.translate(0f, (nextHeader.top - currentHeader.height).toFloat())
        currentHeader.draw(canvas)
        canvas.restore()
    }

    private fun fixLayoutSize(parent: ViewGroup, view: View) {
        // Specs for parent (RecyclerView)
        val widthSpec: Int = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        val heightSpec: Int = View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)

        // Specs for children (headers)
        val childWidthSpec: Int = ViewGroup.getChildMeasureSpec(
            widthSpec,
            parent.paddingLeft + parent.paddingRight,
            view.layoutParams.width
        )
        val childHeightSpec: Int = ViewGroup.getChildMeasureSpec(
            heightSpec,
            parent.paddingTop + parent.paddingBottom,
            view.layoutParams.height
        )

        view.measure(childWidthSpec, childHeightSpec)
        headerHeight = view.measuredHeight
        view.layout(0, 0, view.measuredWidth, headerHeight)
    }

    private fun getChildInContact(parent: RecyclerView, contactPoint: Int, currentHeaderPosition: Int): View? {
        for (index: Int in 0 until parent.childCount) {
            var heightTolerance = 0
            parent.getChildAt(index)?.let { child ->
                //measure height tolerance with child if child is another header
                if (currentHeaderPosition != index) {
                    if (listener.isHeader(parent.getChildAdapterPosition(child))) {
                        heightTolerance = headerHeight - child.height
                    }
                }

                //add heightTolerance if child top be in display area
                val childBottomPosition: Int = if (child.top > 0) {
                    child.bottom + heightTolerance
                } else {
                    child.bottom
                }

                if (childBottomPosition > contactPoint) {
                    if (child.top <= contactPoint) {
                        // This child overlaps the contactPoint
                        return child
                    }
                }
            }
        }
        return null
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (drawable == null) {
            outRect.set(0, 0, 0, 0)
            return
        }
        outRect.set(0, 0, 0, drawable.intrinsicHeight)
    }

    private fun isChildHeader(index: Int, parent: RecyclerView): Boolean {
        parent.getChildAt(index)?.let {
            val position: Int = parent.getChildAdapterPosition(it)
            if (position != RecyclerView.NO_POSITION && listener.isHeader(position))
                return true
        }
        return false
    }
}