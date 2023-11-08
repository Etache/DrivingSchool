package com.example.drivingschool.ui.fragments.currentDetails

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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

@AndroidEntryPoint
class CurrentLessonDetailsFragment :
    BaseFragment<FragmentCurrentLessonDetailBinding, CurrentLessonDetailsViewModel>(R.layout.fragment_current_lesson_detail) {

    override val binding by viewBinding(FragmentCurrentLessonDetailBinding::bind)
    override val viewModel: CurrentLessonDetailsViewModel by viewModels()
    private var inputDateTimeString: String? = null

    override fun initialize() {
        Log.e("ololololo", "initialize: ${arguments?.getString(BundleKeys.MAIN_TO_CURRENT_KEY)}")
        viewModel.getDetails(arguments?.getString(BundleKeys.MAIN_TO_CURRENT_KEY) ?: "1")
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
                            inputDateTimeString = "${it.data?.date}, ${it.data?.time}"
                            Log.e("ololo", "inputDateTimeString: $inputDateTimeString")
                            Log.e("ololo", "setupSubscribes: $it")
                            showToast("UiState.Success")
                            binding.apply {
                                detailsProgressBar.viewVisibility(false)
                                btnCancelLesson.viewVisibility(true)
                                val last = it.data?.instructor?.lastname ?: ""
                                tvUserName.text =
                                    "${it.data?.instructor?.surname} ${it.data?.instructor?.name} $last"
                                tvUserNumber.text = it.data?.instructor?.phone_number
                                tvStartDate.text = it.data?.date
                                tvEndDate.text = it.data?.date
                                tvStartTime.text = it.data?.time
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

    private fun dateValidityCheck() {
        if (inputDateTimeString != null) {
            if (isLessThan4Hours(inputDateTimeString!!)) {
                showCancelAlert()
            } else {
                showAlert()
            }
        } else {
            showToast("Время начала урока неизвестно!")
        }

    }

    private fun showCancelAlert() {
//        val builder = AlertDialog.Builder(requireContext())
//        val customDialog =
//            LayoutInflater.from(requireContext()).inflate(R.layout.custom_cancel_dialog, null)
//        builder.setView(customDialog)
//
//        val btnConfirm = customDialog.findViewById<TextView>(R.id.btn_comment_confirm)
//        val btnBack = customDialog.findViewById<TextView>(R.id.btn_comment_back)
//
//        btnConfirm.setOnClickListener {
//            viewModel.cancelLessonFromId(
//                arguments?.getString(BundleKeys.MAIN_TO_CURRENT_KEY) ?: "1"
//            )
//            viewModel.cancelLiveData.observe(viewLifecycleOwner) {
//                showToast(it?.success.toString())
//            }
//        }
//
//        val dialog = builder.create()
//        dialog.show()

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
                        showToast(it?.success.toString())
//                        binding.btnCancelLesson.viewVisibility(false)
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
                getString(R.string.alert_continue_text),
                DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.cancel()
                })
            .show()
    }

    @SuppressLint("SimpleDateFormat")
    private fun calculateEndTime(inputTime: String?){
        val timeFormat = SimpleDateFormat("HH:mm:ss")

        try {
            val date = inputTime?.let { timeFormat.parse(it) }
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.HOUR_OF_DAY, 1)
            val outputTimeFormat = SimpleDateFormat("HH:mm:ss")
            val outputTime = outputTimeFormat.format(calendar.time)
            binding.tvEndTime.text = outputTime
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

}