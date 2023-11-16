package com.example.drivingschool.ui.fragments.instructorLessonInfo.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.drivingschool.base.BaseViewModel
import com.example.drivingschool.data.models.start_finish_lesson.FinishLessonResponse
import com.example.drivingschool.data.models.start_finish_lesson.StartLessonResponse
import com.example.drivingschool.data.repositories.MainRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

class StartFinishLessonViewModel @Inject constructor(private val repository: MainRepository) :
    BaseViewModel() {

    private var _finishLessonResult = MutableLiveData<FinishLessonResponse?>()
    val finishLessonResult = _finishLessonResult as LiveData<FinishLessonResponse?>


    private var _startLessonResult = MutableLiveData<StartLessonResponse?>()
    val startLessonResult = _startLessonResult as LiveData<StartLessonResponse?>


    fun startLesson(id: String) = viewModelScope.launch {
        repository.startLesson(id).collect {
            _startLessonResult.value = it
        }
    }

    fun finishLesson(id: String) = viewModelScope.launch {
        repository.finishLesson(id).collect {
            _finishLessonResult.value = it
        }
    }
}