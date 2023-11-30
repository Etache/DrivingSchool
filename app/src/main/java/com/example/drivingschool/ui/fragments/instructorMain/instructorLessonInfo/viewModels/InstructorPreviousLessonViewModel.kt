package com.example.drivingschool.ui.fragments.instructorMain.instructorLessonInfo.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.drivingschool.base.BaseViewModel
import com.example.drivingschool.data.models.FeedbackForStudentRequest
import com.example.drivingschool.data.models.FeedbackForStudentResponse
import com.example.drivingschool.data.models.mainresponse.LessonsItem
import com.example.drivingschool.data.repositories.DrivingRepository
import com.example.drivingschool.tools.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InstructorPreviousLessonViewModel @Inject constructor(
    private val repository: DrivingRepository
)  : BaseViewModel() {

    private val _detailsState = MutableStateFlow<UiState<LessonsItem>>(UiState.Loading())
    val detailsState = _detailsState.asStateFlow()

    private var _commentLiveData = MutableLiveData<FeedbackForStudentResponse>()
    val commentLiveData = _commentLiveData as LiveData<FeedbackForStudentResponse>

    fun getDetails(id: String) = viewModelScope.launch {
        repository.getPreviousDetailsInstructor(id).collect {
            _detailsState.value = it
        }
    }

    fun saveComment(comment: FeedbackForStudentRequest) = viewModelScope.launch{
        repository.saveInstructorComment(comment).collect{
            _commentLiveData.value = it
        }
    }

}