package com.example.drivingschool.ui.fragments.previousDetails

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.data.models.StudentCommentRequest
import com.example.drivingschool.data.models.StudentCommentResponse
import com.example.drivingschool.databinding.FragmentPreviousLessonDetailsBinding
import com.example.drivingschool.tools.UiState
import com.example.drivingschool.tools.showToast
import com.example.drivingschool.tools.viewVisibility
import com.example.drivingschool.ui.fragments.BundleKeys
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar

@AndroidEntryPoint
class PreviousLessonDetailsFragment :
    BaseFragment<FragmentPreviousLessonDetailsBinding, PreviousLessonDetailsViewModel>(R.layout.fragment_previous_lesson_details) {

    override val binding by viewBinding(FragmentPreviousLessonDetailsBinding::bind)
    override val viewModel: PreviousLessonDetailsViewModel by viewModels()
    private lateinit var lessonId: String
    private lateinit var studentId: String

    override fun initialize() {
        lessonId = arguments?.getString(BundleKeys.MAIN_TO_PREVIOUS_KEY) ?: "1"
        Log.e("ololo", "initialize: $lessonId")
        viewModel.getDetails(lessonId)


        binding.tvCommentBody.setOnClickListener {
            if (binding.tvCommentBody.maxHeight > 60) {
                binding.tvCommentBody.maxHeight = 60
            } else {
                binding.tvCommentBody.maxHeight = 400
            }
        }

        binding.btnComment.setOnClickListener {
            showCustomDialog()
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
                            Log.e("ololo", "setupSubscribes: $it")
                            showToast("UiState.Success")
                            binding.apply {
                                detailsProgressBar.viewVisibility(false)
                                btnComment.viewVisibility(true)
                                studentId = it.data?.student?.id.toString()
                                val last = it.data?.instructor?.lastname ?: ""
                                tvUserName.text =
                                    "${it.data?.instructor?.surname} ${it.data?.instructor?.name} $last"
                                tvUserNumber.text = it.data?.instructor?.phone_number
                                tvPreviousStartDate.text = it.data?.date
                                tvScheduleEndDate.text = it.data?.date
                                tvPreviousStartTime.text = it.data?.time
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
            binding.tvPreviousEndTime.text = outputTime
        } catch (e: Exception) {
            showToast(e.message.toString())
        }
    }

    @SuppressLint("MissingInflatedId")
    private fun showCustomDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val customDialog =
            LayoutInflater.from(requireContext()).inflate(R.layout.custom_rate_dialog, null)
        builder.setView(customDialog)

        val rating = customDialog.findViewById<RatingBar>(R.id.rb_comment_small)
        val edt = customDialog.findViewById<EditText>(R.id.et_comment_text)
        val counter = customDialog.findViewById<TextView>(R.id.tv_comment_character_count)
        val btn = customDialog.findViewById<Button>(R.id.btn_comment_confirm)

        edt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                counter.text = "(${p0?.length.toString()}/250)"
//                counter.text = getString(R.string._0_250.toString().toInt(), p0?.length.toString())
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        btn.setOnClickListener {
            createComment(
                StudentCommentRequest(
                    lesson = lessonId.toInt(),
                    student = studentId.toInt(),
                    text = edt.text.toString(),
                    mark = rating.rating.toInt()
                    )
            )
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun createComment(comment: StudentCommentRequest) {
        viewModel.saveComment(comment)
        viewModel.commentLiveData.observe(viewLifecycleOwner) {
            showToast(it.toString())
        }
    }
}