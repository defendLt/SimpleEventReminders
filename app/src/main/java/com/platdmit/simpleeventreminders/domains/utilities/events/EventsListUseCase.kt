package com.platdmit.simpleeventreminders.domains.utilities.events

import com.platdmit.simpleeventreminders.domains.model.Event
import com.platdmit.simpleeventreminders.domains.repo.EventsRepo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class EventsListUseCase
@Inject
constructor(
    private val eventsRepo: EventsRepo
) : GetEventsUseCase {
    private val handCall = PublishSubject.create<Boolean>()

    override fun getEvents(): Observable<List<Event>> {
        return eventsRepo.getEventsList()
            .subscribeOn(Schedulers.io())
            .toObservable()
            .repeatWhen { handCall.hide() }
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun updateResult() {
        handCall.onNext(true)
    }

}