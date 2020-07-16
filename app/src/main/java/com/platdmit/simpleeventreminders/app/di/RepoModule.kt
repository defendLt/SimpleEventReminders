package com.platdmit.simpleeventreminders.app.di

import com.platdmit.simpleeventreminders.data.database.dao.EventDao
import com.platdmit.simpleeventreminders.data.database.entity.DbEvent
import com.platdmit.simpleeventreminders.domains.converters.EventsConverter
import com.platdmit.simpleeventreminders.domains.model.Event
import com.platdmit.simpleeventreminders.domains.repo.EventsRepo
import com.platdmit.simpleeventreminders.domains.repo.implement.EventsRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepoModule {

    @Singleton
    @Provides
    fun provideEventsRepo(
        eventDao: EventDao,
        eventsConverter: EventsConverter<DbEvent, Event>
    ) : EventsRepo{
        return EventsRepoImpl(eventDao, eventsConverter)
    }
}