package com.platdmit.simpleeventreminders.domains.utilities.events

import com.platdmit.simpleeventreminders.domains.model.Event
import com.platdmit.simpleeventreminders.domains.repo.EventsRepo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class EventUseCase
@Inject
constructor(
    private val eventsRepo: EventsRepo
) : ChangeEventsUseCase {
    override fun addEvent(event: Event): Single<Long> {
        return eventsRepo.addEvent(event)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun delEvent(event: Event): Completable {
        println("ChangeEventsUseCase delEvent: $event")
        return eventsRepo.delEvent(event)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun updEvent(event: Event): Completable {
        println("ChangeEventsUseCase updEvent: $event")
        return eventsRepo.updEvent(event)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getEvent(eventId: Int): Observable<Event> {
        TODO("Not yet implemented")
    }
}