package com.solodilov.wateringreminderkotlin.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.solodilov.wateringreminderkotlin.MainApplication
import com.solodilov.wateringreminderkotlin.R
import com.solodilov.wateringreminderkotlin.databinding.FragmentContainerBinding
import com.solodilov.wateringreminderkotlin.databinding.FragmentTasksBinding

class TaskListFragment : Fragment() {

    companion object {
        fun newInstance(): TaskListFragment = TaskListFragment()
    }

    private var _binding: FragmentTasksBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("FragmentTasksBinding is null")

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


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}