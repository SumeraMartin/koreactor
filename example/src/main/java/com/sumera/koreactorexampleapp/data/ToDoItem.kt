package com.sumera.koreactorexampleapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class ToDoItem(
		val id: Int,
		val title: String
) : Parcelable