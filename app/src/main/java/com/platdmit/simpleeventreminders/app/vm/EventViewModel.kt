package com.platdmit.simpleeventreminders.app.vm

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.platdmit.simpleeventreminders.domains.model.Event
import com.platdmit.simpleeventreminders.domains.utilities.events.ChangeEventsUseCase
import org.joda.time.DateTime

class EventViewModel
@ViewModelInject
constructor(
    private val changeEventsUseCase: ChangeEventsUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    val messageLiveData = LiveDataReactiveStreams.fromPublisher(messageProvider)
    var eventLiveData = MutableLiveData<Event>()

    fun setEvent(event: Event){
        eventLiveData.postValue(event)
    }

    fun changeEvent(name : String, desc : String = ""){
        eventLiveData.value?.name = name
        eventLiveData.value?.desc = desc
    }

    fun changeDate(date: DateTime){
        eventLiveData.value?.date = date
    }

    fun saveEvent(name : String, desc : String = ""){
        eventLiveData.value?.let {
            it.name = name
            it.desc = desc
        }
        if(eventLiveData.value?.id != null){
            changeEventsUseCase.updEvent(eventLiveData.value!!).subscribe(
                { messageProvider.onNext(MessageType.SUCCESS_SAVE) },
                { messageProvider.onNext(MessageType.FALL) }
            )
        } else {
            changeEventsUseCase.addEvent(eventLiveData.value!!).subscribe(
                {
                    eventLiveData.value?.id = it.toInt()
                    messageProvider.onNext(MessageType.SUCCESS_ADD)
                },
                { messageProvider.onNext(MessageType.FALL) }
            )
        }
    }

    fun delEvent() {
        changeEventsUseCase.delEvent(eventLiveData.value!!).subscribe(
            { messageProvider.onNext(MessageType.SUCCESS_DELETE) },
            { messageProvider.onNext(MessageType.FALL) }
        )
    }
}