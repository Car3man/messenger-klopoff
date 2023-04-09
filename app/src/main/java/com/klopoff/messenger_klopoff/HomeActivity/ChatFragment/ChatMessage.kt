package com.klopoff.messenger_klopoff.HomeActivity.ChatFragment

import android.os.Parcel
import android.os.Parcelable

class ChatMessage(
    var mine: Boolean, var message: String, var createdAt: Long
) : Parcelable {

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(if (mine) 1 else 0)
        dest.writeString(message)
        dest.writeLong(createdAt)
    }

    companion object CREATOR : Parcelable.Creator<ChatMessage> {
        override fun createFromParcel(`in`: Parcel): ChatMessage {
            val mine = `in`.readInt() == 1
            val message = `in`.readString()!!
            val createdAt = `in`.readLong()
            return ChatMessage(mine, message, createdAt)
        }

        override fun newArray(size: Int): Array<ChatMessage?> {
            return arrayOfNulls(size)
        }
    }
}