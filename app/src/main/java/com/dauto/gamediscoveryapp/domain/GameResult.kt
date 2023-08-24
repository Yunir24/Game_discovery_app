package com.dauto.gamediscoveryapp.domain

sealed class GameResult<T> {
    class Success<T>(val data: T) : GameResult<T>()
    class Loading<T> : GameResult<T>()
    class ApiError<T>(val status: String) : GameResult<T>()
    class Exception<T>(val message: String) : GameResult<T>()
}