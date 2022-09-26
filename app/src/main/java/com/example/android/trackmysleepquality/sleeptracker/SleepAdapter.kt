package com.example.android.trackmysleepquality.sleeptracker

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.SleepListitemBinding

class SleepAdapter(private val context: Context) :
    ListAdapter<SleepNight,SleepAdapter.SleepViewHolder>(SleepNightDiffCallback()) {

    //remove these as listAdapter will have method to handle list and its items
//    var data = listOf<SleepNight>()
//        set(value) {
//            field = value
//            notifyDataSetChanged()
//        }

//    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SleepViewHolder {
        return SleepViewHolder.from( parent)
    }

    override fun onBindViewHolder(holder: SleepViewHolder, position: Int) {
        val d = getItem(position)
        holder.bind(d)
    }


    class SleepViewHolder private constructor(val binding: SleepListitemBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object {
             fun from( parent: ViewGroup): SleepViewHolder {
                 val binding = SleepListitemBinding.inflate(
                     LayoutInflater.from(parent.context),
                     parent,
                     false
                )
                return SleepViewHolder(binding)
            }
        }

        fun bind(
            d: SleepNight
        ) {
            binding.startTime.text =
                convertNumericQualityToString(d.sleepQuality, binding.root.resources)
            binding.hours.text = (d.endTimeMilli - d.endTimeMilli).toString()
            binding.imageViewQuality.setImageResource(
                when (d.sleepQuality) {
                    0 -> R.drawable.ic_sleep_0
                    1 -> R.drawable.ic_sleep_1
                    2 -> R.drawable.ic_sleep_2
                    3 -> R.drawable.ic_sleep_3
                    4 -> R.drawable.ic_sleep_4
                    5 -> R.drawable.ic_sleep_5
                    else -> R.drawable.ic_sleep_0
                }
            )
        }
    }
}
class SleepNightDiffCallback : DiffUtil.ItemCallback<SleepNight>(){
    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem.nightId == newItem.nightId
    }

    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        //note in data class == compares value of each field (implemented by default)
        return oldItem == newItem
    }

}
