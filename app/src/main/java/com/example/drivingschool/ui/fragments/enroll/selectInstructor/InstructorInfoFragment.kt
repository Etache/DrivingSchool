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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.drivingschool.R
import com.example.drivingschool.databinding.FragmentInstructorInfoBinding
import com.example.drivingschool.tools.UiState
import com.example.drivingschool.ui.fragments.enroll.EnrollViewModel
import com.example.drivingschool.ui.fragments.enroll.adapter.InstructorCommentAdapter
import com.example.drivingschool.ui.fragments.enroll.adapter.SelectInstructorAdapter
import com.example.drivingschool.ui.fragments.enroll.commentsList
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InstructorInfoFragment : Fragment() {

    private lateinit var binding: FragmentInstructorInfoBinding
    private lateinit var adapter: InstructorCommentAdapter
    private val viewModel: EnrollViewModel by viewModels()
    private var id: Int ?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInstructorInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = InstructorCommentAdapter(commentsList)
        binding.rvInstructorProfileComments.adapter = adapter
        binding.rvInstructorProfileComments.layoutManager = LinearLayoutManager(context)
        adapter.notifyDataSetChanged()

        id = arguments?.getInt(SelectInstructorAdapter.ID_KEY)
        Log.d("madimadi", "instructor id in fragment: ${id}")
        getInstructorProfile()
    }


    private fun getInstructorProfile() {
        viewModel.getInstructorById(id = id!!)
        lifecycleScope.launch {
            viewModel.instructorDetails.observe(requireActivity()) { state ->
                when (state) {
                    is UiState.Loading -> {
                        Toast.makeText(requireContext(), "Wait it's loading..", Toast.LENGTH_SHORT).show()
                    }
                    is UiState.Success -> {
                        binding.tvName.text = state.data?.name
                        binding.tvSurname.text = state.data?.surname
                        binding.tvExpienceNum.text = state.data?.experience.toString()
                        binding.tvNumber.text = state.data?.phoneNumber
                        binding.tvCarName.text = state.data?.car
                        Log.d("madimadi", "getInstructorDetails in fragment: ${state.data}")

                        Glide
                            .with(binding.ivProfileImage)
                            .load(state.data?.profilePhoto)
                            .circleCrop()
                            .placeholder(R.drawable.default_pfp)
                            .into(binding.ivProfileImage)
                    }

                    is UiState.Empty -> {
                        Toast.makeText(requireContext(), "Empty", Toast.LENGTH_SHORT).show()
                    }

                    is UiState.Error -> {
                        Toast.makeText(requireContext(), "Error: ${state.msg}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}