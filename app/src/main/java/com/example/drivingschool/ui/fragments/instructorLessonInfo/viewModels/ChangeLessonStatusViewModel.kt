package com.example.drivingschool.ui.fragments.instructorLessonInfo.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.drivingschool.base.BaseViewModel
import com.example.drivingschool.data.models.start_finish_lesson.ChangeLessonStatusResponse
import com.example.drivingschool.data.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeLessonStatusViewModel @Inject constructor(private val repository: MainRepository) :
    BaseViewModel() {

    private var _startLessonResult = MutableLiveData<ChangeLessonStatusResponse?>()
    val startLessonResult = _startLessonResult as LiveData<ChangeLessonStatusResponse?>

    private var _finishLessonResult = MutableLiveData<ChangeLessonStatusResponse?>()
    val finishLessonResult = _finishLessonResult as LiveData<ChangeLessonStatusResponse?>

    private var _studentAbsentResult = MutableLiveData<ChangeLessonStatusResponse?>()
    val studentAbsentResult = _studentAbsentResult as LiveData<ChangeLessonStatusResponse?>


    fun startLesson(id: String) = viewModelScope.launch {
        repository.startLesson(id).collect {
            _startLessonResult.value = it
        }
    }

    fun finishLesson(id : String) = viewModelScope.launch {
        repository.finishLesson(id).collect {
            _finishLessonResult.value = it
        }
    }

    fun studentAbsent(id: String) = viewModelScope.launch {
        repository.studentAbsent(id).collect {
            _studentAbsentResult.postValue(it)
        }
    }
}