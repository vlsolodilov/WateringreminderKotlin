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
import com.google.android.material.snackbar.Snackbar
import com.solodilov.wateringreminderkotlin.MainApplication
import com.solodilov.wateringreminderkotlin.R
import com.solodilov.wateringreminderkotlin.databinding.FragmentCalendarBinding
import com.solodilov.wateringreminderkotlin.presentation.CalendarViewModel
import com.solodilov.wateringreminderkotlin.ui.adapter.CalendarAdapter
import javax.inject.Inject

class CalendarFragment : Fragment() {

    companion object {
        fun newInstance(): CalendarFragment = CalendarFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: CalendarViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[CalendarViewModel::class.java]
    }

    private var _binding: FragmentCalendarBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("FragmentCalendarBinding is null")

    private var calendarAdapter: CalendarAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as MainApplication).component.inject(this)
        Log.d("TAG", "onAttach: TaskListFragment")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCalendarBinding.inflate(layoutInflater, container, false)

        Log.d("TAG", "onCreateView: CalendarFragment")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeViewModel()
    }

    private fun initViews() {
        calendarAdapter = CalendarAdapter()
        binding.calendarList.adapter = calendarAdapter
    }

    private fun observeViewModel() {
        viewModel.loading.observe(viewLifecycleOwner, ::toggleProgress)
        viewModel.calendarItemList.observe(viewLifecycleOwner) { calendarItems ->
            calendarAdapter?.calendarItemList = calendarItems
            toggleShowCalendarList(calendarItems.isNotEmpty())
            Log.d("TAG", "observeViewModel: Calendar")
        }
        viewModel.calendarErrorEvent.observe(viewLifecycleOwner) { showError() }
    }

    private fun toggleProgress(visible: Boolean) {
        binding.calendarLoading.isVisible = visible
    }

    private fun toggleShowCalendarList(visible: Boolean) {
        binding.calendarList.isVisible = visible
        binding.emptyCalendar.isVisible = !visible
    }

    private fun showError() {
        Snackbar.make(binding.root, R.string.error, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        calendarAdapter = null
        _binding = null
        Log.d("TAG", "onDestroyView: CalendarFragment")
        super.onDestroyView()
    }
}