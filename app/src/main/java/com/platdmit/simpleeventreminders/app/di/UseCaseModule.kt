package com.platdmit.simpleeventreminders.app.di

import com.platdmit.simpleeventreminders.domains.repo.EventsRepo
import com.platdmit.simpleeventreminders.domains.utilities.events.ChangeEventsUseCase
import com.platdmit.simpleeventreminders.domains.utilities.events.EventUseCase
import com.platdmit.simpleeventreminders.domains.utilities.events.EventsListUseCase
import com.platdmit.simpleeventreminders.domains.utilities.events.GetEventsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object UseCaseModule {

    @Singleton
    @Provides
    fun provideEventsListUseCase(
        eventsRepo: EventsRepo
    ) : GetEventsUseCase {
        return EventsListUseCase(eventsRepo)
    }

    @Singleton
    @Provides
    fun provideEventUseCase(
        eventsRepo: EventsRepo
    ) : ChangeEventsUseCase {
        return EventUseCase(eventsRepo)
    }

}