package com.example.drivingschool.ui.fragments.lessonInfo.currentDetails

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.drivingschool.R
import com.example.drivingschool.base.BaseFragment
import com.example.drivingschool.databinding.FragmentCurrentLessonDetailBinding
import com.example.drivingschool.tools.showImage
import com.example.drivingschool.tools.showToast
import com.example.drivingschool.tools.viewVisibility
import com.example.drivingschool.ui.fragments.Constants
import com.example.drivingschool.ui.fragments.noInternet.NetworkConnection
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date

@AndroidEntryPoint
class CurrentLessonDetailsFragment :
    BaseFragment<FragmentCurrentLessonDetailBinding, CurrentLessonDetailsViewModel>(R.layout.fragment_current_lesson_detail) {

    override val binding by viewBinding(FragmentCurrentLessonDetailBinding::bind)
    override val viewModel: CurrentLessonDetailsViewModel by viewModels()
    private var inputDateTimeString: String? = null
    private lateinit var networkConnection: NetworkConnection

    override fun initialize() {
        networkConnection = NetworkConnection(requireContext())
        networkConnection.observe(viewLifecycleOwner){
            if (it) viewModel.getDetails(arguments?.getString(Constants.MAIN_TO_CURRENT_KEY) ?: Constants.DEFAULT_KEY)
        }
        binding.layoutSwipeRefresh.setOnRefreshListener {
            networkConnection.observe(viewLifecycleOwner){
                if (it) viewModel.getDetails(arguments?.getString(Constants.MAIN_TO_CURRENT_KEY) ?: Constants.DEFAULT_KEY)
            }
            binding.layoutSwipeRefresh.isRefreshing = false
        }

        binding.circleImageView.showFullSizeImage()
    }

    override fun setupListeners() {
        binding.btnCancelLesson.setOnClickListener {
            dateValidityCheck()
        }
    }

    override fun setupSubscribes() {

        viewModel.detailsState.collectStateFlow(
            empty = {
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
                showToast("UiState.Error")
            },
            success = {
                inputDateTimeString = "${it?.date}, ${it?.time}"
                Log.e("ololo", "inputDateTimeString: $inputDateTimeString")
                Log.e("ololo", "setupSubscribes: $it")
                //showToast("UiState.Success")
                binding.apply {
                    detailsProgressBar.viewVisibility(false)
                    mainContainer.viewVisibility(true)
                    val last = it?.instructor?.lastname ?: ""
                    tvUserName.text = getString(
                        R.string.person_full_name,
                        it?.instructor?.surname,
                        it?.instructor?.name,
                        last
                    )
                    tvUserNumber.text = it?.instructor?.phoneNumber

                    tvStartDate.text = formatDate(it?.date)
                    tvEndDate.text = formatDate(it?.date)

                    tvStartTime.text = timeWithoutSeconds(it?.time)
                    calculateEndTime(it?.time, tvEndTime)
                    circleImageView.showImage(it?.instructor?.profilePhoto?.big)

//                    Picasso.get().load(it?.instructor?.profilePhoto?.big) //changed to big
//                        .into(binding.circleImageView)

                    if(it?.status == getString(R.string.active)) {
                        btnCancelLesson.viewVisibility(false)
                    } else {
                        btnCancelLesson.viewVisibility(true)
                    }
                }
            }
        )

    }

    private fun dateValidityCheck() {
        if (inputDateTimeString != null) {
            if (isLessThan4Hours(inputDateTimeString!!)) {
                showCancelAlert()
            } else {
                showAlert()
            }
        }
    }

    private fun showCancelAlert() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.cancel_lesson_text))
            .setCancelable(true)
            .setPositiveButton(
                getString(R.string.confirm)
            ) { dialogInterface, _ ->
                viewModel.cancelLessonFromId(
                    arguments?.getString(Constants.MAIN_TO_CURRENT_KEY) ?: "1"
                )
                networkConnection.observe(viewLifecycleOwner) { data ->
                    if (data) {
                        viewModel.cancelLiveData.observe(viewLifecycleOwner) {
                            Log.e("ololo", "showCancelAlert: ${it.toString()}")
                            if (it?.status == "success") {
                                showSuccessCancelAlert()
                                binding.btnCancelLesson.viewVisibility(false)
                            } else {
                                showToast("error")
                            }
                        }
                    }
                }
                dialogInterface.cancel()
            }
            .setNegativeButton(
                getString(R.string.back)
            ) { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .show()
    }


    private fun showAlert() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.cancel_is_impossible))
            .setMessage(getString(R.string.cancel_alert_msg))
            .setCancelable(true)
            .setNegativeButton(
                getString(R.string.ok)
            ) { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .show()
    }

    private fun showSuccessCancelAlert() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.canceled_text))
            .setCancelable(true)
            .setNegativeButton(
                getString(R.string.ok)
            ) { dialogInterface, _ ->
                findNavController().navigateUp()
                dialogInterface.cancel()
            }
            .show()
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