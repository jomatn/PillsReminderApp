package com.example.vitaminapp.ui.fragment

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.vitaminapp.data.di.MedicinesModel
import com.example.vitaminapp.ui.interfaces.OnClickItem
import com.example.vitaminapp.databinding.ItemAlarmBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ReminderAdapter(
    private val onLongClickItem: OnClickItem,
    private val onClickItem: OnClickItem,
) : ListAdapter<MedicinesModel, ReminderAdapter.ReminderViewHolder>(
    DiffCallback()
)
{
    class ReminderViewHolder(private val binding: ItemAlarmBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: MedicinesModel)= with(binding) {
            title.text = model.title
            description.text = model.description
            progressBar.progress = model.quantity
            time.text =if (model.date != "no date")
                model.date + " " + model.time else  model.time
            val currentDate = Calendar.getInstance().time
            val delayedDate = if (model.date != null && model.date != "No date selected")
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(model.date)
            else
                null

            if (delayedDate != null && currentDate.before(delayedDate)) (root as CardView).setCardBackgroundColor(
                Color.GRAY)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val binding = ItemAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReminderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnLongClickListener {
            onLongClickItem.onLongClick(getItem(position))
            true
        }
        holder.itemView.setOnClickListener {
            onClickItem.onClick(getItem(position))
        }

    }

}

class DiffCallback : DiffUtil.ItemCallback<MedicinesModel>() {
    override fun areItemsTheSame(oldItem: MedicinesModel, newItem: MedicinesModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MedicinesModel, newItem: MedicinesModel): Boolean {
        return oldItem == newItem
    }
}
