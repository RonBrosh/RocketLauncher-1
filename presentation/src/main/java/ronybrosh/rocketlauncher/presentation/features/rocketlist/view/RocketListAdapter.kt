package ronybrosh.rocketlauncher.presentation.features.rocketlist.view

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.viewholder_rocket_item.view.*
import ronybrosh.rocketlauncher.presentation.R
import ronybrosh.rocketlauncher.presentation.features.common.model.PresentableRocket

class RocketListAdapter(private val clickListener: View.OnClickListener) :
    RecyclerView.Adapter<RocketListAdapter.RocketListViewHolder>() {

    private var data: List<PresentableRocket> = emptyList()

    inner class RocketListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val placeHolder: Drawable? = itemView.context.getDrawable(R.drawable.ic_image_place_holder)
        fun bind(data: PresentableRocket) {
            itemView.rocketName.text = data.name
            itemView.rocketCountry.text = data.country
            itemView.isActive.text = if (data.isActive) itemView.context.getString(R.string.rocket_list_active_label) else ""
            itemView.rocketEnginesCount.text =
                itemView.context.getString(R.string.engines_count_format, data.enginesCount)
            Picasso.with(itemView.context).load(data.imageUrl).placeholder(placeHolder).into(itemView.rocketImage)
            ViewCompat.setTransitionName(itemView.rocketImage, data.id)
            itemView.tag = data
            itemView.setOnClickListener(clickListener)
        }
    }

    fun setData(data: List<PresentableRocket>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RocketListViewHolder {
        return RocketListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.viewholder_rocket_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RocketListViewHolder, position: Int) {
        holder.bind(data[position])
    }
}