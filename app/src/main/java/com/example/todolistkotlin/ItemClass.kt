package com.example.todolistkotlin

import android.os.Parcel
import android.os.Parcelable

data class ItemClass(val Name: String?, val Surname: String?): Parcelable
{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(Name)
        parcel.writeString(Surname)
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