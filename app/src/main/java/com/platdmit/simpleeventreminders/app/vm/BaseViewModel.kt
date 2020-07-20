package com.platdmit.simpleeventreminders.app.vm

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.processors.BehaviorProcessor

abstract class BaseViewModel<T> : ViewModel() {
    protected val compositeDisposable = CompositeDisposable()
    protected val stateProvider: BehaviorProcessor<T> = BehaviorProcessor.create<T>()
    protected val messageProvider: BehaviorProcessor<MessageType> = BehaviorProcessor.create<MessageType>()

    override fun onCleared() {
        super.onCleared()
        stateProvider.onComplete()
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