package com.example.drivingschool.ui.fragments.enroll.selectInstructor

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.drivingschool.databinding.FragmentSelectInstructorBinding
import com.example.drivingschool.tools.UiState
import com.example.drivingschool.ui.fragments.enroll.EnrollViewModel
import com.example.drivingschool.ui.fragments.enroll.adapter.SelectInstructorAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SelectInstructorFragment : Fragment() {

    private lateinit var binding: FragmentSelectInstructorBinding
    private lateinit var adapter: SelectInstructorAdapter
    private val viewModel: EnrollViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectInstructorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.getInstructors()
            viewModel.instructors.observe(requireActivity()) { uiState ->
                when(uiState) {
                    is UiState.Success -> {
                        val instructors = uiState.data
                        adapter = SelectInstructorAdapter(instructors ?: emptyList())
                        binding.rvInstructorList.adapter = adapter
                        Log.d("madimadi", "instructors list in fragment: ${uiState.data}")
                    }
                    is UiState.Error -> {
                        Toast.makeText(requireContext(), "state error: ${uiState.msg}", Toast.LENGTH_SHORT).show()
                    }
                    is UiState.Loading -> {
                        Toast.makeText(requireContext(), "wait...", Toast.LENGTH_LONG).show()
                    }
                    is UiState.Empty -> {
                        Toast.makeText(requireContext(), "state is empty", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }

    }
}