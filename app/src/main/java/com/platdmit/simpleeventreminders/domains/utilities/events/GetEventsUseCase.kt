package com.platdmit.simpleeventreminders.domains.utilities.events

import com.platdmit.simpleeventreminders.domains.model.Event
import io.reactivex.rxjava3.core.Observable

interface GetEventsUseCase {
    fun getEvents() : Observable<List<Event>>
    fun updateResult()
}