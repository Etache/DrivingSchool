package com.example.drivingschool.ui.fragments.instructorMain.instructorLessonInfo

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.data.models.FeedbackForStudentRequest
import com.example.drivingschool.databinding.FragmentInstructorPreviousLessonBinding
import com.example.drivingschool.tools.showImage
import com.example.drivingschool.tools.showToast
import com.example.drivingschool.tools.viewVisibility
import com.example.drivingschool.ui.fragments.Constants
import com.example.drivingschool.ui.fragments.instructorMain.instructorLessonInfo.viewModels.InstructorPreviousLessonViewModel
import com.example.drivingschool.ui.fragments.noInternet.NetworkConnection
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InstructorPreviousLessonFragment :
    BaseFragment<FragmentInstructorPreviousLessonBinding, InstructorPreviousLessonViewModel>() {
    override fun getViewBinding(): FragmentInstructorPreviousLessonBinding =
        FragmentInstructorPreviousLessonBinding.inflate(layoutInflater)

    override val viewModel: InstructorPreviousLessonViewModel by viewModels()

    private lateinit var lessonId: String
    private lateinit var instructorId: String
    private var isCommentCreated: Boolean = false
    private lateinit var networkConnection: NetworkConnection

    override fun initialize() {
        lessonId =
            arguments?.getString(Constants.INSTRUCTOR_MAIN_TO_PREVIOUS_KEY) ?: Constants.DEFAULT_KEY
        networkConnection = NetworkConnection(requireContext())
        networkConnection.observe(viewLifecycleOwner) {
            if (it) viewModel.getDetails(lessonId)
        }

        binding.circleImageView.setOnClickListener {
            binding.circleImageView.showFullSizeImage()
        }

        binding.btnComment.setOnClickListener {
            if (!isCommentCreated) showCustomDialog()
        }
    }

    override fun setupListeners() {
        binding.layoutSwipeRefresh.setOnRefreshListener {
            networkConnection.observe(viewLifecycleOwner) {
                if (it) viewModel.getDetails(lessonId)
            }
            binding.layoutSwipeRefresh.isRefreshing = false
        }
    }

    override fun setupSubscribes() {

        viewModel.detailsState.collectStateFlow(
            empty = {
                showToast("UiState.Empty")
                binding.detailsProgressBar.viewVisibility(false)
                binding.mainContainer.viewVisibility(true)
            },
            loading = {
                binding.detailsProgressBar.viewVisibility(true)
                binding.mainContainer.viewVisibility(false)
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
                    instructorId = it?.instructor?.id.toString()
                    val last = it?.student?.lastname ?: ""
                    tvUserName.text = getString(
                        R.string.person_full_name,
                        it?.student?.surname,
                        it?.student?.name,
                        last
                    )
                    val number = it?.student?.phoneNumber
                    binding.tvUserNumber.text = number?.substring(0, 4) + " " + number?.substring(4, 7) + " " + number?.substring(7, 10) + " " + number?.substring(10)
                    tvPreviousStartDate.text = formatDate(it?.date)
                    tvScheduleEndDate.text = formatDate(it?.date)
                    tvPreviousStartTime.text = timeWithoutSeconds(it?.time)
                    calculateEndTime(it?.time, tvPreviousEndTime)
                    circleImageView.showImage(it?.student?.profilePhoto?.big)

                    if (it?.feedbackForStudent != null) {
                        isCommentCreated = true
                        isCommentCreated = true
                        binding.btnComment.viewVisibility(false)
                    }

                }
            }
        )

    }

    @SuppressLint("MissingInflatedId")
    private fun showCustomDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val customDialog =
            LayoutInflater.from(requireContext())
                .inflate(R.layout.instructor_custom_rate_dialog, null)
        builder.setView(customDialog)

        val header = customDialog.findViewById<TextView>(R.id.tv_comment_header)
        val rating = customDialog.findViewById<RatingBar>(R.id.rb_comment_small)
        val edt = customDialog.findViewById<EditText>(R.id.et_comment_text)
        val counter = customDialog.findViewById<TextView>(R.id.tv_comment_character_count)
        val btn = customDialog.findViewById<Button>(R.id.btn_comment_confirm)

        val headerText = header.text.toString()
        header.text = headerText.replace(getString(R.string.for_instructor),
            getString(R.string.for_student))

        edt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                counter.text = "(${p0?.length.toString()}/250)"
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        val dialog = builder.create()

        btn.setOnClickListener {
            createComment(
                FeedbackForStudentRequest(
                    lesson = lessonId.toInt(),
                    student = instructorId.toInt(),
                    text = edt.text.toString(),
                    mark = rating.rating.toInt()
                )
            )
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun createComment(comment: FeedbackForStudentRequest) {
        networkConnection.observe(viewLifecycleOwner) {
            if (it) viewModel.saveComment(comment)
        }
        viewModel.commentLiveData.observe(viewLifecycleOwner) {
            it.access?.let {
                showAlert()
            }
        }
        viewModel.getDetails(lessonId)
    }

    private fun showAlert() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.comment_is_sended))
            .setCancelable(true)
            .setNegativeButton(
                getString(R.string.ok)
            ) { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .show()
    }

}