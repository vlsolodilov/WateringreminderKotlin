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

class ContainerFragment : Fragment() {

    companion object {
        fun newInstance(): ContainerFragment = ContainerFragment()
    }

    private var _binding: FragmentContainerBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("FragmentContainerBinding is null")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as MainApplication).component.inject(this)
        Log.d("TAG", "onAttach: ContainerFragment")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentContainerBinding.inflate(layoutInflater, container, false)

        if (savedInstanceState == null){
            parentFragmentManager.beginTransaction()
                //.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container, TaskListFragment.newInstance())
                .commit()
        }

        initViews()
        Log.d("TAG", "onCreateView: ContainerFragment item: ${binding.bottomNavigation.selectedItemId}" +
                " ${R.id.menuTasks}")
        return binding.root
    }

    private fun initViews() {

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menuTasks -> {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.container, TaskListFragment.newInstance())
                        .commit()
                    true
                }
                R.id.menuCalendar -> {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.container, CalendarFragment.newInstance())
                        .commit()
                    true
                }
                R.id.menuPlants -> {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.container, PlantListFragment.newInstance())
                        .commit()
                    true
                }
                else -> false
            }
        }

    }

    fun startPlantList() {
        binding.bottomNavigation.selectedItemId = R.id.menuPlants
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}