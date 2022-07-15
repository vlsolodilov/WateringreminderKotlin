package com.solodilov.wateringreminderkotlin.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.solodilov.wateringreminderkotlin.MainApplication
import com.solodilov.wateringreminderkotlin.R
import com.solodilov.wateringreminderkotlin.databinding.FragmentPlantBinding
import com.solodilov.wateringreminderkotlin.domain.entity.Plant
import com.solodilov.wateringreminderkotlin.presentation.PlantViewModel
import com.solodilov.wateringreminderkotlin.ui.DateTimeConverter
import com.solodilov.wateringreminderkotlin.ui.adapter.ReminderAdapter
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class PlantFragment : Fragment() {

    companion object {
        private const val LAUNCH_MODE_KEY = "launch mode"
        private const val LAUNCH_MODE_EDIT = "launch mode edit"
        private const val LAUNCH_MODE_ADD = "launch mode add"
        private const val LAUNCH_MODE_UNKNOWN = "UNKNOWN"
        private const val EDITED_PLANT_ID = "edited plant id"

        fun newInstance(): PlantFragment {
            return PlantFragment().apply {
                arguments = Bundle().apply {
                    putString(LAUNCH_MODE_KEY, LAUNCH_MODE_ADD)
                }
            }
        }

        fun newInstance(id: Long): PlantFragment {
            return PlantFragment().apply {
                arguments = Bundle().apply {
                    putString(LAUNCH_MODE_KEY, LAUNCH_MODE_EDIT)
                    putLong(EDITED_PLANT_ID, id)
                }
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: PlantViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PlantViewModel::class.java]
    }

    private var _binding: FragmentPlantBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("FragmentPlantBinding is null")

    private var reminderAdapter: ReminderAdapter? = null
    private var screenMode: String = LAUNCH_MODE_UNKNOWN
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
                if (!requireArguments().containsKey(EDITED_PLANT_ID)) {
                    throw RuntimeException("Param EDITED_PLANT_ID is absent")
                }
                plantId = requireArguments().getLong(EDITED_PLANT_ID, Plant.UNDEFINED_ID)
                if (plantId == Plant.UNDEFINED_ID) {
                    throw RuntimeException("Param EDITED_PLANT_ID is absent")
                }
            }
            LAUNCH_MODE_ADD -> {
                plantId = Plant.TEMP_ID
            }
            else -> throw RuntimeException("Unknown screen mode")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPlantBinding.inflate(layoutInflater, container, false)
        Log.d("TAG", "onCreateView: PlantFragment")
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
        viewModel.getSavedPlant(plantId)
        binding.savePlant.setOnClickListener {
            viewModel.updatePlant(
                id = plantId,
                name = binding.plantName.text.toString(),
                description = binding.plantDescription.text.toString(),
                plantingDate = binding.plantCreationDate.text.toString(),
            )
        }

        binding.deletePlant.setOnClickListener {
            viewModel.deletePlant(plantId)
        }
    }

    private fun launchAddMode() {
        binding.plantCreationDate.setText(DateTimeConverter.getFormattedDate(Date()))
        binding.savePlant.setOnClickListener {
            viewModel.createPlant(
                name = binding.plantName.text.toString(),
                description = binding.plantDescription.text.toString(),
                plantingDate = binding.plantCreationDate.text.toString(),
            )
        }
        binding.deletePlant.isVisible = false
    }

    private fun initViews() {
        binding.plantCreationDate.setOnClickListener { selectCreationDate() }
        binding.addNewReminder.setOnClickListener { createReminder() }
        reminderAdapter = ReminderAdapter { reminder ->
            updateReminder(reminderId = reminder.id)
        }
        binding.reminderList.adapter = reminderAdapter
    }

    private fun observeViewModel() {
        viewModel.loading.observe(viewLifecycleOwner, ::toggleProgress)
        viewModel.savedPlant.observe(viewLifecycleOwner) { savedPlant ->
            showSavedPlant(savedPlant)
        }
        viewModel.savePlantSuccessEvent.observe(viewLifecycleOwner) { startPlantListFragment() }
        viewModel.deletePlantSuccessEvent.observe(viewLifecycleOwner) { startPlantListFragment() }
        viewModel.plantErrorEvent.observe(viewLifecycleOwner) { showSavePlantFailed() }
        viewModel.textFieldErrorEvent.observe(viewLifecycleOwner) { showTextFieldsFailed() }

        viewModel.getReminderList(plantId).observe(viewLifecycleOwner) { reminders ->
            reminderAdapter?.reminderList = reminders
        }
    }

    private fun toggleProgress(visible: Boolean) {
        binding.plantLoading.isVisible = visible
        binding.savePlant.isEnabled = !visible
        binding.deletePlant.isEnabled = !visible
    }

    private fun showSavedPlant(plant: Plant) {
        binding.plantName.setText(plant.name)
        binding.plantDescription.setText(plant.description)
        binding.plantCreationDate.setText(DateTimeConverter.getFormattedDate(plant.plantingDate))
    }

    private fun startPlantListFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, ContainerFragment.newInstance())
            .commit()
    }

    private fun showSavePlantFailed() {
        Snackbar.make(binding.root, R.string.error, Snackbar.LENGTH_SHORT).show()
    }

    private fun showTextFieldsFailed() {
        Snackbar.make(binding.root, R.string.emptyName, Snackbar.LENGTH_SHORT).show()
    }

    private fun selectCreationDate() {
        val datePicker = MaterialDatePicker.Builder
            .datePicker()
            .build()
        datePicker.show(parentFragmentManager, "DatePicker")
        datePicker.addOnPositiveButtonClickListener {
            binding.plantCreationDate.setText(DateTimeConverter.getFormattedDate(Date(it)))
        }
    }

    private fun createReminder() {
        startReminderFragment(ReminderFragment.newInstance(plantId))
    }

    private fun updateReminder(reminderId: Long) {
        startReminderFragment(ReminderFragment.newInstance(plantId, reminderId))
    }

    private fun startReminderFragment(fragment: ReminderFragment) {
        parentFragmentManager.beginTransaction()
            .add(R.id.mainContainer, fragment)
            .addToBackStack(ReminderFragment::class.java.name)
            .commit()
    }

    override fun onDestroyView() {
        reminderAdapter = null
        _binding = null
        super.onDestroyView()
    }
}