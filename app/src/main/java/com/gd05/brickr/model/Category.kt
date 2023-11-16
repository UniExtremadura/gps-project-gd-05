package com.gd05.brickr.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/** Class that represents a Theme for the pieces of the Lego API **/
@Entity
data class Category(
    /** Theme's identification number **/
    @PrimaryKey
    val categoryId: Int,
    /** Theme's readable name **/
    val categoryName: String,
) : Serializable