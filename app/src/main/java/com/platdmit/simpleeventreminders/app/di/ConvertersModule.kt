package com.platdmit.simpleeventreminders.app.di

import com.platdmit.simpleeventreminders.data.database.entity.DbEvent
import com.platdmit.simpleeventreminders.domains.converters.EventsConverter
import com.platdmit.simpleeventreminders.domains.converters.EventsConverterImp
import com.platdmit.simpleeventreminders.domains.model.Event
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ConvertersModule {

    @Singleton
    @Provides
    fun providesEventConverter() : EventsConverter<DbEvent, Event>{
        return EventsConverterImp()
    }

}