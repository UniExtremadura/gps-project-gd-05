package com.gd05.brickr.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

class BrickSetWithBricks (
    @Embedded val brickSet: BrickSet,
    @Relation(
        //Indicamos que la clave primaria de BrickSet es brickSetId
        parentColumn = "brickSetId",
        //Indicamos que la clave primaria de Brick es brickId
        entityColumn = "brickId",
        //Indicamos que la tabla intermedia es BrickSetBrickCrossRef
        associateBy = Junction(BrickSetBrickCrossRef::class)
    )
    val shows: List<Brick>

)