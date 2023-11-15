package com.gd05.brickr.model

import java.io.Serializable

/** Class that represents a brick of the Lego API **/
data class Brick(
    /** Brick's identification number **/
    val brickId: Int,
    /** Brick's readable name **/
    val name: String,
    /** Brick's category identification number **/
    val brickCatId: Int,
    /** Year when the Brick was released **/
    val yearFrom: Int,
    /** Year when the Brick was discontinued **/
    val yearTo: Int,
    /** Brick's Rebrickable URL **/
    val brickUrl: String,
    /** Brick's image URL **/
    val brickImgUrl: String,
    /** Brick amount in inventory **/
    val amount: Int
) : Serializable