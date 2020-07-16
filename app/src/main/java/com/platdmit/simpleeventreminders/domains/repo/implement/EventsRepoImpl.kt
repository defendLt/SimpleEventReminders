package com.platdmit.simpleeventreminders.domains.repo.implement

import com.platdmit.simpleeventreminders.data.database.DbManager
import com.platdmit.simpleeventreminders.data.database.dao.EventDao
import com.platdmit.simpleeventreminders.data.database.entity.DbEvent
import com.platdmit.simpleeventreminders.domains.converters.EventsConverter
import com.platdmit.simpleeventreminders.domains.model.Event
import com.platdmit.simpleeventreminders.domains.repo.EventsRepo
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class EventsRepoImpl
constructor(
    private val eventsDatabase: EventDao,
    private val eventsConverter: EventsConverter<DbEvent, Event>
) : EventsRepo {
    override fun getEventsList(): Single<List<Event>> {
        return Single.create{
            val events = eventsDatabase.getElements()?.map { event -> eventsConverter.fromDbToDomain(event) }
            if(events?.isEmpty() != true){
                events?.forEach { event -> println("EventsRepoImpl: $event") }
                it.onSuccess(events)
            } else {
                it.onError(Throwable("Empty Elements"))
            }
        }
    }

    override fun getEvent(id: Int): Single<Event> {
        return Single.create{
            val dbEvent = eventsDatabase.getElement(id)
            if(dbEvent != null){
                it.onSuccess(eventsConverter.fromDbToDomain(dbEvent))
            } else {
                it.onError(Throwable("Empty Element ID: $id"))
            }
        }
    }

    override fun addEvent(event: Event): Single<Long> {
        return Single.create {
            val dbEvent = eventsConverter.fromDomainToDb(event)
            try {
                it.onSuccess(eventsDatabase.insert(dbEvent))
            } catch (e: Exception) {
                it.onError(e)
            }
        }
    }

    override fun updEvent(event: Event): Completable {
        return Completable.create {
            val dbEvent = eventsConverter.fromDomainToDb(event)
            try {
                eventsDatabase.update(dbEvent)
                it.onComplete()
            } catch (e: Exception) {
                it.onError(e)
            }
        }
    }

    override fun delEvent(event: Event): Completable {
        return Completable.create {
            val dbEvent = eventsConverter.fromDomainToDb(event)
            try {
                eventsDatabase.delete(dbEvent)
                println("EventsRepoImpl delEvent: $dbEvent")
                it.onComplete()
            } catch (e: Exception) {
                it.onError(e)
            }
        }
    }
}