package com.boredream.lovebook.base

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class BaseEntity : java.io.Serializable {

    @PrimaryKey var id: String? = null

}
