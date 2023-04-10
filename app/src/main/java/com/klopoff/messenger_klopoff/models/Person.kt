package com.klopoff.messenger_klopoff.models

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

class Person(
    var userId: String,
    var userName: String,
    var userAvatar: Bitmap?
) : Parcelable {

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(userId)
        dest.writeString(userName)
        dest.writeInt(if (userAvatar != null) 1 else 0)
        userAvatar?.writeToParcel(dest, flags)
    }

    companion object CREATOR : Parcelable.Creator<Person> {

        override fun createFromParcel(`in`: Parcel): Person {
            val userId = `in`.readString()!!
            val userName = `in`.readString()!!
            val userAvatarExist = `in`.readInt() == 1
            val userAvatar = if (userAvatarExist) { Bitmap.CREATOR.createFromParcel(`in`) } else { null }
            return Person(userId, userName, userAvatar)
        }

        override fun newArray(size: Int): Array<Person?> {
            return arrayOfNulls(size)
        }
    }
}