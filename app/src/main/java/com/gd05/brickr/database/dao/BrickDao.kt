package com.gd05.brickr.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.gd05.brickr.model.Brick
import com.gd05.brickr.model.BrickSetBrickCrossRef
import com.gd05.brickr.model.BrickSetWithBricks

@Dao
interface BrickDao {

    @Query("SELECT * FROM brick")
    suspend fun getAllBricks(): List<Brick>

    @Query("SELECT * FROM brick WHERE amount > 0")
    suspend fun getInventoryBricks(): List<Brick>

    @Query("SELECT * FROM brick WHERE brickId = :id")
    suspend fun findById(id: String): Brick

    //OnConflictStrategy.REPLACE indica que si se intenta añadir un elemento con la misma clave primaria
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(brick: Brick)

    @Delete
    suspend fun delete(show: Brick)

    @Transaction
    @Query("SELECT * FROM BrickSet where brickSetId = :brickSetId")
    suspend fun getBrickSetWithBricks(brickSetId: String): BrickSetWithBricks

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBrickSetBrick(crossRef: BrickSetBrickCrossRef)

    //Inserta una pieza y la relaciona con un brickset, transaccion para que se haga las dos cosas o ninguna
    @Transaction
    suspend fun insertAndRelate(brick: Brick, setId: String) {
        insert(brick)
        insertBrickSetBrick(BrickSetBrickCrossRef(setId, brick.brickId))
    }

}