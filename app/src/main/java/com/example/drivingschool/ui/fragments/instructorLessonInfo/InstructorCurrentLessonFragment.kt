package com.example.drivingschool.ui.fragments.instructorLessonInfo

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
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
    private lateinit var networkConnection: NetworkConnection
    //private var id: String? = null

    override fun initialize() {
        networkConnection = NetworkConnection(requireContext())
        Log.e("ololololo", "initialize: ${arguments?.getString("key")}")
        networkConnection.observe(viewLifecycleOwner){
            viewModel.getCurrentById(arguments?.getString("key") ?: "1")
        }

        showImage()
    }

    override fun setupListeners() {
        binding.btnStartLesson.setOnClickListener {
            binding.btnStartLesson.text = "Завершить занятие"
        }
        binding.layoutSwipeRefresh.setOnRefreshListener {
            networkConnection.observe(viewLifecycleOwner){
                viewModel.getCurrentById(arguments?.getString("key") ?: "1")
            }
        }
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
                            Log.e(
                                "ahahaha",
                                "InstructorCurrentLessonFragment Success: ${state.data}",
                            )

                            binding.apply {
                                progressBar.visibility = View.GONE
                                clContainer.visibility = View.VISIBLE

                                tvFullname.text =
                                    "${state.data?.student?.surname} ${state.data?.student?.name} ${state.data?.student?.lastname}"
                                tvNumber.text = state.data?.student?.phone_number

                                Picasso.get()
                                    .load(state.data?.student?.profile_photo)
                                    .placeholder(R.drawable.ic_default_photo)
                                    .into(ivProfileImage)

                                tvBeginningTime.text = timeWithoutSeconds(state.data?.time)
                                calculateEndTime(state.data?.time)

                                binding.tvBeginningDate.text = formatDate(state.data?.date)
                                binding.tvEndingDate.text = formatDate(state.data?.date)

//                                val parts = (originalDate?.split("-"))!!
//                                val day = parts[0].toInt()
//                                val month = parts[1].toInt()
//                                val monthString = when (month) {
//                                    1 -> "января"
//                                    2 -> "февраля"
//                                    3 -> "марта"
//                                    4 -> "апреля"
//                                    5 -> "мая"
//                                    6 -> "июня"
//                                    7 -> "июля"
//                                    8 -> "августа"
//                                    9 -> "сентября"
//                                    10 -> "октября"
//                                    11 -> "ноября"
//                                    12 -> "декабря"
//                                    else -> throw IllegalArgumentException("Некорректный месяц: $month")
//                                }
//                                tvBeginningDate.text = "$day $monthString"
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
}