package com.example.drivingschool.ui.fragments.previousDetails

import androidx.lifecycle.viewModelScope
import com.example.drivingschool.base.BaseViewModel
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


    fun getDetails(id: String) = viewModelScope.launch {
        repository.getPreviousDetails(id).collect {
            _detailsState.value = it
        }
    }
}