package com.example.drivingschool.tools

sealed class UiState<T> {
    class Loading<T> : UiState<T>()
    class Success<T>(var data: T?) : UiState<T>()
    class Error<T>(val msg: String) : UiState<T>()
    class Empty<T>: UiState<T>()
}