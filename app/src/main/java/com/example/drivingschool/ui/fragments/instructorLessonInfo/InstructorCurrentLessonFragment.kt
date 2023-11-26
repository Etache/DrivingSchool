package com.example.drivingschool.ui.fragments.instructorLessonInfo

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import androidx.lifecycle.Observer
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.databinding.FragmentInstructorCurrentLessonBinding
import com.example.drivingschool.tools.UiState
import com.example.drivingschool.tools.showOnlyPositiveAlert
import com.example.drivingschool.tools.showToast
import com.example.drivingschool.ui.fragments.Constants
import com.example.drivingschool.ui.fragments.instructorLessonInfo.viewModels.ChangeLessonStatusViewModel
import com.example.drivingschool.ui.fragments.main.mainExplore.MainExploreViewModel
import com.example.drivingschool.ui.fragments.noInternet.NetworkConnection
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class InstructorCurrentLessonFragment :
    BaseFragment<FragmentInstructorCurrentLessonBinding, MainExploreViewModel>(R.layout.fragment_instructor_current_lesson) {

    override val binding by viewBinding(FragmentInstructorCurrentLessonBinding::bind)
    override val viewModel: MainExploreViewModel by viewModels()
    private val changeLessonStatusViewModel: ChangeLessonStatusViewModel by viewModels()

    private lateinit var lessonId: String
    private lateinit var lessonDateTime: String

    private lateinit var networkConnection: NetworkConnection

    override fun initialize() {
        networkConnection = NetworkConnection(requireContext())

        lessonId = arguments?.getString(Constants.CURRENT_KEY) ?: "1"
        viewModel.getCurrentById(lessonId)

        showImage()
    }

    override fun setupListeners() {
        binding.layoutSwipeRefresh.setOnRefreshListener {
            networkConnection.observe(viewLifecycleOwner){
                viewModel.getCurrentById(arguments?.getString("key") ?: "1")
            }
            binding.layoutSwipeRefresh.isRefreshing = false
        }

        binding.btnStartLesson.setOnClickListener {
            changeLessonStatusViewModel.startLesson(lessonId)
            startLesson()
        }
        binding.btnEndLesson.setOnClickListener {
            changeLessonStatusViewModel.finishLesson(lessonId)
            finishLesson()
        }
        binding.btnNotVisit.setOnClickListener {
            changeLessonStatusViewModel.studentAbsent(lessonId)
            studentAbsent()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun setupSubscribes() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentDetailsState.collect { state ->
                    when (state) {
                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.clContainer.visibility = View.GONE
                        }

                        is UiState.Empty -> {
                            Toast.makeText(requireContext(),
                                getString(R.string.empty), Toast.LENGTH_SHORT).show()
                        }

                        is UiState.Error -> {
                            showToast("error")
                        }

                        is UiState.Success -> {
                            binding.apply {
                                progressBar.visibility = View.GONE
                                clContainer.visibility = View.VISIBLE

                                tvFullname.text =
                                    "${state.data?.student?.surname} ${state.data?.student?.name} ${state.data?.student?.lastname}"
                                tvNumber.text = state.data?.student?.phoneNumber

                                Picasso.get()
                                    .load(state.data?.student?.profilePhoto?.big)
                                    .placeholder(R.drawable.ic_default_photo)
                                    .into(ivProfileImage)

                                tvBeginningTime.text = timeWithoutSeconds(state.data?.time)
                                calculateEndTime(state.data?.time)

                                binding.tvBeginningDate.text = formatDate(state.data?.date)
                                binding.tvEndingDate.text = formatDate(state.data?.date)

                                lessonDateTime = "${state.data?.date} ${timeWithoutSeconds(state.data?.time)}"

                                if (state.data?.status == getString(R.string.active)) {
                                    btnStartLesson.visibility = View.GONE
                                    btnEndLesson.visibility = View.VISIBLE
                                    btnNotVisit.visibility = View.VISIBLE
                                } else {
                                    btnEndLesson.visibility = View.GONE
                                    btnStartLesson.visibility = View.VISIBLE
                                    btnNotVisit.visibility = View.GONE
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun timeWithoutSeconds(inputTime: String?): String {
        val timeParts = inputTime?.split(":")
        return "${timeParts?.get(0)}:${timeParts?.get(1)}"
    }

    private fun formatDate(inputDate: String?): String {
        val inputFormat = SimpleDateFormat(getString(R.string.yyyy_mm_dd), Locale.getDefault())
        val date = inputFormat.parse(inputDate) ?: return ""

        val outputFormat = SimpleDateFormat(getString(R.string.d_mmmm), Locale(getString(R.string.ru)))
        return outputFormat.format(date).replaceFirstChar { it.uppercase() }
    }

    private fun calculateEndTime(inputTime: String?) {
        val timeFormat = SimpleDateFormat(getString(R.string.hh_mm_ss))

        try {
            val date = inputTime?.let { timeFormat.parse(it) }
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.HOUR_OF_DAY, 1)
            val outputTimeFormat = SimpleDateFormat(getString(R.string.hh_mm_ss))
            val outputTime = outputTimeFormat.format(calendar.time)
            val timeParts = outputTime.split(":")
            binding.tvEndingTime.text = "${timeParts[0]}:${timeParts[1]}"
        } catch (e: Exception) {
            showToast(e.message.toString())
        }
    }

    private fun showImage() {
        binding.ivProfileImage.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.show_photo_profile)
            val image = dialog.findViewById<ImageView>(R.id.image)

            if (binding.ivProfileImage.drawable is BitmapDrawable) {
                image.setImageBitmap((binding.ivProfileImage.drawable as BitmapDrawable).bitmap)
            } else if (binding.ivProfileImage.drawable is VectorDrawable) {
                image.setImageDrawable(binding.ivProfileImage.drawable)
            } else {
                image.setImageResource(R.drawable.ic_default_photo)
            }

            dialog.window?.setBackgroundDrawableResource(R.drawable.ic_default_photo)
            dialog.show()
        }
    }

    private fun startLesson() {
        //проверка на начало нет
        changeLessonStatusViewModel.startLessonResult.observe(viewLifecycleOwner, Observer {
            if (it?.status == getString(R.string.success)) {
                showOnlyPositiveAlert(getString(R.string.your_lesson_started))

                binding.btnStartLesson.visibility = View.GONE
                binding.btnEndLesson.visibility = View.VISIBLE
                binding.btnNotVisit.visibility = View.VISIBLE
            } else if (it?.status == getString(R.string.error)) {
                showOnlyPositiveAlert(getString(R.string.lesson_start_is_not_possible_due_to_time_constraints))
            } else {
                Log.e("ahahaha", "startLesson: ${it?.status}")
            }
        })
    }

    private fun finishLesson() {
        changeLessonStatusViewModel.finishLessonResult.observe(viewLifecycleOwner) {
            if (it?.status == getString(R.string.success)) {
                showOnlyPositiveAlert(getString(R.string.your_lesson_ended))
                binding.btnEndLesson.visibility = View.GONE
                binding.btnNotVisit.visibility = View.GONE
            } else {
                Toast.makeText(requireContext(), getString(R.string.error), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun studentAbsent() {
        changeLessonStatusViewModel.studentAbsentResult.observe(viewLifecycleOwner) {
            if (it?.status == getString(R.string.success)) {
                binding.btnNotVisit.visibility = View.GONE
                binding.btnEndLesson.visibility = View.GONE
            } else {
                Toast.makeText(requireContext(), getString(R.string.error), Toast.LENGTH_SHORT).show()
            }
        }
    }
}