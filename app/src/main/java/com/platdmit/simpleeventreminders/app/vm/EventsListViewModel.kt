package com.platdmit.simpleeventreminders.app.vm

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.SavedStateHandle
import com.platdmit.simpleeventreminders.domains.utilities.events.GetEventsUseCase

class EventsListViewModel
@ViewModelInject
constructor(
    private val getEventsUseCase: GetEventsUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    val eventsLiveData = LiveDataReactiveStreams.fromPublisher(contentProvider)
    val messageLiveData = LiveDataReactiveStreams.fromPublisher(messageProvider)
    init {
        compositeDisposable.add(
            getEventsUseCase.getEvents().subscribe(
                {
                    contentProvider.onNext(it)
                    messageProvider.onNext(MessageType.SUCCESS_LOAD)
                    messageProvider.onNext(MessageType.EMPTY)
                }, {
                    messageProvider.onNext(MessageType.FALL)
                }
            )
        )
    }

    fun refreshResult(){
        getEventsUseCase.updateResult()
    }
}