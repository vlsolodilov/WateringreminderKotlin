package com.solodilov.wateringreminderkotlin.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.solodilov.wateringreminderkotlin.databinding.ItemReminderBinding
import com.solodilov.wateringreminderkotlin.domain.entity.Reminder
import com.solodilov.wateringreminderkotlin.extension.DateTimeConverter

class ReminderAdapter(private val onClick: (Reminder) -> Unit) :
    RecyclerView.Adapter<ReminderItemViewHolder>() {

    var reminderList: List<Reminder> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderItemViewHolder =
        ReminderItemViewHolder(ItemReminderBinding
            .inflate(LayoutInflater.from(parent.context), parent, false), onClick)

    override fun onBindViewHolder(holder: ReminderItemViewHolder, position: Int) {
        holder.bind(reminderList[position])
    }

    override fun getItemCount(): Int =
        reminderList.size
}

class ReminderItemViewHolder(
    private val binding: ItemReminderBinding,
    private val onClick: (Reminder) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(reminderItem: Reminder) {

        binding.reminderName.text = reminderItem.name
        binding.reminderInterval.text = reminderItem.signalPeriod.toString()
        binding.reminderTime.text = DateTimeConverter.getFormattedTime(reminderItem.signalTime)
        binding.reminderLastAction.text =
            DateTimeConverter.getFormattedDate(reminderItem.lastSignalDate)

        itemView.setOnClickListener { onClick(reminderItem) }
    }
}
