package com.gd05.brickr.util

import android.content.Context
import com.gd05.brickr.api.RebrickableService
import com.gd05.brickr.database.BrickrDatabase
import com.gd05.brickr.database.Repository

class AppContainer(context: Context?) {
    private val networkService = RebrickableService
    private val db = context?.let { BrickrDatabase.getInstance(it) }!!
    val repository = Repository(
    db.brickDao(),
    db.brickSetDao(),
    db.categoryDao(),
    db.themeDao(),
    RebrickableService
    )
}
