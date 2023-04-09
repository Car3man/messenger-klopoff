package com.klopoff.messenger_klopoff.HomeActivity.ChatFragment

import android.os.Parcel
import android.os.Parcelable

class ChatFragmentState(
    var userId: String, var messages: MutableList<ChatMessage>
) : Parcelable {

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(userId)
    }

    companion object CREATOR : Parcelable.Creator<ChatFragmentState> {
        override fun createFromParcel(`in`: Parcel): ChatFragmentState {
            val userId = `in`.readString()!!
            val messagesCount = `in`.readInt()
            val messagePlaceholder = ChatMessage(true, "", 0L)
            val messages = MutableList(messagesCount) { messagePlaceholder }
            for (i in 0..messagesCount) {
                val message = ChatMessage.createFromParcel(`in`)
                message.let {
                    messages.add(it)
                }
            }
            return ChatFragmentState(userId, messages)
        }

        override fun newArray(size: Int): Array<ChatFragmentState?> {
            return arrayOfNulls(size)
        }
    }
}