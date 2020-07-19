package com.platdmit.simpleeventreminders.domains.state

sealed class EventListState<out R> {
    data class Success<out R>(val data: R) : EventListState<R>()
    object Empty : EventListState<Nothing>()
    object Error : EventListState<Nothing>()
    object Loading : EventListState<Nothing>()
}