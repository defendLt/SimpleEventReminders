package com.platdmit.simpleeventreminders.app.vm

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.SavedStateHandle
import com.platdmit.simpleeventreminders.domains.model.Event
import com.platdmit.simpleeventreminders.domains.state.EventListState
import com.platdmit.simpleeventreminders.domains.utilities.events.GetEventsUseCase

class EventsListViewModel
@ViewModelInject
constructor(
    private val getEventsUseCase: GetEventsUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel<EventListState<List<Event>>>() {
    val eventsListStateLiveData = LiveDataReactiveStreams.fromPublisher(stateProvider)
    val messageLiveData = LiveDataReactiveStreams.fromPublisher(messageProvider)
    init {
        stateProvider.onNext(EventListState.Loading)
        compositeDisposable.add(
            getEventsUseCase.getEvents().subscribe(
                {
                    stateProvider.onNext(EventListState.Success(it))
                    messageProvider.onNext(MessageType.SUCCESS_LOAD)
                }, {
                    stateProvider.onNext(EventListState.Error)
                    messageProvider.onNext(MessageType.FALL)
                }
            )
        )
    }

    fun setStateInstance(stateInstance: StateInstance){
        when(stateInstance){
            StateInstance.RefreshResult -> refreshResult()
        }
    }

    private fun refreshResult(){
        getEventsUseCase.updateResult()
    }

    sealed class StateInstance {
        object RefreshResult : StateInstance()
    }
}