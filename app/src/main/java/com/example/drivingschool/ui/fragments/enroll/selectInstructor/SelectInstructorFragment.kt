package com.example.drivingschool.ui.fragments.enroll.selectInstructor

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.drivingschool.R
import com.example.drivingschool.data.models.Date
import com.example.drivingschool.databinding.FragmentSelectInstructorBinding
import com.example.drivingschool.tools.UiState
import com.example.drivingschool.ui.fragments.enroll.EnrollViewModel
import com.example.drivingschool.ui.fragments.enroll.adapter.SelectInstructorAdapter
import com.example.drivingschool.ui.fragments.noInternet.NetworkConnection
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SelectInstructorFragment : Fragment() {
//Почему этот класс не унаследован BaseFragment
    private lateinit var binding: FragmentSelectInstructorBinding
    private lateinit var adapter: SelectInstructorAdapter
    private val viewModel: EnrollViewModel by viewModels()
    private lateinit var networkConnection: NetworkConnection

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectInstructorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = SelectInstructorAdapter(this::onClick)
        networkConnection = NetworkConnection(requireContext())

        networkConnection.observe(viewLifecycleOwner){
            if(it) getInstructorsList()
        }

        binding.layoutSwipeRefresh.setOnRefreshListener {
            networkConnection.observe(viewLifecycleOwner){
                if(it) getInstructorsList()
            }
            binding.layoutSwipeRefresh.isRefreshing = false
        }
    }

    private fun getInstructorsList() {
        lifecycleScope.launch {
            viewModel.getInstructors()
            viewModel.instructors.observe(requireActivity()) { uiState ->
                when (uiState) {
                    is UiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.clContainer.visibility = View.GONE
                    }

                    is UiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.clContainer.visibility = View.VISIBLE
                        val instructors = uiState.data
                        adapter.updateList(instructors ?: emptyList())
                        binding.rvInstructorList.adapter = adapter

                        Log.d("madimadi", "instructors list in fragment: ${uiState.data}")
                    }

                    is UiState.Error -> {
                        Toast.makeText(
                            requireContext(),
                            "state error: ${uiState.msg}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is UiState.Empty -> {
                        Toast.makeText(requireContext(), "state is empty", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

            }
        }
    }

    private fun onClick(workWindows: ArrayList<Date>, name : String) {
        val bundle = Bundle()
        bundle.putString(FULL_NAME, name)
        bundle.putSerializable(WORK_WINDOWS, workWindows)
        findNavController().navigate(R.id.selectDateTimeFragment, bundle)
    }

    companion object {
        const val WORK_WINDOWS = "work_windows"
        const val FULL_NAME = "full_name_key"
    }
}