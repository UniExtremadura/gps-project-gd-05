package com.gd05.brickr.database.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    //Funciones CRUD

    @Query("SELECT * FROM brick WHERE brickId = :id ORDER BY name ASC")
    suspend fun findById(id: String): Brick

    //OnConflictStrategy.REPLACE indica que si se intenta añadir un elemento con la misma clave primaria, el elemento nuevo remplazará al anterior
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(brick: Brick)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(bricks: List<Brick>)

    @Delete
    suspend fun delete(show: Brick)

    //Funciones para repository
    @Query("SELECT * FROM brick")
    fun getBricks(): LiveData<List<Brick>>

    @Query("SELECT * FROM brick WHERE amount > 0 AND name LIKE :name || '%' ORDER BY name ASC")
    fun getLiveDataSearchedBricks(name: String): LiveData<List<Brick>>

    @Query("SELECT * FROM brick WHERE name LIKE '%' || :name || '%' ORDER BY name ASC")
    suspend fun getBricksByName(name: String): List<Brick>



    //Funciones para inventario

    @Query("SELECT * FROM brick WHERE amount > 0 ORDER BY name ASC")
    suspend fun getInventoryBricks(): List<Brick>

    @Query("SELECT * FROM brick WHERE amount > 0 AND categoryId = :category ORDER BY name ASC")
    suspend fun getFilteredInventoryBricks(category: Int): List<Brick>

    @Query("SELECT * FROM brick WHERE amount > 0 AND name LIKE :name || '%' ORDER BY name ASC")
    suspend fun getSearchedInventoryBricks(name: String): List<Brick>

    @Query("SELECT * FROM brick WHERE amount > 0 AND name LIKE :query || '%' AND (:category IS NULL OR categoryId = :category) ORDER BY name ASC")
    suspend fun getSearchedFilteredInventoryBricks(query: String, category: Int?): List<Brick>


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