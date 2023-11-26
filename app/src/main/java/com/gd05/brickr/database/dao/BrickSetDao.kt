package com.gd05.brickr.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gd05.brickr.model.Brick
import com.gd05.brickr.model.BrickSet

@Dao
interface BrickSetDao {

    //Limit 1 indica que solo toma un resultado, el que más se parezca
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(brickSet: BrickSet)

    @Delete
    suspend fun delete(brickSet: BrickSet)

    @Query("SELECT * FROM brickset WHERE name LIKE :first LIMIT 1")
    suspend fun findByName(first: String): BrickSet

    @Query("SELECT * FROM brickset WHERE isFavorite = 1")
    suspend fun findFavorites(): List<BrickSet>

}