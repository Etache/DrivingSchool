package com.example.drivingschool.ui.fragments.instructorMain.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.drivingschool.R
import com.example.drivingschool.databinding.FragmentInstructorCurrentLessonBinding
import com.example.drivingschool.tools.UiState
import com.example.drivingschool.ui.fragments.instructorMain.adapter.InstructorLessonAdapter
import com.example.drivingschool.ui.fragments.main.mainExplore.MainExploreViewModel
import kotlinx.coroutines.launch
import java.util.*

class InstructorCurrentLessonFragment : Fragment() {

    private lateinit var binding: FragmentInstructorCurrentLessonBinding
    private val viewModel: MainExploreViewModel by viewModels()
    private var id: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInstructorCurrentLessonBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        id = arguments?.getInt(InstructorLessonAdapter.ID_KEY)

        getLessonsInfo()
    }

    @SuppressLint("SetTextI18n")
    private fun getLessonsInfo() {
        viewModel.getCurrentById(id = id!!)
        lifecycleScope.launch {
            viewModel.currentDetailsState.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.clContainer.visibility = View.GONE

                    }

                    is UiState.Empty -> {
                        Toast.makeText(requireContext(), "Empty", Toast.LENGTH_SHORT).show()
                    }

                    is UiState.Error -> {
                        Toast.makeText(requireContext(), "Error: ${state.msg}", Toast.LENGTH_SHORT)
                            .show()
                    }

                    is UiState.Success -> {
                        binding.apply {
                            tvFullname.text =
                                "${state.data?.student?.surname} ${state.data?.student?.name} ${state.data?.student?.lastname}"
                            tvNumber.text = state.data?.student?.phone_number

                            Glide
                                .with(requireContext())
                                .load(state.data?.student?.profile_photo)
                                .placeholder(R.drawable.ic_default_photo)
                                .into(ivProfileImage)

                            tvBeginningTime.text = state.data?.time
                            tvEndingTime.text = (state.data?.time?.toInt()?.plus(1)).toString()

                            val originalDate = state.data?.date
                            val parts = (originalDate?.split("-"))!!
                            val day = parts[0].toInt()
                            val month = parts[1].toInt()
                            val monthString = when (month) {
                                1 -> "января"
                                2 -> "февраля"
                                3 -> "марта"
                                4 -> "апреля"
                                5 -> "мая"
                                6 -> "июня"
                                7 -> "июля"
                                8 -> "августа"
                                9 -> "сентября"
                                10 -> "октября"
                                11 -> "ноября"
                                12 -> "декабря"
                                else -> throw IllegalArgumentException("Некорректный месяц: $month") }
                            tvBeginningDate.text = "${day} ${monthString}"
                        }

                    }
                }
            }
        }
    }
}