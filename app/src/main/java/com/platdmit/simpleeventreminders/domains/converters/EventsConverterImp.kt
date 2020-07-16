package com.platdmit.simpleeventreminders.domains.converters

import com.platdmit.simpleeventreminders.data.database.entity.DbEvent
import com.platdmit.simpleeventreminders.domains.model.Event
import org.joda.time.DateTime
import javax.inject.Inject

class EventsConverterImp
@Inject
constructor()
    : EventsConverter<DbEvent, Event> {

    override fun fromDbToDomain(dbModel: DbEvent): Event {
        return Event(
            dbModel.id,
            dbModel.name,
            dbModel.desc,
            convertDate(dbModel.date)
        )
    }

    override fun fromDomainToDb(model: Event): DbEvent {
       return DbEvent(
           model.id,
           model.name,
           model.desc,
           model.date?.millis?:0
       )
    }

    private fun convertDate(date : Long) : DateTime?{
        return if (date > 0){
            DateTime(date)
        } else null
    }
}