package com.gd05.brickr.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/** Class that represents a Category for sets of the Lego API **/
@Entity
data class Theme(
    /** Theme's identification number **/
    @PrimaryKey
    val themeId: Int,
    /** Theme's readable name **/
    val themeName: String,
) : Serializable