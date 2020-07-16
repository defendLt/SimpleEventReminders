package com.platdmit.simpleeventreminders.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.platdmit.simpleeventreminders.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_date_picker.*
import org.joda.time.DateTime

@AndroidEntryPoint
class DatePickerFragment: DialogFragment() {

    interface NoticeDialogResult {
        fun onOkResultClick(date : DateTime)
    }

    private var date : DateTime = DateTime.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (arguments?.get("DATA") as? DateTime)?.let {
            date = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = layoutInflater.inflate(R.layout.fragment_date_picker, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        date.let {
            event_date_picker.init(date.year, date.monthOfYear, date.dayOfMonth, null)
        }

        event_date_confirm.setOnClickListener {
            date = DateTime(event_date_picker.year, event_date_picker.month, event_date_picker.dayOfMonth, 0, 0)

            (parentFragment as? NoticeDialogResult)?.onOkResultClick(date)

            dismiss()
        }

        event_date_cancel.setOnClickListener {
            dismiss()
        }
    }
}