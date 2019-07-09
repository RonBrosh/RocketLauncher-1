package ronybrosh.rocketlauncher.presentation.features.rocketdetails.view

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.viewholder_launch_header.view.*
import kotlinx.android.synthetic.main.viewholder_launch_info.view.*
import kotlinx.android.synthetic.main.viewholder_launch_item.view.*
import ronybrosh.rocketlauncher.presentation.R
import ronybrosh.rocketlauncher.presentation.features.common.view.stickyheaders.StickyHeaderListener
import ronybrosh.rocketlauncher.presentation.features.rocketdetails.model.RecyclerViewEntry

class RocketLaunchesAdapter :
    RecyclerView.Adapter<RocketLaunchesAdapter.BaseViewHolder>(),
    StickyHeaderListener {

    private var data: List<RecyclerViewEntry>? = null

    abstract inner class BaseViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {
        val placeHolder: Drawable? = itemView.context.getDrawable(R.drawable.ic_patch_place_holder)
        abstract fun bind(item: RecyclerViewEntry)
    }

    inner class ItemViewHolder(rootView: View) : BaseViewHolder(rootView) {
        override fun bind(item: RecyclerViewEntry) {
            if (item !is RecyclerViewEntry.Item)
                return
            itemView.missionName.text = item.missionName
            itemView.missionDate.text = item.launchDate
            itemView.isSuccessful.setText(item.isSuccessfulTextResourceId)

            val imageUrl: String? = with(item.patchImageUrl, {
                if (this == null || this.isEmpty())
                    null
                else
                    this
            })
            Picasso.with(itemView.context).load(imageUrl)
                .placeholder(placeHolder)
                .into(itemView.patchImage)
        }
    }

    inner class HeaderViewHolder(rootView: View) : BaseViewHolder(rootView) {
        override fun bind(item: RecyclerViewEntry) {
            if (item is RecyclerViewEntry.Header)
                itemView.title.text = item.title
        }
    }

    inner class InfoViewHolder(rootView: View) : BaseViewHolder(rootView) {
        private val indexAxisValueFormatter = object : IndexAxisValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }
        }

        private val barChart: BarChart = itemView.barChart

        init {
            barChart.setTouchEnabled(false)
            barChart.description.isEnabled = false
            barChart.legend.isEnabled = false
            barChart.setNoDataText(itemView.context.getString(R.string.rocket_details_screen_bar_chart_no_data_message))
            barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
            barChart.xAxis.setDrawGridLines(false)
            barChart.xAxis.isGranularityEnabled = true
            barChart.xAxis.valueFormatter = indexAxisValueFormatter
            barChart.axisRight.isEnabled = false
            barChart.axisLeft.valueFormatter = indexAxisValueFormatter
            barChart.axisLeft.isGranularityEnabled = true
            barChart.axisLeft.setDrawGridLines(false)
            barChart.setNoDataTextColor(ContextCompat.getColor(itemView.context, R.color.purple_dark))
        }

        override fun bind(item: RecyclerViewEntry) {
            if (item is RecyclerViewEntry.Info) {
                itemView.rocketDescription.text = item.description

                if (item.barEntries.isNotEmpty()) {
                    val barDataSet = BarDataSet(
                        item.barEntries,
                        itemView.context.getString(R.string.rocket_details_screen_bar_chart_label)
                    )
                    barDataSet.valueFormatter = indexAxisValueFormatter
                    barDataSet.valueTextSize = itemView.context.resources.getInteger(R.integer.bar_chart_value_text_size)
                        .toFloat()
                    barDataSet.colors = listOf(
                        ContextCompat.getColor(itemView.context, R.color.purple_dark),
                        ContextCompat.getColor(itemView.context, R.color.blue)
                    )

                    val barData = BarData(barDataSet)
                    barData.barWidth = 0.9f
                    barChart.data = barData
                    barChart.xAxis.labelCount = item.barEntries.size
                    barChart.animateXY(
                        itemView.context.resources.getInteger(android.R.integer.config_longAnimTime),
                        itemView.context.resources.getInteger(android.R.integer.config_longAnimTime),
                        Easing.EaseOutBack
                    )
                } else {
                    barChart.clear()
                }
            }
        }
    }

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_ITEM = 1
        const val TYPE_INFO = 2
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        data?.let { list ->
            return when (list[position]) {
                is RecyclerViewEntry.Info -> {
                    TYPE_INFO
                }
                is RecyclerViewEntry.Header -> {
                    TYPE_HEADER
                }
                else -> {
                    TYPE_ITEM
                }
            }
        }
        return TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            TYPE_INFO -> {
                InfoViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.viewholder_launch_info,
                        parent,
                        false
                    )
                )
            }
            TYPE_HEADER -> {
                HeaderViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.viewholder_launch_header,
                        parent,
                        false
                    )
                )
            }
            else -> {
                // TYPE_ITEM
                ItemViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.viewholder_launch_item,
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        data?.let { list ->
            holder.bind(list[position])
        }
    }

    override fun getHeaderPositionForItem(position: Int): Int {
        for (index: Int in position downTo 0) {
            if (isHeader(index)) {
                return index
            }
        }
        return 0
    }

    override fun getHeaderLayoutResourceId(position: Int): Int {
        return R.layout.viewholder_launch_header
    }

    override fun bindHeaderData(header: View, position: Int) {
        data?.let { list ->
            list[position].let { item ->
                if (item is RecyclerViewEntry.Header) {
                    header.title.text = item.title
                }
            }
        }
    }

    override fun isHeader(position: Int): Boolean {
        return getItemViewType(position) == TYPE_HEADER
    }

    fun setData(data: List<RecyclerViewEntry>) {
        this.data = data
        notifyDataSetChanged()
    }
}