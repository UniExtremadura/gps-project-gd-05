package com.gd05.brickr.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gd05.brickr.model.Theme

@Dao
interface ThemeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTheme(theme: Theme)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(themes: List<Theme>)

    @Query("SELECT * FROM Theme WHERE themeId = :themeId")
    suspend fun getThemeById(themeId: Int): Theme?

    @Query("SELECT * FROM Theme")
    suspend fun getAllThemes(): List<Theme>


    @Query("DELETE FROM Theme")
    suspend fun deleteAllThemes()

}