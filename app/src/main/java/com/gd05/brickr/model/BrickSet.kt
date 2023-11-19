package com.gd05.brickr.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

/** Class that represents a brick set of the Lego API **/
@Entity(foreignKeys = [ForeignKey(entity = Theme::class,
    parentColumns = arrayOf("themeId"),
    childColumns = arrayOf("themeId"),
    onDelete = ForeignKey.CASCADE)])
data class BrickSet (
    /** Set's identification number **/
    @PrimaryKey
    var brickSetId: Int,

    /** Set's readable name **/
    var name: String,

    /** Set's year of release **/
    var year: Int,

    /** Set's theme identification number **/
    var themeId: Int,

    /** Set's number of bricks **/
    var numParts: Int,

    /** Set's image URL **/
    var setImgUrl: String,

    /** Set's Rebrickable URL **/
    var setUrl: String,

    /** Set's last modification date **/
    var lastModifiedDt: String,

    /** Indicates if the set has been added to favorites **/
    var isFavorite: Boolean = false

) : Serializable