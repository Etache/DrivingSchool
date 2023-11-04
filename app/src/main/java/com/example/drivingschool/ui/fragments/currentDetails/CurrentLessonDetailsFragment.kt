package com.example.drivingschool.ui.fragments.currentDetails

import android.app.AlertDialog
import android.content.DialogInterface
import android.util.Log
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

@AndroidEntryPoint
class CurrentLessonDetailsFragment :
    BaseFragment<FragmentCurrentLessonDetailBinding, CurrentLessonDetailsViewModel>(R.layout.fragment_current_lesson_detail) {

    override val binding by viewBinding(FragmentCurrentLessonDetailBinding::bind)
    override val viewModel: CurrentLessonDetailsViewModel by viewModels()

    override fun initialize() {
        Log.e("ololololo", "initialize: ${arguments?.getString(BundleKeys.MAIN_TO_CURRENT_KEY)}")
        viewModel.getDetails(arguments?.getString(BundleKeys.MAIN_TO_CURRENT_KEY) ?: "1")
    }

    override fun setupListeners() {
        binding.btnCancelLesson.setOnClickListener {
            showCancelAlert()
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
                            binding.detailsProgressBar.viewVisibility(false)
                            Log.e("ololo", "setupSubscribes: $it")
                            showToast("UiState.Success")
                            binding.apply {
                                val last = it.data?.instructor?.lastname ?: ""
                                tvUserName.text =
                                    "${it.data?.instructor?.surname} ${it.data?.instructor?.name} $last"
                                tvUserNumber.text = it.data?.instructor?.phone_number
                                tvStartDate.text = it.data?.date
                                tvEndDate.text = it.data?.date
                                tvStartTime.text = it.data?.time
                                tvEndTime.text = it.data?.time

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

    //    @SuppressLint("MissingInflatedId")
//    private fun showCustomDialog() {
//        val builder = AlertDialog.Builder(requireContext())
//        val customDialog = LayoutInflater.from(requireContext()).inflate(R.layout.custom_rate_dialog, null)
//        builder.setView(customDialog)
//
//        val rating = customDialog.findViewById<RatingBar>(R.id.rb_comment_small)
//        val edt = customDialog.findViewById<EditText>(R.id.et_comment_text)
//        val counter = customDialog.findViewById<TextView>(R.id.tv_comment_character_count)
//        val btn = customDialog.findViewById<Button>(R.id.btn_comment_confirm)
//
//        edt.addTextChangedListener(object : TextWatcher{
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                counter.text = "(${p0?.length.toString()}/250)"
////          counter.text = getString(R.string._0_250,p0?.length.toString())
//            }
//
//            override fun afterTextChanged(p0: Editable?) {}
//
//        }
//        )
//
//        btn.setOnClickListener {
//            rating.setOnRatingBarChangeListener { ratingBar, _, _ ->
//                counter.text = "${ratingBar.rating}"
//            }
//        }
//
//        val dialog = builder.create()
//        dialog.show()
//    }
    private fun showCancelAlert() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.cancel_lesson_text))
            .setCancelable(true)
            .setPositiveButton(
                getString(R.string.confirm),
                DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.cancel()
                    showToast("Вы отменили занятия")
                })
            .setNegativeButton(
                getString(R.string.back),
                DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.cancel()
                })
            .show()

    }


}