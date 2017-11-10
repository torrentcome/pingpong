package com.cto3543.pingpong.match

import android.os.Parcel
import android.os.Parcelable

data class Match(val mref1: String = "", val mref2: String = "", val score1: Int = 0, val score2: Int = 0, val timestamp: Long = -1) : Parcelable {
    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Match> = object : Parcelable.Creator<Match> {
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
        dest.writeString(mref1)
        dest.writeString(mref2)
        dest.writeValue(score1)
        dest.writeValue(score2)
        dest.writeLong(timestamp)
    }

}