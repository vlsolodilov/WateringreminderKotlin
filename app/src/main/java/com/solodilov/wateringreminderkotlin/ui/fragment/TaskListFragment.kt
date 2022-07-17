package com.solodilov.wateringreminderkotlin.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.solodilov.wateringreminderkotlin.MainApplication
import com.solodilov.wateringreminderkotlin.databinding.FragmentTasksBinding
import com.solodilov.wateringreminderkotlin.presentation.TaskListViewModel
import com.solodilov.wateringreminderkotlin.ui.adapter.TaskAdapter
import javax.inject.Inject

class TaskListFragment : Fragment() {

    companion object {
        fun newInstance(): TaskListFragment = TaskListFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: TaskListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[TaskListViewModel::class.java]
    }

    private var _binding: FragmentTasksBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("FragmentTasksBinding is null")

    private var taskAdapter: TaskAdapter? = null

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
        _binding = FragmentTasksBinding.inflate(layoutInflater, container, false)

        Log.d("TAG", "onCreateView: TaskListFragment")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeViewModel()
    }

    private fun initViews() {
        taskAdapter = TaskAdapter { task ->
            viewModel.changeSelectionTask(task)
        }
        binding.taskList.adapter = taskAdapter

        binding.fabSelectAll.setOnClickListener { viewModel.selectAllTasks() }
        binding.fabExecute.setOnClickListener { viewModel.executeSelectedTask() }
    }

    private fun observeViewModel() {
        viewModel.taskList.observe(viewLifecycleOwner) { tasks ->
            taskAdapter?.submitList(tasks)
            Log.d("TAG", "observeViewModel: ")
        }
    }

    override fun onDestroyView() {
        taskAdapter = null
        _binding = null
        super.onDestroyView()
    }
}