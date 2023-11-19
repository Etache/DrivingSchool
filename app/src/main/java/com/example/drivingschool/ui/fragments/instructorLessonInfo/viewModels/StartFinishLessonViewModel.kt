package com.example.drivingschool.ui.fragments.instructorLessonInfo.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.drivingschool.base.BaseViewModel
import com.example.drivingschool.data.models.start_finish_lesson.FinishLessonResponse
import com.example.drivingschool.data.models.start_finish_lesson.StartLessonResponse
import com.example.drivingschool.data.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartFinishLessonViewModel @Inject constructor(private val repository: MainRepository) :
    BaseViewModel() {

    private var _startLessonResult = MutableLiveData<StartLessonResponse?>()
    val startLessonResult = _startLessonResult as LiveData<StartLessonResponse?>

    private var _finishLessonResult = MutableLiveData<FinishLessonResponse?>()
    val finishLessonResult = _finishLessonResult as LiveData<FinishLessonResponse?>


    fun startLesson(id: String) = viewModelScope.launch {
        repository.startLesson(id).collect {
            _startLessonResult.value = it
            Log.e("ahahaha", "startLesson VM: ${it}")
        }
    }

    fun finishLesson(id: String) = viewModelScope.launch {
        repository.finishLesson(id).collect {
            _finishLessonResult.value = it
        }
    }
}