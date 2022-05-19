package com.example.moviesearchapp.util

// retrofit2을 통한 통신이 성공인지 실패인지 나타내기 위한 클래스
// sealed class 는 동일 파일에 정의된 하위 클래스 외에 다른 하위 클래스는 존재하지 않는다는 것을 의미한다.
sealed class Resource<T> (
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>() : Resource<T>()
}