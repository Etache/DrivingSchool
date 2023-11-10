package com.example.drivingschool.ui.fragments.previousDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.drivingschool.base.BaseViewModel
import com.example.drivingschool.data.models.FeedbackForInstructorRequest
import com.example.drivingschool.data.models.FeedbackForInstructorResponse
import com.example.drivingschool.data.models.mainresponse.LessonsItem
import com.example.drivingschool.data.repositories.DetailsRepository
import com.example.drivingschool.tools.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreviousLessonDetailsViewModel @Inject constructor(
    private val repository: DetailsRepository
) : BaseViewModel() {

    private val _detailsState = MutableStateFlow<UiState<LessonsItem>>(UiState.Loading())
    val detailsState = _detailsState.asStateFlow()

    private var _commentLiveData = MutableLiveData<FeedbackForInstructorResponse>()
    val commentLiveData = _commentLiveData as LiveData<FeedbackForInstructorResponse>

    fun getDetails(id: String) = viewModelScope.launch {
        repository.getPreviousDetails(id).collect {
            _detailsState.value = it
        }
    }

    fun saveComment(comment: FeedbackForInstructorRequest) = viewModelScope.launch{
        repository.saveComment(comment).collect{
            _commentLiveData.value = it
        }
    }
}