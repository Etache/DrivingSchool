package com.example.drivingschool.ui.fragments.studentMain.studentLessonInfo.currentDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.drivingschool.base.BaseViewModel
import com.example.drivingschool.data.models.CancelResponse
import com.example.drivingschool.data.models.mainresponse.LessonsItem
import com.example.drivingschool.data.repositories.DrivingRepository
import com.example.drivingschool.tools.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrentLessonDetailsViewModel @Inject constructor(
    private val repository: DrivingRepository
) : BaseViewModel() {

    private val _detailsState = MutableStateFlow<UiState<LessonsItem>>(UiState.Loading())
    val detailsState = _detailsState.asStateFlow()

    private var _cancelLiveData = MutableLiveData<CancelResponse?>()
    val cancelLiveData = _cancelLiveData as LiveData<CancelResponse?>

    fun getDetails(id: String) = viewModelScope.launch {
        repository.getCurrentDetails(id).collect {
            _detailsState.value = it
        }
    }

    fun cancelLessonFromId(id: String) = viewModelScope.launch {
        repository.cancelLesson(id).collect{
            _cancelLiveData.value = it
        }
    }

}