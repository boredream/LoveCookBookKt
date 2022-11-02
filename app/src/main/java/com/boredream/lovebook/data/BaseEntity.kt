package com.boredream.lovebook.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
open class BaseEntity : Parcelable {

    open lateinit var id: String

}
