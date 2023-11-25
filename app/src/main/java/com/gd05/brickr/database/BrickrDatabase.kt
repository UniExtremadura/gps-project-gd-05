package com.gd05.brickr.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gd05.brickr.database.dao.BrickDao
import com.gd05.brickr.database.dao.BrickSetDao
import com.gd05.brickr.database.dao.CategoryDao
import com.gd05.brickr.database.dao.ThemeDao
import com.gd05.brickr.model.Brick
import com.gd05.brickr.model.BrickSet
import com.gd05.brickr.model.BrickSetBrickCrossRef
import com.gd05.brickr.model.Category
import com.gd05.brickr.model.Theme


//TODO Database indica que la clase es una base de datos
//Debemos listar todas las clases de datos que se utilizan en nuestra base de datos
@Database(entities = [Brick::class, BrickSet::class, Category::class, Theme::class, BrickSetBrickCrossRef::class], version = 1)
abstract class BrickrDatabase : RoomDatabase() {
    //metodos para acceder a las clases DAO
    abstract fun brickDao(): BrickDao
    abstract fun brickSetDao(): BrickSetDao
    abstract fun categoryDao(): CategoryDao
    abstract fun themeDao(): ThemeDao


    companion object {
        private var INSTANCE: BrickrDatabase? = null

        fun getInstance(context: Context): BrickrDatabase? {
            if (INSTANCE == null) {
                synchronized(BrickrDatabase::class) {
                    //Usamos singletown para crear una unica clase de base de datos y debemos pasar el CONTEXTO
                    INSTANCE = Room.databaseBuilder(
                        context,
                        BrickrDatabase::class.java, "brickr.db"
                    ).build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
