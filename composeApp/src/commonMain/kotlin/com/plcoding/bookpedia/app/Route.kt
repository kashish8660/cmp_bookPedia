package com.plcoding.bookpedia.app

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object BookGraph: Route

    @Serializable
    data object BookList: Route //'data object' provides inbuilt functions like toString(), equals(), hashCode()

    @Serializable
    data class BookDetail(val id: String): Route //'id' is the argument to passed in destination
}