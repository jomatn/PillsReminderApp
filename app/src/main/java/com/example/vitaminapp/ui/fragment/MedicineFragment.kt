package com.example.vitaminapp.ui.fragment

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.vitaminapp.data.di.MedicinesModel
import com.example.vitaminapp.ui.interfaces.OnClickItem
import com.example.vitaminapp.databinding.FragmentMedicineBinding
import com.example.vitaminapp.ui.notification.AlarmReceiver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class MedicinesFragment : Fragment(), OnClickItem {

    private var _binding: FragmentMedicineBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ReminderAdapter
    private val viewModel: ReminderViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMedicineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ReminderAdapter(onClickItem = this, onLongClickItem = this)
        binding.rvAlarm.adapter = adapter
        addData()
    }

    private fun addData() {
        viewModel.getAll().observe(viewLifecycleOwner) { medicines ->
            adapter.submitList(medicines)
        }
    }
    private fun convertTimeToMillis(time: String): Long {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = sdf.parse(time)

        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        if (calendar.timeInMillis < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        return calendar.timeInMillis
    }
    private fun scheduleNotification(context: Context, title: String, message: String, timeInMillis: Long) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("title", title)
            putExtra("message", message)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
            } else {
                Toast.makeText(context, "Включите уведомления", Toast.LENGTH_SHORT).show()
            }
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
        }
    }

    override fun onLongClick(medicinesModel: MedicinesModel) {
        lifecycleScope.launch {
            viewModel.delete(medicinesModel)
        }
    }

    override fun onClick(medicinesModel: MedicinesModel) {
        TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}