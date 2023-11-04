package ru.jengle88.deliveryapp.common

sealed class ApiResult<T>(val result: T? = null, val message: String? = null) {
    class Loading<T>: ApiResult<T>()

    class Success<T>(result: T?): ApiResult<T>(result = result)

    class Failure<T>(message: String?): ApiResult<T>(message = message)

}
