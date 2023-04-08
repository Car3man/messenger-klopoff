package com.klopoff.messenger_klopoff.HomeActivity.ChatsFragment

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import com.klopoff.messenger_klopoff.HomeActivity.ChatFragment.ChatMessage

class Chat(
    val userId: String,
    val userName: String,
    val userAvatar: Bitmap?,
    val lastMessage: ChatMessage?
): Parcelable {
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(userId)
        dest.writeString(userName)
        dest.writeInt(if (userAvatar != null) 1 else 0)
        userAvatar?.writeToParcel(dest, flags)
        dest.writeInt(if (lastMessage != null) 1 else 0)
        lastMessage?.writeToParcel(dest, flags)
    }

    companion object CREATOR: Parcelable.Creator<Chat> {
        override fun createFromParcel(`in`: Parcel): Chat {
            val userId = `in`.readString()!!
            val userName = `in`.readString()!!
            val userAvatarExist = `in`.readInt() == 1
            val userAvatar = if (userAvatarExist) {
                Bitmap.CREATOR.createFromParcel(`in`)
            } else {
                null
            }
            val lastMessageExist = `in`.readInt() == 1
            val lastMessage = if (lastMessageExist) {
                ChatMessage.createFromParcel(`in`)
            } else {
                null
            }
            return Chat(userId, userName, userAvatar, lastMessage)
        }

        override fun newArray(size: Int): Array<Chat?> {
            return arrayOfNulls(size)
        }
    }
}