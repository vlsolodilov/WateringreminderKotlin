package com.solodilov.wateringreminderkotlin.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.solodilov.wateringreminderkotlin.MainApplication
import com.solodilov.wateringreminderkotlin.R
import com.solodilov.wateringreminderkotlin.databinding.FragmentReminderBinding
import com.solodilov.wateringreminderkotlin.domain.entity.Plant
import com.solodilov.wateringreminderkotlin.domain.entity.Reminder
import com.solodilov.wateringreminderkotlin.presentation.ReminderViewModel
import com.solodilov.wateringreminderkotlin.extension.DateTimeConverter
import java.lang.reflect.Array
import java.util.*
import javax.inject.Inject

class ReminderFragment : Fragment() {
    companion object {
        private const val LAUNCH_MODE_KEY = "launch mode"
        private const val LAUNCH_MODE_EDIT = "launch mode edit"
        private const val LAUNCH_MODE_ADD = "launch mode add"
        private const val LAUNCH_MODE_UNKNOWN = "UNKNOWN"
        private const val PLANT_ID = "plant id"
        private const val EDITED_REMINDER_ID = "edited reminder id"

        fun newInstance(plantId: Long): ReminderFragment {
            return ReminderFragment().apply {
                arguments = Bundle().apply {
                    putString(LAUNCH_MODE_KEY, LAUNCH_MODE_ADD)
                    putLong(PLANT_ID, plantId)
                }
            }
        }

        fun newInstance(plantId: Long, reminderId: Long): ReminderFragment {
            return ReminderFragment().apply {
                arguments = Bundle().apply {
                    putString(LAUNCH_MODE_KEY, LAUNCH_MODE_EDIT)
                    putLong(PLANT_ID, plantId)
                    putLong(EDITED_REMINDER_ID, reminderId)
                }
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: ReminderViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ReminderViewModel::class.java]
    }

    private var _binding: FragmentReminderBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("FragmentReminderBinding is null")

    private var screenMode: String = LAUNCH_MODE_UNKNOWN
    private var reminderId: Long = Reminder.UNDEFINED_ID
    private var plantId: Long = Plant.UNDEFINED_ID

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as MainApplication).component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    private fun parseParams() {
        if (!requireArguments().containsKey(LAUNCH_MODE_KEY)) {
            throw RuntimeException("Param LAUNCH_MODE_KEY is absent")
        }
        screenMode = requireArguments().getString(LAUNCH_MODE_KEY, LAUNCH_MODE_UNKNOWN)

        when (screenMode) {
            LAUNCH_MODE_EDIT -> {
                if (!requireArguments().containsKey(EDITED_REMINDER_ID)) {
                    throw RuntimeException("Param EDITED_REMINDER_ID is absent")
                }
                reminderId = requireArguments().getLong(EDITED_REMINDER_ID, Reminder.UNDEFINED_ID)
                if (reminderId == Reminder.UNDEFINED_ID) {
                    throw RuntimeException("Param EDITED_REMINDER_ID is absent")
                }
                if (!requireArguments().containsKey(PLANT_ID)) {
                    throw RuntimeException("Param PLANT_ID is absent")
                }
                plantId = requireArguments().getLong(PLANT_ID, Plant.UNDEFINED_ID)
                if (plantId == Plant.UNDEFINED_ID) {
                    throw RuntimeException("Param PLANT_ID is absent")
                }
            }
            LAUNCH_MODE_ADD -> {
                if (!requireArguments().containsKey(PLANT_ID)) {
                    throw RuntimeException("Param PLANT_ID is absent")
                }
                plantId = requireArguments().getLong(PLANT_ID, Plant.UNDEFINED_ID)
                if (plantId == Plant.UNDEFINED_ID) {
                    throw RuntimeException("Param PLANT_ID is absent")
                }
            }
            else -> throw RuntimeException("Unknown screen mode")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentReminderBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (screenMode) {
            LAUNCH_MODE_EDIT -> launchEditMode()
            LAUNCH_MODE_ADD -> launchAddMode()
        }

        initViews()
        observeViewModel()
    }

    private fun launchEditMode() {
        viewModel.getSavedReminder(reminderId)
        binding.saveReminder.setOnClickListener {
            viewModel.updateReminder(
                id = reminderId,
                name = binding.reminderName.text.toString(),
                signalTime = binding.signalTime.text.toString(),
                signalPeriod = binding.signalInterval.text.toString(),
                lastSignalDate = binding.lastSignalDate.text.toString(),
                plantId = plantId,
            )
        }

        binding.deleteReminder.setOnClickListener {
            viewModel.deleteReminder(reminderId)
        }
    }

    private fun launchAddMode() {
        binding.lastSignalDate.setText(DateTimeConverter.getFormattedDate(Date()))
        binding.saveReminder.setOnClickListener {
            viewModel.createReminder(
                name = binding.reminderName.text.toString(),
                signalTime = binding.signalTime.text.toString(),
                signalPeriod = binding.signalInterval.text.toString(),
                lastSignalDate = binding.lastSignalDate.text.toString(),
                plantId = plantId,
            )
        }
        binding.deleteReminder.isVisible = false
    }

    private fun initViews() {
        binding.signalTime.setOnClickListener { selectSignalTime() }
        binding.lastSignalDate.setOnClickListener { selectLastSignalDate() }
        binding.reminderName.setAdapter(ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_dropdown_item_1line,
            resources.getStringArray(R.array.reminders)
        ))
    }

    private fun observeViewModel() {
        viewModel.loading.observe(viewLifecycleOwner, ::toggleProgress)
        viewModel.savedReminder.observe(viewLifecycleOwner) { savedReminder ->
            showSavedReminder(savedReminder)
        }
        viewModel.saveReminderSuccessEvent.observe(viewLifecycleOwner) { startPlantFragment() }
        viewModel.deleteReminderSuccessEvent.observe(viewLifecycleOwner) { startPlantFragment() }
        viewModel.reminderErrorEvent.observe(viewLifecycleOwner) { showSaveReminderFailed() }
        viewModel.textFieldErrorEvent.observe(viewLifecycleOwner) { showTextFieldsFailed() }
    }

    private fun toggleProgress(visible: Boolean) {
        binding.reminderLoading.isVisible = visible
        binding.saveReminder.isEnabled = !visible
        binding.deleteReminder.isEnabled = !visible
    }

    private fun showSavedReminder(reminder: Reminder) {
        binding.reminderName.setText(reminder.name)
        binding.signalTime.setText(DateTimeConverter.getFormattedTime(reminder.signalTime))
        binding.signalInterval.setText(reminder.signalPeriod.toString())
        binding.lastSignalDate.setText(DateTimeConverter.getFormattedDate(reminder.lastSignalDate))
    }

    private fun startPlantFragment() {
        parentFragmentManager.popBackStack()
    }

    private fun showSaveReminderFailed() {
        Snackbar.make(binding.root, R.string.error, Snackbar.LENGTH_SHORT).show()
    }

    private fun showTextFieldsFailed() {
        Snackbar.make(binding.root, R.string.emptyField, Snackbar.LENGTH_SHORT).show()
    }

    private fun selectLastSignalDate() {
        val datePicker = MaterialDatePicker.Builder
            .datePicker()
            .build()
        datePicker.show(parentFragmentManager, "DatePicker")
        datePicker.addOnPositiveButtonClickListener {
            binding.lastSignalDate.setText(DateTimeConverter.getFormattedDate(Date(it)))
        }
    }

    private fun selectSignalTime() {
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .build()
        timePicker.show(parentFragmentManager, "DatePicker")
        timePicker.addOnPositiveButtonClickListener {
            binding.signalTime.setText(DateTimeConverter.getTime(
                timePicker.hour,
                timePicker.minute
            ))
        }
    }

    override fun onDestroyView() {
        _binding = null
        Log.d("TAG", "onDestroyView: ReminderFragment")
        super.onDestroyView()
    }
}