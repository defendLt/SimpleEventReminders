package com.platdmit.simpleeventreminders.app.vm.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class EventsViewModelFactory<UseCase>
@Inject
constructor(
private val useCase : UseCase,
private val useCaseInterface : Class<UseCase>
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return try {
            modelClass.getConstructor(useCaseInterface).newInstance(useCase)
        } catch (e: Exception) {
            try {
                modelClass.newInstance()
            } catch (g: InstantiationException) {
                throw RuntimeException("Cannot create an instance of $modelClass", g)
            } catch (g: IllegalAccessException) {
                throw RuntimeException("Cannot create an instance of $modelClass", g)
            }
        }
    }
}