package com.platdmit.simpleeventreminders.domains.model

import android.os.Parcel
import android.os.Parcelable
import org.joda.time.DateTime

data class Event(
    var id: Int?,
    var name: String,
    var desc: String,
    var date: DateTime? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString() ?:"",
        parcel.readString() ?:"",
        DateTime(parcel.readValue(Long::class.java.classLoader) as? Long)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeString(desc)
        parcel.writeValue(date?.millis)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Event> {
        override fun createFromParcel(parcel: Parcel): Event {
            return Event(parcel)
        }

        override fun newArray(size: Int): Array<Event?> {
            return arrayOfNulls(size)
        }
    }

}