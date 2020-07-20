package com.platdmit.simpleeventreminders.app.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy
import com.google.android.material.snackbar.Snackbar
import com.platdmit.simpleeventreminders.R
import com.platdmit.simpleeventreminders.app.adapters.EventsListAdapter
import com.platdmit.simpleeventreminders.app.fragments.helpers.OnShowMessageFragment
import com.platdmit.simpleeventreminders.app.fragments.helpers.OnUpdateRecyclerView
import com.platdmit.simpleeventreminders.app.vm.BaseViewModel
import com.platdmit.simpleeventreminders.app.vm.EventsListViewModel
import com.platdmit.simpleeventreminders.domains.model.Event
import com.platdmit.simpleeventreminders.domains.state.EventListState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_events.*

@AndroidEntryPoint
class EventsListFragment : Fragment(R.layout.fragment_events),
    OnShowMessageFragment,
    OnUpdateRecyclerView
{
    private val eventsListAdapter : EventsListAdapter = EventsListAdapter()
    private val eventsListViewModel: EventsListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_events, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.events_refresh -> {
                updateElementsList()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        events_list.layoutManager = LinearLayoutManager(context)

        eventsListAdapter.stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY

        eventsListViewModel.eventsListStateLiveData.observe(viewLifecycleOwner, Observer { stateHandler(it) })

        eventsListViewModel.messageLiveData.observe(viewLifecycleOwner, Observer { statusMessageHandler(it) })

        parentFragmentManager.setFragmentResultListener(
            EventFragment.REQUEST_KEY, this
        ) { requestKey: String, result: Bundle ->
            val deletedItemId = result.getInt(EventFragment.SUCCESS_KEY)
            eventsListAdapter.deletedItem(deletedItemId)
            println("setFragmentResultListener $requestKey: $deletedItemId")
        }
    }

    private fun stateHandler(eventListState: EventListState<List<Event>>){
        when(eventListState){
            is EventListState.Loading -> {
                showMessage(requireContext().getString(R.string.status_message_load))
            }
            is EventListState.Success<List<Event>> -> {
                bindEventsData(eventListState.data)
                showMessage(requireContext().getString(R.string.status_message_ok_load))
            }
            is EventListState.Empty -> {
                showMessage(requireContext().getString(R.string.status_message_empty))
            }
            is EventListState.Error -> {
                showMessage(requireContext().getString(R.string.status_message_fall))
            }
        }
    }

    private fun setStateIntent(stateIntent: EventsListViewModel.StateIntent){
        eventsListViewModel.setStateIntent(stateIntent)
    }

    private fun bindEventsData(events : List<Event>){
        eventsListAdapter.setContentData(events)
        if(events_list.adapter == null){
            events_list.adapter = eventsListAdapter
        }
    }

    private fun statusMessageHandler(messageType: BaseViewModel.MessageType){
        when(messageType){
            BaseViewModel.MessageType.SUCCESS_LOAD -> showMessage(requireContext().getString(R.string.status_message_ok_load))
            BaseViewModel.MessageType.FALL -> showMessage(requireContext().getString(R.string.status_message_fall))
        }
    }

    override fun showMessage(message : String){
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    }

    override fun updateElementsList() {
        setStateIntent(
            EventsListViewModel.StateIntent.RefreshResult
        )
    }
}