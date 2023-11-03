package com.example.drivingschool.ui.fragments.previousDetails

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.databinding.FragmentPreviousLessonDetailsBinding
import com.example.drivingschool.tools.UiState
import com.example.drivingschool.tools.showToast
import com.example.drivingschool.tools.viewVisibility
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PreviousLessonDetailsFragment :
    BaseFragment<FragmentPreviousLessonDetailsBinding, PreviousLessonDetailsViewModel>(R.layout.fragment_previous_lesson_details) {

    override val binding by viewBinding(FragmentPreviousLessonDetailsBinding::bind)
    override val viewModel: PreviousLessonDetailsViewModel by viewModels()

    override fun initialize() {
        Log.e("ololololo", "initialize: ${arguments?.getString("key")}")
        viewModel.getDetails(arguments?.getString("key") ?: "1")
    }

    override fun setupSubscribes() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.detailsState.collect {
                    when (it) {
                        is UiState.Empty -> {
                            showToast("UiState.Empty")
                            binding.detailsProgressBar.viewVisibility(false)
                        }

                        is UiState.Error -> {
                            showToast(it.msg.toString())
                            binding.detailsProgressBar.viewVisibility(false)
                            showToast("UiState.Error")
                        }

                        is UiState.Loading -> {
                            binding.apply {
                                detailsProgressBar.viewVisibility(true)
                            }
                        }

                        is UiState.Success -> {
                            binding.detailsProgressBar.viewVisibility(false)
                            Log.e("ololo", "setupSubscribes: $it")
                            showToast("UiState.Success")
                            binding.apply {
                                tvUserName.text =
                                    "${it.data?.instructor?.surname} ${it.data?.instructor?.name}"
                                tvUserNumber.text = it.data?.instructor?.phone_number
                                tvPreviousStartDate.text = it.data?.date
                                tvScheduleEndDate.text = it.data?.date
                                tvPreviousStartTime.text = it.data?.time
                                tvPreviousEndTime.text = it.data?.time

                                Picasso.get()
                                    .load(it.data?.instructor?.profile_photo)
                                    .placeholder(R.drawable.ic_default_photo)
                                    .into(circleImageView)
                            }
                        }
                    }
                }
            }
        }
    }
}