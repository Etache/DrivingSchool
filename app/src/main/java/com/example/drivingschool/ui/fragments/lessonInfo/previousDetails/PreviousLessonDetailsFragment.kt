package com.example.drivingschool.ui.fragments.lessonInfo.previousDetails

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.data.models.FeedbackForInstructorRequest
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
import java.util.Locale

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
        showImage()
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
                            binding.detailsProgressBar.viewVisibility(false)
                            binding.mainContainer.viewVisibility(true)
                            Log.e("ololo", "setupSubscribes: $it")
                            //showToast("UiState.Success")
                            binding.apply {
                                detailsProgressBar.viewVisibility(false)
                                btnComment.viewVisibility(true)
                                studentId = it.data?.student?.id.toString()
                                val last = it.data?.instructor?.lastname ?: ""
                                tvUserName.text =
                                    "${it.data?.instructor?.surname} ${it.data?.instructor?.name} $last"
                                tvUserNumber.text = it.data?.instructor?.phone_number
                                tvPreviousStartDate.text = formatDate(it.data?.date)
                                tvScheduleEndDate.text = formatDate(it.data?.date)
                                tvPreviousStartTime.text = timeWithoutSeconds(it.data?.time)
                                calculateEndTime(it.data?.time)

                                val httpsImageUrl = it.data?.instructor?.profile_photo?.replace(
                                    "http://",
                                    "https://"
                                )
                                Picasso.get()
                                    .load(httpsImageUrl)
                                    .placeholder(R.drawable.ic_default_photo)
                                    .into(circleImageView)


                                if (it.data?.feedbackForStudent != null) {
                                    containerComment.viewVisibility(true)
                                    val lastILN =
                                        it.data?.feedbackForStudent?.instructor?.lastname ?: ""
                                    tvCommentTitle.text =
                                        getString(
                                            R.string.person_full_name,
                                            it.data?.feedbackForStudent?.instructor?.surname,
                                            it.data?.feedbackForStudent?.instructor?.name,
                                            lastILN
                                        )
                                    tvCommentBody.text = it.data?.feedbackForStudent?.text
                                    tvCommentDate.text =
                                        formatDateTime(it.data?.feedbackForStudent?.created_at!!)
                                    rbCommentSmall.rating =
                                        it.data?.feedbackForStudent?.mark?.toInt()!!.toFloat()
                                    Log.e("ololo", "setupSubscribes: full ${it.data}")
                                    val httpToHttps =
                                        it.data?.feedbackForStudent?.instructor?.profile_photo?.replace(
                                            "http://",
                                            "https://"
                                        )
                                    Log.e("ololo", "setupSubscribes: after $httpToHttps")
                                    Picasso.get()
                                        .load(httpToHttps)
                                        .placeholder(R.drawable.ic_default_photo)
                                        .into(circleCommentImage)
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
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = inputFormat.parse(inputDate) ?: return ""

        val outputFormat = SimpleDateFormat("d MMMM", Locale("ru"))
        return outputFormat.format(date).replaceFirstChar { it.uppercase() }
    }

    private fun formatDateTime(createdAt: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSX", Locale.ENGLISH)
        val date = inputFormat.parse(createdAt)

        val outputFormat = SimpleDateFormat("d MMMM", Locale("ru"))
        val formattedDate = outputFormat.format(date)
        val calendar = Calendar.getInstance()
        calendar.time = date

        val day = calendar.get(Calendar.DAY_OF_MONTH)
        // Получаем месяц из отформатированной даты
        val month = formattedDate.split(" ")[1]
        return "$day $month"
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
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        btn.setOnClickListener {
            createComment(
                FeedbackForInstructorRequest(
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

    private fun createComment(comment: FeedbackForInstructorRequest) {
        viewModel.saveComment(comment)
        viewModel.commentLiveData.observe(viewLifecycleOwner) {
            it.status?.let { showToast(it) }
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
            val timeParts = outputTime.split(":")
            binding.tvPreviousEndTime.text = "${timeParts[0]}:${timeParts[1]}"
        } catch (e: Exception) {
            showToast(e.message.toString())
        }
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