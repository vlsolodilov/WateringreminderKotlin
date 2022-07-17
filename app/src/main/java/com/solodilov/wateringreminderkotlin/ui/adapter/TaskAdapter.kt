package com.solodilov.wateringreminderkotlin.ui.adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.solodilov.wateringreminderkotlin.R
import com.solodilov.wateringreminderkotlin.databinding.ItemTaskBinding
import com.solodilov.wateringreminderkotlin.presentation.Task

class TaskAdapter(
    private val onClickCheckBox: (Task) -> Unit,
): ListAdapter<Task, TaskItemViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskItemViewHolder =
        TaskItemViewHolder(
            ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClickCheckBox,
        )


    override fun onBindViewHolder(holder: TaskItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class TaskItemViewHolder(private val binding: ItemTaskBinding, private val onClickCheckBox: (Task) -> Unit)
    : RecyclerView.ViewHolder(binding.root){

    fun bind(task: Task) {

        if (!task.imagePlant.equals("empty", true)){
            binding.imageTask.setImageURI(Uri.parse(task.imagePlant))
        } else {
            binding.imageTask.setImageResource(R.drawable.ic_plant_in_pot)
        }

        binding.nameTask.text = task.taskName
        binding.namePlant.text = task.plantName
        binding.checkBoxTask.isChecked = task.isSelect

        binding.checkBoxTask.setOnClickListener { onClickCheckBox(task) }
    }
}

private class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {

    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        Log.d("TAG", "areItemsTheSame: ")
        return oldItem.reminderId == newItem.reminderId
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        Log.d("TAG", "areContentsTheSame: ${oldItem.isSelect == newItem.isSelect}")
        return oldItem.isSelect == newItem.isSelect
    }
}
