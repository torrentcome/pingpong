package com.cto3543.pingpong.addmatch

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by cto3543 on 03/05/2017.
 */
data class Match(val mRef1: String? = null, val mRef2: String? = null, val score1: Int = 0, val score2: Int = 0, val timestamp: Long) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Match> = object : Parcelable.Creator<Match> {
            override fun createFromParcel(source: Parcel): Match = Match(source)
            override fun newArray(size: Int): Array<Match?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Int,
            source.readValue(Int::class.java.classLoader) as Int,
            source.readLong()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(mRef1)
        dest.writeString(mRef2)
        dest.writeValue(score1)
        dest.writeValue(score2)
        dest.writeLong(timestamp)
    }
}