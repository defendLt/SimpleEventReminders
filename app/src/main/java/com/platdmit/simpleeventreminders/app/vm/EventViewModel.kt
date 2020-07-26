package com.platdmit.simpleeventreminders.app.vm

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.SavedStateHandle
import com.platdmit.simpleeventreminders.domains.model.Event
import com.platdmit.simpleeventreminders.domains.state.EventState
import com.platdmit.simpleeventreminders.domains.utilities.events.ChangeEventsUseCase
import org.joda.time.DateTime

class EventViewModel
@ViewModelInject
constructor(
    private val changeEventsUseCase: ChangeEventsUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel<EventState<Event>>() {
    var eventStateLiveData = LiveDataReactiveStreams.fromPublisher(stateProvider)
    val messageLiveData = LiveDataReactiveStreams.fromPublisher(messageProvider)
    var activeEvent : Event? = null

    init {
        stateProvider.onNext(EventState.Loading)
        setEvent(
            savedStateHandle.get<Event>("EVENT")?:
            Event(null, "", "", null)
        )
    }

    fun setStateIntent(stateIntent: StateIntent){
        when(stateIntent){
            is StateIntent.SetEvent -> {
                setEvent(stateIntent.event)
            }
            is StateIntent.SaveEvent -> {
                saveEvent(stateIntent.name, stateIntent.desc)
            }
            is StateIntent.UpdEvent -> {
                changeEvent(stateIntent.name, stateIntent.desc)
            }
            is StateIntent.UpdDateEvent -> {
                changeEventDate(stateIntent.date)
            }
            is StateIntent.DelEvent -> {
                delEvent()
            }
        }
    }

    private fun setEvent(event: Event){
        activeEvent = event;
        stateProvider.onNext(EventState.LoadSuccess(event))
    }

    private fun changeEvent(name : String, desc : String = ""){
        activeEvent?.name = name
        activeEvent?.desc = desc
    }

    private fun changeEventDate(date: DateTime){
        activeEvent?.date = date
    }

    private fun saveEvent(name : String, desc : String = ""){
        activeEvent?.let {
            it.name = name
            it.desc = desc
        }
        if(activeEvent?.id != null){
            changeEventsUseCase.updEvent(activeEvent!!).subscribe(
                {
                    stateProvider.onNext(EventState.SaveSuccess)
                    messageProvider.onNext(MessageType.SUCCESS_SAVE)
                },
                {
                    stateProvider.onNext(EventState.Error(it.localizedMessage?:""))
                    messageProvider.onNext(MessageType.FALL)
                }
            )
        } else {
            changeEventsUseCase.addEvent(activeEvent!!).subscribe(
                {
                    activeEvent?.id = it.toInt()
                    stateProvider.onNext(EventState.CreateSuccess)
                    messageProvider.onNext(MessageType.SUCCESS_ADD)
                },
                { messageProvider.onNext(MessageType.FALL) }
            )
        }
    }

    private fun delEvent() {
        changeEventsUseCase.delEvent(activeEvent!!).subscribe(
            {
                stateProvider.onNext(EventState.DeletedSuccess(activeEvent!!))
                messageProvider.onNext(MessageType.SUCCESS_DELETE)
            },
            {
                messageProvider.onNext(MessageType.FALL)
            }
        )
    }

    sealed class StateIntent {
        data class SetEvent(val event: Event) : StateIntent()
        data class SaveEvent(val name : String, val desc : String = "") : StateIntent()
        data class UpdEvent(val name : String, val desc : String = "") : StateIntent()
        data class UpdDateEvent(val date: DateTime) : StateIntent()
        object DelEvent : StateIntent()
    }
}