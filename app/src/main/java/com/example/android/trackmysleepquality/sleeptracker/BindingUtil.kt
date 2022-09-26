package com.example.android.trackmysleepquality.sleeptracker

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight
import org.w3c.dom.Text

//bind the image with specific images
@BindingAdapter("sleepImage")
fun ImageView.setSleepImage(item: SleepNight?) {
    item?.let {
        setImageResource(
            when (item.sleepQuality) {
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

//binds the sleep duration text view in the image
@BindingAdapter("sleepDurationFormatted")
fun TextView.setSleepDurationFormatted(item: SleepNight?) {
    item?.let {
        text = (item.endTimeMilli - item.startTimeMilli).toString()
    }
}

//input the text string as per the sleep quality
@BindingAdapter("sleepQualityString")
fun TextView.setQualityString(item : SleepNight?){
    item?.let {
        text = convertNumericQualityToString(item.sleepQuality,context.resources)
    }
}