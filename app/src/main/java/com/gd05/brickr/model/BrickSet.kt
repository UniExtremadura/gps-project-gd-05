package com.gd05.brickr.model

import java.io.Serializable

/** Class that represents a brick set of the Lego API **/
data class BrickSet (
    /** Set's identification number **/
    var setId: Int,
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
    /** Set's list of bricks **/
    var bricks: List<Brick> = emptyList(),
    /** Indicates if the set has been added to favorites **/
    var isFavorite: Boolean = false
) : Serializable