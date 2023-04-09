package com.klopoff.messenger_klopoff.HomeActivity.NewChatFragment

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

class FoundedPerson(
    var userId: String, var userName: String, var userAvatar: Bitmap?
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

    companion object CREATOR : Parcelable.Creator<FoundedPerson> {

        override fun createFromParcel(`in`: Parcel): FoundedPerson {
            val userId = `in`.readString()!!
            val userName = `in`.readString()!!
            val userAvatarExist = `in`.readInt() == 1
            val userAvatar = if (userAvatarExist) {
                Bitmap.CREATOR.createFromParcel(`in`)
            } else {
                null
            }
            return FoundedPerson(userId, userName, userAvatar)
        }

        override fun newArray(size: Int): Array<FoundedPerson?> {
            return arrayOfNulls(size)
        }
    }
}