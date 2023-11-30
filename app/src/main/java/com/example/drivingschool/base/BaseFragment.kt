package com.example.drivingschool.base

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.example.drivingschool.R
import com.example.drivingschool.data.models.mainresponse.LessonsItem
import com.example.drivingschool.tools.UiState
import com.example.drivingschool.tools.showToast
import com.example.drivingschool.ui.fragments.studentMain.lesson.LessonType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

abstract class BaseFragment<VB : ViewBinding, VM : ViewModel> : Fragment() {

    private var _binding: VB? = null
    protected val binding: VB get() = _binding!!

    protected abstract fun getViewBinding(): VB
    abstract val viewModel : VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getViewBinding()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        setupListeners()
        setupRequests()
        setupSubscribes()
    }

    protected open fun initialize() {
    }

    protected open fun setupListeners() {
    }

    protected open fun setupRequests() {
    }

    protected open fun setupSubscribes() {
    }

    protected fun <T> Flow<UiState<T>>.collectStateFlow(
        empty: () -> Unit,
        loading: () -> Unit,
        error: (String) -> Unit,
        success: (T?) -> Unit
    ) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                this@collectStateFlow.collect {
                    when (it) {
                        is UiState.Empty -> {
                            empty()
                        }

                        is UiState.Loading -> {
                            loading()
                        }

                        is UiState.Error -> {
                            error(it.msg.toString())
                        }

                        is UiState.Success -> {
                            success(it.data)
                        }
                    }
                }
            }
        }
    }

    protected fun Fragment.timeWithoutSeconds(inputTime: String?): String {
        val timeParts = inputTime?.split(getString(R.string.delimeter))
        return "${timeParts?.get(0)}:${timeParts?.get(1)}"
    }

    protected fun Fragment.formatDate(inputDate: String?): String {
        val inputFormat = SimpleDateFormat(getString(R.string.yyyy_mm_dd), Locale.getDefault())
        val date = inputFormat.parse(inputDate) ?: return ""

        val outputFormat =
            SimpleDateFormat(getString(R.string.d_mmmm), Locale(getString(R.string.ru)))
        return outputFormat.format(date).replaceFirstChar { it.uppercase() }
    }

    protected fun Fragment.formatDateTime(createdAt: String): String {
        val inputFormat =
            SimpleDateFormat(getString(R.string.yyyy_mm_dd_t_hh_mm_ss_ssssssx), Locale.ENGLISH)
        return if (createdAt.isNotEmpty()) {
            try {
                val date = inputFormat.parse(createdAt)
                val outputFormat =
                    SimpleDateFormat(getString(R.string.d_mmmm), Locale(getString(R.string.ru)))
                val formattedDate = outputFormat.format(date)
                val calendar = Calendar.getInstance()
                calendar.time = date

                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val month = formattedDate.split(" ")[1]
                "$day $month"
            } catch (e: Exception) {
                getString(R.string.unknown_date_type)
            }
        } else {
            ""
        }
    }

    @SuppressLint("SimpleDateFormat")
    protected fun Fragment.calculateEndTime(inputTime: String?, tv: TextView) {
        val timeFormat = SimpleDateFormat(getString(R.string.hh_mm_ss))
        try {
            val date = inputTime?.let { timeFormat.parse(it) }
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.HOUR_OF_DAY, 1)
            val outputTimeFormat = SimpleDateFormat(getString(R.string.hh_mm_ss))
            val outputTime = outputTimeFormat.format(calendar.time)
            val timeParts = outputTime.split(":")
            tv.text = getString(R.string.calc_time_text, timeParts[0], timeParts[1])
        } catch (e: Exception) {
            showToast(e.message.toString())
        }
    }

    protected fun ImageView.showFullSizeImage() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.show_photo_profile)
        val image = dialog.findViewById<ImageView>(R.id.image)

        when (this.drawable) {
            is BitmapDrawable -> {
                image.setImageBitmap((this.drawable as BitmapDrawable).bitmap)
            }

            is VectorDrawable -> {
                image.setImageDrawable(this.drawable)
            }

            else -> {
                image.setImageResource(R.drawable.ic_default_photo)
            }
        }
        dialog.window?.setBackgroundDrawableResource(R.drawable.ic_default_photo)
        dialog.show()
    }

    protected fun sortDataByDateTime(data: List<LessonsItem>, type: LessonType): List<LessonsItem> {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val formattedData = data.map { customData ->
            Pair(sdf.parse("${customData.date} ${customData.time}"), customData)
        }

        val sortedData = if (type == LessonType.Current){
            formattedData.sortedBy { it.first }
        } else {
            formattedData.sortedByDescending { it.first }
        }

        return sortedData.map { it.second }
    }
}