package com.platdmit.simpleeventreminders.domains.repo

import com.platdmit.simpleeventreminders.domains.model.Event
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface EventsRepo {
    fun getEventsList() : Single<List<Event>>
    fun getEvent(id : Int) : Single<Event>
    fun addEvent(event: Event) : Single<Long>
    fun updEvent(event: Event) : Completable
    fun delEvent(event: Event) : Completable
}