package com.example.drivingschool.ui.fragments.instructorLessonInfo

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.databinding.FragmentInstructorCurrentLessonBinding
import com.example.drivingschool.tools.showImage
import com.example.drivingschool.tools.showToast
import com.example.drivingschool.ui.fragments.Constants
import com.example.drivingschool.ui.fragments.main.mainExplore.MainExploreViewModel
import com.example.drivingschool.ui.fragments.noInternet.NetworkConnection
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class InstructorCurrentLessonFragment :
    BaseFragment<FragmentInstructorCurrentLessonBinding, MainExploreViewModel>(R.layout.fragment_instructor_current_lesson) {

    override val binding by viewBinding(FragmentInstructorCurrentLessonBinding::bind)
    override val viewModel: MainExploreViewModel by viewModels()
    private lateinit var networkConnection: NetworkConnection
    //private var id: String? = null

    override fun initialize() {
        networkConnection = NetworkConnection(requireContext())
        Log.e("ololololo", "initialize: ${arguments?.getString(Constants.INSTRUCTOR_MAIN_TO_CURRENT_KEY)}")
        networkConnection.observe(viewLifecycleOwner){
            viewModel.getCurrentById(arguments?.getString(Constants.INSTRUCTOR_MAIN_TO_CURRENT_KEY) ?: Constants.DEFAULT_KEY)
        }

        binding.ivProfileImage.showFullSizeImage()
    }

    override fun setupListeners() {
        binding.btnStartLesson.setOnClickListener {
            binding.btnStartLesson.text = "Завершить занятие"
        }
        binding.layoutSwipeRefresh.setOnRefreshListener {
            networkConnection.observe(viewLifecycleOwner){
                viewModel.getCurrentById(arguments?.getString(Constants.INSTRUCTOR_MAIN_TO_CURRENT_KEY) ?: Constants.DEFAULT_KEY)
            }
            binding.layoutSwipeRefresh.isRefreshing = false
        }
    }

    @SuppressLint("SetTextI18n")
    override fun setupSubscribes() {
        viewModel.currentDetailsState.collectStateFlow(
            empty = {
                showToast("Empty")
            },
            loading = {
                binding.progressBar.visibility = View.VISIBLE
                binding.clContainer.visibility = View.GONE
            },
            error ={
                showToast("Error $it")
            },
            success = {
                Log.e(
                    "ahahaha",
                    "InstructorCurrentLessonFragment Success: $it",
                )

                binding.apply {
                    progressBar.visibility = View.GONE
                    clContainer.visibility = View.VISIBLE

                    tvFullname.text =
                        "${it?.student?.surname} ${it?.student?.name} ${it?.student?.lastname}"
                    tvNumber.text = it?.student?.phone_number
                    tvBeginningTime.text = timeWithoutSeconds(it?.time)
                    calculateEndTime(it?.time, tvEndingTime)
                    binding.tvBeginningDate.text = formatDate(it?.date)
                    binding.tvEndingDate.text = formatDate(it?.date)
                    ivProfileImage.showImage(it?.student?.profile_photo?.small)
                }
            }
        )

    }

}