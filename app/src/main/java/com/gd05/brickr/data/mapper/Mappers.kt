package com.gd05.brickr.data.mapper

import com.gd05.brickr.data.api.ApiCategorie
import com.gd05.brickr.data.api.ApiSet
import com.gd05.brickr.data.api.ApiTheme
import com.gd05.brickr.data.api.BrickApi
import com.gd05.brickr.model.Brick
import com.gd05.brickr.model.BrickSet
import com.gd05.brickr.model.Category
import com.gd05.brickr.model.Theme

fun BrickApi.toBrick(): Brick {
    return Brick(
        brickId = partNum,
        name = name,
        categoryId = partCatId,
        brickUrl = partUrl,
        brickImgUrl = partImgUrl,
        amount = 0,
        yearFrom = yearFrom,
        yearTo = yearTo
    )
}

fun ApiSet.toSet(): BrickSet {
    return BrickSet(
        brickSetId = setNum,
        name = name,
        year = year,
        themeId = themeId,
        numParts = numParts,
        setImgUrl = setImgUrl,
        setUrl = setUrl,
        lastModifiedDt = lastModifiedDt,
        // TODO: Cuando se cargue la lista, habrá que comprobar si el set ha sido marcado como favorito o no
        isFavorite = false
    )
}

fun ApiTheme.toTheme(): Theme {
    return Theme(themeId = id, themeName = name)
}

fun ApiCategorie.toCategory(): Category {
    return Category(
        categoryId = id,
        categoryName = name
    )
}