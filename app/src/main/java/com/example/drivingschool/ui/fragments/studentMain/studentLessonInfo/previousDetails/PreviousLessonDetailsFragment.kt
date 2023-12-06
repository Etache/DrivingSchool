package com.example.drivingschool.ui.fragments.studentMain.studentLessonInfo.previousDetails

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.data.models.FeedbackForInstructorRequest
import com.example.drivingschool.databinding.FragmentPreviousLessonDetailsBinding
import com.example.drivingschool.tools.showImage
import com.example.drivingschool.tools.showToast
import com.example.drivingschool.tools.viewVisibility
import com.example.drivingschool.ui.fragments.Constants
import com.example.drivingschool.ui.fragments.noInternet.NetworkConnection
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreviousLessonDetailsFragment :
    BaseFragment<FragmentPreviousLessonDetailsBinding, PreviousLessonDetailsViewModel>() {
    override fun getViewBinding(): FragmentPreviousLessonDetailsBinding =
        FragmentPreviousLessonDetailsBinding.inflate(layoutInflater)

    override val viewModel: PreviousLessonDetailsViewModel by viewModels()

    private lateinit var lessonId: String
    private lateinit var studentId: String
    private var isCommentCreated: Boolean = false
    private lateinit var networkConnection: NetworkConnection

    override fun initialize() {
        networkConnection = NetworkConnection(requireContext())
        lessonId = arguments?.getString(Constants.MAIN_TO_PREVIOUS_KEY) ?: Constants.DEFAULT_KEY
        networkConnection.observe(viewLifecycleOwner) {
            if (it) viewModel.getDetails(lessonId)
        }

        binding.layoutSwipeRefresh.setOnRefreshListener {
            networkConnection.observe(viewLifecycleOwner) {
                if (it) viewModel.getDetails(lessonId)
            }
            binding.layoutSwipeRefresh.isRefreshing = false
        }

        binding.tvCommentBody.setOnClickListener {
            if (binding.tvCommentBody.maxHeight > 60) {
                binding.tvCommentBody.maxHeight = 60
            } else {
                binding.tvCommentBody.maxHeight = 400
            }
        }

        binding.circleImageView.setOnClickListener {
            binding.circleImageView.showFullSizeImage()
        }


        binding.btnComment.setOnClickListener {
            if (!isCommentCreated) showCustomDialog()
            else binding.btnComment.viewVisibility(false)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun setupSubscribes() {

        viewModel.detailsState.collectStateFlow(
            empty = {
                showToast(getString(R.string.empty_state))
                binding.detailsProgressBar.viewVisibility(false)
                binding.mainContainer.viewVisibility(true)
            },
            loading = {
                binding.apply {
                    detailsProgressBar.viewVisibility(true)
                    mainContainer.viewVisibility(false)
                }
            },
            error = {
                showToast(it)
                binding.detailsProgressBar.viewVisibility(false)
                binding.mainContainer.viewVisibility(true)
            },
            success = {
                binding.detailsProgressBar.viewVisibility(false)
                binding.mainContainer.viewVisibility(true)
                binding.apply {
                    detailsProgressBar.viewVisibility(false)
                    btnComment.viewVisibility(true)
                    studentId = it?.student?.id.toString()
                    tvUserName.text = getString(
                        R.string.person_full_name,
                        it?.instructor?.surname,
                        it?.instructor?.name
                    )
                    val number = it?.instructor?.phoneNumber
                    binding.tvUserNumber.text = number?.substring(0, 4) + " " + number?.substring(
                        4,
                        7
                    ) + " " + number?.substring(7, 10) + " " + number?.substring(10)
                    tvPreviousStartDate.text = formatDate(it?.date)
                    tvScheduleEndDate.text = formatDate(it?.date)
                    tvPreviousStartTime.text = timeWithoutSeconds(it?.time)
                    calculateEndTime(it?.time, tvPreviousEndTime)
                    circleImageView.showImage(it?.instructor?.profilePhoto?.big)


                    if (it?.feedbackForInstructor != null) {
                        isCommentCreated = true
                        binding.btnComment.viewVisibility(false)
                    }

                    if (it?.feedbackForStudent != null) {
                        containerComment.viewVisibility(true)
                        tvCommentTitle.text =
                            getString(
                                R.string.person_full_name,
                                it.feedbackForStudent?.instructor?.surname,
                                it.feedbackForStudent?.instructor?.name
                            )
                        tvCommentBody.text = it.feedbackForStudent?.text
                        tvCommentDate.text =
                            formatDateTime(it.feedbackForStudent?.createdAt!!)
                        rbCommentSmall.rating =
                            it.feedbackForStudent?.mark?.toInt()!!.toFloat()
                        circleCommentImage.showImage(it.feedbackForStudent?.instructor?.profilePhoto?.big)
                    }
                }
            }
        )

    }


    @SuppressLint("MissingInflatedId")
    private fun showCustomDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val customDialog =
            LayoutInflater.from(requireContext()).inflate(R.layout.custom_rate_dialog, null)
        builder.setView(customDialog)
        builder.setCancelable(false)

        val rating = customDialog.findViewById<RatingBar>(R.id.rb_comment_small)
        val edt = customDialog.findViewById<EditText>(R.id.et_comment_text)
        val counter = customDialog.findViewById<TextView>(R.id.tv_comment_character_count)
        val btnSend = customDialog.findViewById<Button>(R.id.btn_comment_confirm)
        val btnDismiss = customDialog.findViewById<ImageButton>(R.id.btn_dismiss)

        edt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length == 0) {
                    btnSend.setBackgroundColor(resources.getColor(R.color.gray_btn))
                } else {
                    btnSend.setBackgroundColor(resources.getColor(R.color.bright_blue))
                }
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                counter.text = "(${p0?.length.toString()}/250)"
                if (p0?.length == 0) {
                    btnSend.setBackgroundColor(resources.getColor(R.color.gray_btn))
                } else {
                    btnSend.setBackgroundColor(resources.getColor(R.color.bright_blue))
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length == 0) {
                    btnSend.setBackgroundColor(resources.getColor(R.color.gray_btn))
                } else {
                    btnSend.setBackgroundColor(resources.getColor(R.color.bright_blue))
                }
            }
        })

        val dialog = builder.create()
        btnSend.setOnClickListener {
            if (edt.text.toString().isNotEmpty()) {
                createComment(
                    FeedbackForInstructorRequest(
                        lesson = lessonId.toInt(),
                        instructor = studentId.toInt(),
                        text = edt.text.toString(),
                        mark = rating.rating.toInt()
                    )
                )
                dialog.dismiss()
            }
        }
        btnDismiss.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun createComment(comment: FeedbackForInstructorRequest) {
        networkConnection.observe(viewLifecycleOwner) {
            if (it) viewModel.saveComment(comment)
        }
        viewModel.commentLiveData.observe(viewLifecycleOwner) {
            if(it.status == getString(R.string.success)) {
                showAlert()
            } else {
                showToast(getString(R.string.couldnt_create_comment))
            }
        }
    }

    private fun showAlert() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.comment_is_sended))
            .setCancelable(true)
            .setNegativeButton(
                getString(R.string.ok)
            ) { dialogInterface, _ ->
                networkConnection.observe(viewLifecycleOwner) {
                    if (it) viewModel.getDetails(lessonId)
                }
                dialogInterface.cancel()
            }
            .show()
    }

}
