package com.example.todolistkotlin.model

import android.os.Parcel
import android.os.Parcelable

data class ItemClass(val Title: String?, val Description: String?, val Date: String?) : Parcelable
{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )



    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(Title)
        parcel.writeString(Description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ItemClass> {
        override fun createFromParcel(parcel: Parcel): ItemClass {
            return ItemClass(parcel)
        }

        override fun newArray(size: Int): Array<ItemClass?> {
            return arrayOfNulls(size)
        }
    }

}