package com.example.drivingschool.ui.fragments.lessonInfo.currentDetails

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.util.Log
import android.view.Window
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.databinding.FragmentCurrentLessonDetailBinding
import com.example.drivingschool.tools.UiState
import com.example.drivingschool.tools.showToast
import com.example.drivingschool.tools.viewVisibility
import com.example.drivingschool.ui.fragments.BundleKeys
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class CurrentLessonDetailsFragment :
    BaseFragment<FragmentCurrentLessonDetailBinding, CurrentLessonDetailsViewModel>(R.layout.fragment_current_lesson_detail) {

    override val binding by viewBinding(FragmentCurrentLessonDetailBinding::bind)
    override val viewModel: CurrentLessonDetailsViewModel by viewModels()
    private var inputDateTimeString: String? = null

    override fun initialize() {
        Log.e("ololololo", "initialize: ${arguments?.getString(BundleKeys.MAIN_TO_CURRENT_KEY)}")
        viewModel.getDetails(arguments?.getString(BundleKeys.MAIN_TO_CURRENT_KEY) ?: "1")
        showImage()
    }

    override fun setupListeners() {
        binding.btnCancelLesson.setOnClickListener {
            dateValidityCheck()
        }
    }

    override fun setupSubscribes() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.detailsState.collect {
                    when (it) {
                        is UiState.Empty -> {
                            showToast("UiState.Empty")
                            binding.detailsProgressBar.viewVisibility(false)
                            binding.mainContainer.viewVisibility(true)
                        }

                        is UiState.Error -> {
                            showToast(it.msg.toString())
                            binding.detailsProgressBar.viewVisibility(false)
                            binding.mainContainer.viewVisibility(true)
                            showToast("UiState.Error")
                        }

                        is UiState.Loading -> {
                            binding.apply {
                                detailsProgressBar.viewVisibility(true)
                                mainContainer.viewVisibility(false)
                            }
                        }

                        is UiState.Success -> {
                            inputDateTimeString = "${it.data?.date}, ${it.data?.time}"
                            Log.e("ololo", "inputDateTimeString: $inputDateTimeString")
                            Log.e("ololo", "setupSubscribes: $it")
                            //showToast("UiState.Success")
                            binding.apply {
                                detailsProgressBar.viewVisibility(false)
                                mainContainer.viewVisibility(true)
                                val last = it.data?.instructor?.lastname ?: ""
                                tvUserName.text = getString(
                                    R.string.person_full_name,
                                    it.data?.instructor?.surname,
                                    it.data?.instructor?.name,
                                    last
                                )
                                tvUserNumber.text = it.data?.instructor?.phone_number
                                tvStartDate.text = formatDate(it.data?.date)
                                tvEndDate.text = formatDate(it.data?.date)
                                tvStartTime.text = timeWithoutSeconds(it.data?.time)
                                calculateEndTime(it.data?.time)

                                val httpsImageUrl = it.data?.instructor?.profile_photo?.replace(
                                    "http://",
                                    "https://"
                                )
                                Picasso.get()
                                    .load(httpsImageUrl)
                                    .placeholder(R.drawable.ic_default_photo)
                                    .into(circleImageView)
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

    private fun dateValidityCheck() {
        if (inputDateTimeString != null) {
            if (isLessThan4Hours(inputDateTimeString!!)) {
                showCancelAlert()
            } else {
                showAlert()
            }
        } else {
            showToast("Время начала урока неизвестно")
        }

    }

    private fun showCancelAlert() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.cancel_lesson_text))
            .setCancelable(true)
            .setPositiveButton(
                getString(R.string.confirm),
                DialogInterface.OnClickListener { dialogInterface, i ->
                    viewModel.cancelLessonFromId(
                        arguments?.getString(BundleKeys.MAIN_TO_CURRENT_KEY) ?: "1"
                    )
                    viewModel.cancelLiveData.observe(viewLifecycleOwner) {
                        Log.e("ololo", "showCancelAlert: ${it.toString()}")
                        if (it?.success != null) {
                            showSuccessCancelAlert()
                            binding.btnCancelLesson.viewVisibility(false)
                        } else {
                            showToast("Success = null")
                        }
                    }
                    dialogInterface.cancel()
                })
            .setNegativeButton(
                getString(R.string.back),
                DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.cancel()
                })
            .show()
    }


    private fun showAlert() {
        AlertDialog.Builder(requireContext())
            .setTitle("Отмена занятия невозможна")
            .setCancelable(true)
            .setNegativeButton(
                getString(R.string.ok),
                DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.cancel()
                })
            .show()
    }

    private fun showSuccessCancelAlert() {
        AlertDialog.Builder(requireContext())
            .setTitle("Ваше занятие отменено")
            .setCancelable(true)
            .setNegativeButton(
                getString(R.string.ok),
                DialogInterface.OnClickListener { dialogInterface, i ->
                    findNavController().navigateUp()
                    dialogInterface.cancel()
                })
            .show()
    }

    @SuppressLint("SimpleDateFormat")
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
            binding.tvEndTime.text = "${timeParts[0]}:${timeParts[1]}"
        } catch (e: Exception) {
            showToast(e.message.toString())
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun isLessThan4Hours(dt: String): Boolean {
        val dateFormat = SimpleDateFormat(getString(R.string.yyyy_mm_dd_hh_mm_ss))

        val targetDateTime = dateFormat.parse(dt)
        val currentTime = Date()
        val timeDifference = targetDateTime!!.time - currentTime.time
        return timeDifference > (4 * 60 * 60 * 1000)
    }

    private fun showImage() {
        binding.circleImageView.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.show_photo_profile)
            val image = dialog.findViewById<ImageView>(R.id.image)

            if (binding.circleImageView.drawable is BitmapDrawable) {
                image.setImageBitmap((binding.circleImageView.drawable as BitmapDrawable).bitmap)
            } else if (binding.circleImageView.drawable is VectorDrawable) {
                image.setImageDrawable(binding.circleImageView.drawable)
            } else {
                image.setImageResource(R.drawable.ic_default_photo)
            }

            dialog.window?.setBackgroundDrawableResource(R.drawable.ic_default_photo)
            dialog.show()
        }
    }
}