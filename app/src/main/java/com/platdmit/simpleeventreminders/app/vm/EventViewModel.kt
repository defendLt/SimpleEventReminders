package com.platdmit.simpleeventreminders.app.vm

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
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
) : BaseViewModel<EventViewModel.StateInstance>() {
    val messageLiveData = LiveDataReactiveStreams.fromPublisher(messageProvider)
    var activeEvent : Event? = null;
    var eventStateLiveData = MutableLiveData<EventState<Event>>()

    init {
        eventStateLiveData.value = EventState.Loading
    }

    fun setStateInstance(stateInstance: StateInstance){
        when(stateInstance){
            is StateInstance.SetEvent -> {
                setEvent(stateInstance.event)
            }
            is StateInstance.SaveEvent -> {
                saveEvent(stateInstance.name, stateInstance.desc)
            }
            is StateInstance.UpdEvent -> {
                changeEvent(stateInstance.name, stateInstance.desc)
            }
            is StateInstance.UpdDateEvent -> {
                changeEventDate(stateInstance.date)
            }
            is StateInstance.DelEvent -> {
                delEvent()
            }
        }
    }

    private fun setEvent(event: Event){
        activeEvent = event;
        eventStateLiveData.value = EventState.LoadSuccess(event)
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
                    eventStateLiveData.value = EventState.SaveSuccess
                    messageProvider.onNext(MessageType.SUCCESS_SAVE)
                },
                {
                    messageProvider.onNext(MessageType.FALL)
                    eventStateLiveData.value = EventState.Error(it.localizedMessage?:"")
                }
            )
        } else {
            changeEventsUseCase.addEvent(activeEvent!!).subscribe(
                {
                    activeEvent?.id = it.toInt()
                    eventStateLiveData.value = EventState.CreateSuccess
                    messageProvider.onNext(MessageType.SUCCESS_ADD)
                },
                { messageProvider.onNext(MessageType.FALL) }
            )
        }
    }

    private fun delEvent() {
        changeEventsUseCase.delEvent(activeEvent!!).subscribe(
            {
                eventStateLiveData.value = EventState.DeletedSuccess(activeEvent!!)
                messageProvider.onNext(MessageType.SUCCESS_DELETE)
            },
            {
                messageProvider.onNext(MessageType.FALL)
            }
        )
    }

    sealed class StateInstance {
        data class SetEvent(val event: Event) : StateInstance()
        data class SaveEvent(val name : String, val desc : String = "") : StateInstance()
        data class UpdEvent(val name : String, val desc : String = "") : StateInstance()
        data class UpdDateEvent(val date: DateTime) : StateInstance()
        object DelEvent : StateInstance()
    }
}