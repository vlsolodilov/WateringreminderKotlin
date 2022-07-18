package com.solodilov.wateringreminderkotlin.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.solodilov.wateringreminderkotlin.databinding.ItemCalendarBinding
import com.solodilov.wateringreminderkotlin.presentation.CalendarItem
import com.solodilov.wateringreminderkotlin.ui.DateTimeConverter

class CalendarAdapter : RecyclerView.Adapter<CalendarItemViewHolder>() {

    var calendarItemList: List<CalendarItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarItemViewHolder =
        CalendarItemViewHolder(ItemCalendarBinding
                .inflate(LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(holder: CalendarItemViewHolder, position: Int) {
        holder.bind(calendarItemList[position])
    }

    override fun getItemCount(): Int =
        calendarItemList.size
}

class CalendarItemViewHolder(private val binding: ItemCalendarBinding)
    : RecyclerView.ViewHolder(binding.root){

    fun bind(calendarItem: CalendarItem) {
        binding.calendarDate.text = DateTimeConverter.getFormattedDate(calendarItem.date)
        binding.calendarDescription.text = calendarItem.description
    }
}
