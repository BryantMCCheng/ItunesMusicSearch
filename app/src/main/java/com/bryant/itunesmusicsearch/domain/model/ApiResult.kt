package com.bryant.itunesmusicsearch.domain.model

/**
 * A sealed interface to represent the result of an API call or any asynchronous operation.
 */
sealed interface ApiResult<out T> {
    // Represents a successful operation with data
    data class Success<out T>(val data: T) : ApiResult<T>

    // Represents a failure with an exception or message
    data class Error(val exception: Throwable) : ApiResult<Nothing>

    // Represents an ongoing operation
    data object Loading : ApiResult<Nothing>
}