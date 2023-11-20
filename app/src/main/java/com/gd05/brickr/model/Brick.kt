package com.gd05.brickr.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

/** Class that represents a brick of the Lego API **/

@Entity(foreignKeys = [ForeignKey(entity = Category::class,
    parentColumns = arrayOf("categoryId"),
    childColumns = arrayOf("categoryId"),
    onDelete = ForeignKey.CASCADE)]
)
data class Brick(
    /** Brick's identification number **/
    @PrimaryKey
    val brickId: String,

    /** Brick's readable name **/
    val name: String?,

    /** Brick's category identification number **/
    val categoryId: Int?,

    /** Year when the Brick was released **/
    val yearFrom: Int?,

    /** Year when the Brick was discontinued **/
    val yearTo: Int?,

    /** Brick's Rebrickable URL **/
    val brickUrl: String?,

    /** Brick's image URL **/
    val brickImgUrl: String?,

    /** Brick amount in inventory **/
    val amount: Int

) : Serializable