package com.udacity.asteroidradar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import kotlinx.android.synthetic.main.list_asteroid_item.view.*

class AsteriodAdapter(val imageList: List<Asteroid>, val context: Context,val onNoteListener: OnNoteListener) : RecyclerView.Adapter<AsteriodAdapter.AsteroidViewViewHolder>() {

    lateinit var mOnNoteListener : OnNoteListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewViewHolder {
        val viewHolder =
            AsteroidViewViewHolder(LayoutInflater.from(context).inflate(R.layout.list_asteroid_item, parent, false))
        val position = viewHolder.adapterPosition

        return viewHolder
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: AsteroidViewViewHolder, position: Int) {
        val timeLine = imageList[position]
        holder.asteroidName.text = timeLine.codename
        holder.asteroidDate.text = timeLine.closeApproachDate

        if(timeLine.isPotentiallyHazardous){

            holder.hazardImageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
        }else{
            holder.hazardImageView.setImageResource(R.drawable.ic_not_hazard)
        }
        holder.itemView.setOnClickListener{ view ->
            var bundle = bundleOf("selectedAsteroid" to timeLine)
            view.findNavController().navigate(R.id.action_showDetail,bundle)

        }

       //holder.bindItems(imageList[position],onNoteListener)





    }

    class AsteroidViewViewHolder(
        view: View

    ) : RecyclerView.ViewHolder(view),View.OnClickListener {

        lateinit var onNoteListener : OnNoteListener
        val asteroidName = view.asteroidNameTextView
        val asteroidDate = view.asteroidDateTextView
        val hazardImageView = view.isHazardImageView
        val layoutItem = view.layoutItem
        fun bindItems(user: Asteroid,onNoteListener: OnNoteListener) {
            val layoutItem =  itemView.findViewById(R.id.layoutItem) as LinearLayout
            itemView.setOnClickListener(this)
            this.onNoteListener = onNoteListener

        }


        override fun onClick(v: View?) {
            onNoteListener.onNoteClick(adapterPosition)
        }

    }
    public interface OnNoteListener{
        fun onNoteClick(position : Int)
    }


}

