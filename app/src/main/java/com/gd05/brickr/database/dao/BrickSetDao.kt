package com.gd05.brickr.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.gd05.brickr.model.BrickSet

@Dao
interface BrickSetDao {

    //Limit 1 indica que solo toma un resultado, el que más se parezca
    @Query("SELECT * FROM brickset WHERE name LIKE :first LIMIT 1")
    suspend fun findByName(first: String): BrickSet

}