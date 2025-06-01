package com.plcoding.bookpedia.core.domain

//Nothing is  bottom type (a subtype of every other type). You can use it to represent values that never exist.
sealed interface Result<out D, out E: Error> { //Now D can be of anytype and E is restricted to 'Error' interface type
    data class Success<out D>(val data: D): Result<D, Nothing> //pass HttpResponse's body in D and get Result. Then use below mapper (DTO -> Object needed by our view)
    data class Error<out E: com.plcoding.bookpedia.core.domain.Error>(val error: E):
        Result<Nothing, E>
}

inline fun <T, E: Error, R> Result<T, E>.map(map: (T) -> R): Result<R, E> { //T is a generic class which represents object returned by API. '<T, E: Error, R>' represents generic classes.
    return when(this) {
        is Result.Error -> Result.Error(error) //I case the 'Result' is of Error type, it already has 'error' property and same we are passing on without touching anything. In case of Success, we are using map function before passing on.
        is Result.Success -> Result.Success(map(data)) //API DTO -> Object needed by our view
    }
}

fun <T, E: Error> Result<T, E>.asEmptyDataResult(): EmptyResult<E> { //
    return map {  }
}

//Below onSuccess and onError are used by ViewModel to react
inline fun <T, E: Error> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    return when(this) {
        is Result.Error -> this
        is Result.Success -> {
            action(data)
            this
        }
    }
}
inline fun <T, E: Error> Result<T, E>.onError(action: (E) -> Unit): Result<T, E> {
    return when(this) {
        is Result.Error -> {
            action(error)
            this
        }
        is Result.Success -> this
    }
}

typealias EmptyResult<E> = Result<Unit, E>


//Using 'sealed interface' above and not this 'sealed class', cuz now 'message' is not restricted to being String. In sealed interface above, error can be of any type implementing Error interface.
sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?) : Resource<T>(data)
    class Error<T>(data: T?, message: String?): Resource<T>(data, message)
}


























