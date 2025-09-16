package com.example.postgresqlbasico.presentation.common

sealed class UiState<out T> {
    data class Success<T>(val data: T) : UiState<T>()
    data class Error<T>(val message: String) : UiState<T>()
    //data object Loading : UiState<Nothing>()

}