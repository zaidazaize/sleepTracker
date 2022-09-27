package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.SleepListitemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

class SleepAdapter(private val sleepClickListener: SleepClickListener) :
    ListAdapter<DataItem, ViewHolder>(SleepNightDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> SleepViewHolder.from(parent)
            else -> throw java.lang.ClassCastException("Unknown View Type ${viewType}")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is SleepViewHolder -> {
                val nightItem = getItem(position) as DataItem.SleepNightItem
                holder.let {
                    holder.bind(sleepClickListener, nightItem.sleepNight)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.SleepNightItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    private val adapterScope = CoroutineScope(Dispatchers.Default)
    fun addHeaderAndSubmitList(list: List<SleepNight>?) {
        adapterScope.launch {
            val item = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.SleepNightItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(item)
            }
        }
    }

    class SleepViewHolder private constructor(private val binding: SleepListitemBinding) :
        ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): SleepViewHolder {
                val binding = SleepListitemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return SleepViewHolder(binding)
            }
        }

        fun bind(
            sleepClickListener: SleepClickListener,
            night: SleepNight
        ) {
            binding.sleep = night
            binding.sleepClickListener = sleepClickListener
            binding.executePendingBindings()
        }
    }

    class TextViewHolder(view: View) : ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.header, parent, false)
                return TextViewHolder(view)
            }
        }
    }
}

class SleepNightDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        //note in data class == compares value of each field (implemented by default)
        return oldItem == newItem
    }
}

class SleepClickListener(val clickListener: (sleepId: Long) -> Unit) {
    fun onClick(sleepNight: SleepNight) = clickListener(sleepNight.nightId)
}

sealed class DataItem {
    data class SleepNightItem(val sleepNight: SleepNight) : DataItem() {
        override val id = sleepNight.nightId
    }

    object Header : DataItem() {
        override val id = Long.MIN_VALUE
    }

    abstract val id: Long

}