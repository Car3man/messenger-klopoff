package com.klopoff.messenger_klopoff.HomeActivity.ChatsFragment

import android.os.Parcel
import android.os.Parcelable

class ChatFragmentState(

) : Parcelable {
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {

    }

    companion object CREATOR : Parcelable.Creator<ChatFragmentState> {
        override fun createFromParcel(source: Parcel?): ChatFragmentState {
            return ChatFragmentState()
        }

        override fun newArray(size: Int): Array<ChatFragmentState?> {
            return arrayOfNulls(size)
        }
    }
}