package com.gd05.brickr.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["brickSetId", "brickId"],
    //Indicamos que son claves externas
    foreignKeys = [
        ForeignKey(
            //Indicamos que la clave externa esta en la tabla brick, esto se hace para el borrado
            entity = Brick::class,
            parentColumns = ["brickId"],
            childColumns = ["brickId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class BrickSetBrickCrossRef(
    val brickSetId: Int,
    val brickId: Int
)