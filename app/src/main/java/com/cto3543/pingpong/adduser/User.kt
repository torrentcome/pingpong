package com.cto3543.pingpong.adduser

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by cto3543 on 18/04/2017.
 */
data class User(val sexe: Boolean = true, val email: String = "", val surname: String? = null, val score: Int = 0, val party: Int? = null, var key: String? = null) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(source: Parcel): User = User(source)
            override fun newArray(size: Int): Array<User?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            1.equals(source.readInt()),
            source.readString(),
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Int,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt((if (sexe) 1 else 0))
        dest?.writeString(email)
        dest?.writeString(surname)
        dest?.writeValue(score)
        dest?.writeValue(party)
        dest?.writeString(key)
    }

}