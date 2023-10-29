package com.example.drivingschool.ui.fragments.enroll.selectInstructor

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.drivingschool.R
import com.example.drivingschool.databinding.FragmentSelectInstructorBinding
import com.example.drivingschool.ui.fragments.enroll.adapter.InstructorViewModel
import com.example.drivingschool.ui.fragments.enroll.adapter.SelectInstructorAdapter

class SelectInstructorFragment : Fragment() {

    private lateinit var binding: FragmentSelectInstructorBinding
    private lateinit var adapter: SelectInstructorAdapter
    private val viewModel: InstructorViewModel by viewModels()

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

        viewModel.instructors.observe(viewLifecycleOwner) { instructors ->
            adapter = SelectInstructorAdapter(instructors)
            binding.rvInstructorList.adapter = adapter
        }
    }
}