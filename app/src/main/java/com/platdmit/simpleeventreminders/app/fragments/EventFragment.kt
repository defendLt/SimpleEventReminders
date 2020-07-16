package com.platdmit.simpleeventreminders.app.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.platdmit.simpleeventreminders.R
import com.platdmit.simpleeventreminders.app.fragments.helpers.OnShowMessageFragment
import com.platdmit.simpleeventreminders.app.helpers.ActionButtonControl
import com.platdmit.simpleeventreminders.app.vm.BaseViewModel
import com.platdmit.simpleeventreminders.app.vm.EventViewModel
import com.platdmit.simpleeventreminders.domains.model.Event
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_event.*
import org.joda.time.DateTime

@AndroidEntryPoint
class EventFragment : Fragment(R.layout.fragment_event),
    OnShowMessageFragment,
    DatePickerFragment.NoticeDialogResult
{
    private val eventViewModel: EventViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hideActivityButton(false)

        setHasOptionsMenu(true)

        if (savedInstanceState == null){
            //Set active Event else new Event
            eventViewModel.setEvent(
                (arguments?.getParcelable("EVENT") as? Event)
                    ?: Event(
                        null, resources.getString(R.string.event_name),
                        resources.getString(R.string.event_desc), null
                    )
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        eventViewModel.messageLiveData.observe(viewLifecycleOwner, Observer { statusMessageHandler(it) })
        eventViewModel.eventLiveData.observe(viewLifecycleOwner, Observer { bindEventData(it) })

        eventDate.setOnClickListener {
            val dialogFragment = DatePickerFragment()
            dialogFragment.show(childFragmentManager, "DIALOG_DATE")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_event, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.event_action_save -> {
                eventViewModel.saveEvent(eventName.text.toString(), eventDesc.text.toString())
                true
            }
            R.id.event_action_deleted -> {
                eventViewModel.delEvent()
                parentFragmentManager.popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStop() {
        super.onStop()
        //Save data before change screen orientation
        eventViewModel.changeEvent(eventName.text.toString(), eventDesc.text.toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        hideActivityButton(true)
    }

    private fun bindEventData(event : Event){
        eventName.setText(event.name)
        eventDesc.setText(event.desc)
        event.date?.let {
            setStringDate(it)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setStringDate(date: DateTime){
        eventDate.text = "${date.dayOfMonth}:${date.monthOfYear}:${date.year}"
    }

    private fun hideActivityButton(status : Boolean){
        (activity as? ActionButtonControl)?.actionButtonVisible(status)
    }

    private fun statusMessageHandler(messageType: BaseViewModel.MessageType){
        when(messageType){
            BaseViewModel.MessageType.SUCCESS_SAVE -> showMessage(requireContext().getString(R.string.status_message_ok_save))
            BaseViewModel.MessageType.SUCCESS_ADD -> showMessage(requireContext().getString(R.string.status_message_ok_add))
            BaseViewModel.MessageType.SUCCESS_DELETE -> showMessage(requireContext().getString(R.string.status_message_ok_delete))
            else -> showMessage(requireContext().getString(R.string.status_message_fall))
        }
    }

    override fun showMessage(message : String){
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    }

    override fun onOkResultClick(date: DateTime) {
        eventViewModel.changeDate(date)
        setStringDate(date)
    }
}