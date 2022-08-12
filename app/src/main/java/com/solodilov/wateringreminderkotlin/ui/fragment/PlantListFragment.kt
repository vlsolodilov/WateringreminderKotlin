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
import com.solodilov.wateringreminderkotlin.R
import com.solodilov.wateringreminderkotlin.databinding.FragmentPlantsBinding
import com.solodilov.wateringreminderkotlin.presentation.PlantListViewModel
import com.solodilov.wateringreminderkotlin.ui.adapter.PlantAdapter
import javax.inject.Inject

class PlantListFragment : Fragment() {

    companion object {
        fun newInstance(): PlantListFragment = PlantListFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: PlantListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PlantListViewModel::class.java]
    }

    private var _binding: FragmentPlantsBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("FragmentPlantsBinding is null")


    private var plantAdapter: PlantAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as MainApplication).component.inject(this)
        Log.d("TAG", "onAttach: PlantListFragment")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPlantsBinding.inflate(layoutInflater, container, false)
        Log.d("TAG", "onCreateView: PlantListFragment")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeViewModel()
    }

    private fun initViews() {
        plantAdapter = PlantAdapter { plant ->
            startPlantFragment(plantId = plant.id)
        }

        binding.addPlant.setOnClickListener { startPlantFragment() }
        binding.plantList.adapter = plantAdapter
    }

    private fun observeViewModel() {
        viewModel.plantList.observe(viewLifecycleOwner) { plants ->
            plantAdapter?.plantList = plants
        }
    }

    private fun startPlantFragment() {
        parentFragmentManager.beginTransaction()
            .add(R.id.mainContainer, PlantFragment.newInstance())
            .addToBackStack(PlantFragment::class.java.name)
            .commit()
    }

    private fun startPlantFragment(plantId: Long) {
        parentFragmentManager.beginTransaction()
            .add(R.id.mainContainer, PlantFragment.newInstance(plantId))
            .addToBackStack(PlantFragment::class.java.name)
            .commit()
    }

        override fun onDestroyView() {
        plantAdapter = null
        _binding = null
            Log.d("TAG", "onDestroyView: PlantListFragment")
        super.onDestroyView()
    }
}