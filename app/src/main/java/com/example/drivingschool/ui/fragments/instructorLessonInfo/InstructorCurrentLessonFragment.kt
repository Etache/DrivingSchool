package com.example.drivingschool.ui.fragments.instructorLessonInfo

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
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
import com.example.drivingschool.tools.showToast
import com.example.drivingschool.ui.fragments.BundleKeys
import com.example.drivingschool.ui.fragments.instructorLessonInfo.viewModels.StartFinishLessonViewModel
import com.example.drivingschool.ui.fragments.main.mainExplore.MainExploreViewModel
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
    private val changeLessonStatusViewModel: StartFinishLessonViewModel by viewModels()

    private lateinit var countDownTimer: CountDownTimer
    private lateinit var lessonId: String


    override fun initialize() {
        lessonId = arguments?.getString(BundleKeys.CURRENT_KEY) ?: "1"
        viewModel.getCurrentById(lessonId)
        changeLessonStatusViewModel.startLesson(lessonId)
        changeLessonStatusViewModel.finishLesson(lessonId)

        binding.btnStartLesson.setOnClickListener {
            startLesson()
        }
        binding.btnEndLesson.setOnClickListener {
            finishLesson()
        }
        showImage()
    }

    @SuppressLint("SetTextI18n")
    override fun setupSubscribes() {
        //viewModel.getCurrentById(id = id!!)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
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
                            Toast.makeText(
                                requireContext(),
                                "Error: ${state.msg}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        is UiState.Success -> {
                            binding.apply {
                                progressBar.visibility = View.GONE
                                clContainer.visibility = View.VISIBLE

                                tvFullname.text =
                                    "${state.data?.student?.surname} ${state.data?.student?.name} ${state.data?.student?.lastname}"
                                tvNumber.text = state.data?.student?.phone_number

                                Picasso.get()
                                    .load(state.data?.student?.profile_photo?.small)
                                    .placeholder(R.drawable.ic_default_photo)
                                    .into(ivProfileImage)

                                tvBeginningTime.text = timeWithoutSeconds(state.data?.time)
                                calculateEndTime(state.data?.time)

                                binding.tvBeginningDate.text = formatDate(state.data?.date)
                                binding.tvEndingDate.text = formatDate(state.data?.date)

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
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = inputFormat.parse(inputDate) ?: return ""

        val outputFormat = SimpleDateFormat("d MMMM", Locale("ru"))
        return outputFormat.format(date).replaceFirstChar { it.uppercase() }
    }

    private fun calculateEndTime(inputTime: String?) {
        val timeFormat = SimpleDateFormat("HH:mm:ss")

        try {
            val date = inputTime?.let { timeFormat.parse(it) }
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.HOUR_OF_DAY, 1)
            val outputTimeFormat = SimpleDateFormat("HH:mm:ss")
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
        changeLessonStatusViewModel.startLessonResult.observe(viewLifecycleOwner) {
            if (it != null && it?.startSuccess != null) {
                showStartFinishAlert("Ваше занятие началось")
                binding.btnStartLesson.visibility = View.GONE
                binding.btnEndLesson.visibility = View.VISIBLE
                startTimer()
            } else {
                Log.e("fff", "startLesson: ${it?.startError}, ${it?.startSuccess}")
                Toast.makeText(requireContext(), "start lesson error: $it", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun finishLesson() {
        changeLessonStatusViewModel.finishLessonResult.observe(viewLifecycleOwner) {
            if (it != null && it?.finishSuccess != null) {
                showStartFinishAlert("Ваше занятие закончилось")
                binding.btnEndLesson.visibility = View.GONE
                binding.btnWriteFeedback.visibility = View.VISIBLE
            } else {
                Toast.makeText(requireContext(), "finish lesson error: $it", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(3000000, 60000) {
            override fun onTick(p0: Long) {}

            override fun onFinish() {
                binding.btnEndLesson.isEnabled = true
            }
        }.start()
        binding.btnStartLesson.isEnabled = false
    }

    private fun showStartFinishAlert(alertMessage: String) {
        AlertDialog.Builder(requireContext())
            .setMessage(alertMessage)
            .setPositiveButton(
                "Ок",
                DialogInterface.OnClickListener { dialog, _ ->
                    dialog.cancel()
                }
            )
    }
}