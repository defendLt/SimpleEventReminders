package com.platdmit.simpleeventreminders.app.vm

import androidx.lifecycle.ViewModel
import com.platdmit.simpleeventreminders.domains.model.Event
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.processors.BehaviorProcessor

abstract class BaseViewModel : ViewModel() {
    protected val compositeDisposable = CompositeDisposable()
    protected val contentProvider: BehaviorProcessor<List<Event>> = BehaviorProcessor.create<List<Event>>()
    protected val messageProvider: BehaviorProcessor<MessageType> = BehaviorProcessor.create<MessageType>()

    override fun onCleared() {
        super.onCleared()
        contentProvider.onComplete()
        messageProvider.onComplete()
        compositeDisposable.clear()
    }

    enum class MessageType {
        SUCCESS_SAVE,
        SUCCESS_ADD,
        SUCCESS_DELETE,
        SUCCESS_LOAD,
        FALL,
        EMPTY
    }
}