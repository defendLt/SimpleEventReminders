package com.platdmit.simpleeventreminders.domains.state

sealed class EventState<out R> {
    data class LoadSuccess<out R>(val data: R) : EventState<R>()
    data class DeletedSuccess<out R>(val data: R) : EventState<R>()
    object CreateSuccess : EventState<Nothing>()
    object SaveSuccess : EventState<Nothing>()
    object Loading : EventState<Nothing>()
    data class Error(val message : String) : EventState<Nothing>()
}