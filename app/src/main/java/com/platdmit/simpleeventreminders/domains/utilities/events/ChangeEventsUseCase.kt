package com.platdmit.simpleeventreminders.domains.utilities.events

import com.platdmit.simpleeventreminders.domains.model.Event
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import org.joda.time.DateTime

interface ChangeEventsUseCase {
    fun getEvent(eventId : Int) : Observable<Event>
    fun addEvent(event : Event) : Single<Long>
    fun delEvent(event : Event) : Completable
    fun updEvent(event : Event) : Completable
}