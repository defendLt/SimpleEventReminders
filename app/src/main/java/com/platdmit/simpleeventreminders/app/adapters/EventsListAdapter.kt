package com.platdmit.simpleeventreminders.app.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.platdmit.simpleeventreminders.R
import com.platdmit.simpleeventreminders.domains.model.Event

class EventsListAdapter : RecyclerView.Adapter<EventsListAdapter.EventsListViewHolder>() {
    private var eventList : MutableList<Event> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsListViewHolder {
        val layoutType = R.layout.fragment_events_item
        return EventsListViewHolder(LayoutInflater.from(parent.context).inflate(layoutType, parent, false))
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    override fun onBindViewHolder(holder: EventsListViewHolder, position: Int) {
        holder.bindData(eventList[position])
    }

    fun setContentData(events : List<Event>){
        eventList.clear()
        eventList.addAll(events)
        notifyDataSetChanged()
    }

    fun delItem(id: Int){
        val index = eventList.indexOfFirst { it.id == id }
        eventList.removeAt(index)
        notifyItemRemoved(index)
    }

    fun addItem(event: Event){
        eventList.add(event)
        notifyItemInserted(eventList.lastIndex)
    }

    inner class EventsListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val name = itemView.findViewById<TextView>(R.id.event_title)
        private val time = itemView.findViewById<TextView>(R.id.event_date)
        private val changeButton = itemView.findViewById<TextView>(R.id.event_change)
        @SuppressLint("SetTextI18n")
        fun bindData(event : Event){
            name.text = event.name
            event.date?.let {
                time.text = "${event.date?.dayOfMonth}:${event.date?.monthOfYear}:${event.date?.year}"
            }
            changeButton.setOnClickListener{
                Navigation.findNavController(it).navigate(R.id.eventFragment, bundleOf("EVENT" to event))
            }
        }
    }
}