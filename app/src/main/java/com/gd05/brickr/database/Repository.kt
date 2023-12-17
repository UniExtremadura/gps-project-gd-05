package com.gd05.brickr.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.gd05.brickr.api.RebrickableAPI
import com.gd05.brickr.api.RebrickableService
import com.gd05.brickr.data.api.BricksRequest
import com.gd05.brickr.data.api.CategoriesRequest
import com.gd05.brickr.data.api.SearchRequest
import com.gd05.brickr.data.api.ThemesRequest
import com.gd05.brickr.data.mapper.toBrick
import com.gd05.brickr.data.mapper.toCategory
import com.gd05.brickr.data.mapper.toSet
import com.gd05.brickr.data.mapper.toTheme
import com.gd05.brickr.database.dao.BrickDao
import com.gd05.brickr.database.dao.BrickSetDao
import com.gd05.brickr.database.dao.CategoryDao
import com.gd05.brickr.database.dao.ThemeDao
import com.gd05.brickr.model.Brick
import com.gd05.brickr.model.BrickSet
import com.gd05.brickr.model.Category
import com.gd05.brickr.model.Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Repository(
    private val brickDao: BrickDao,
    private val setDao: BrickSetDao,
    private val categoryDao: CategoryDao,
    private val themeDao: ThemeDao,
    private val networkService: RebrickableService
) {

    var BricksOfBrickSet:   MutableLiveData<List<Brick>> = MutableLiveData()
    var categories:         LiveData<List<Category>> = categoryDao.getLiveDataCategories()
    var themes:             LiveData<List<Theme>> = themeDao.getLiveDataThemes()

    private var queryBrick: MutableLiveData<String> = MutableLiveData()

    var searchedBricks: LiveData<List<Brick>> = queryBrick.switchMap { query ->
        brickDao.getLiveDataBricksByName(query)
    }

    var searchedSets: LiveData<List<BrickSet>> = queryBrick.switchMap { query ->
        setDao.getLiveDataSetsByName(query)
    }

    var favoriteSets: LiveData<List<BrickSet>> = setDao.getLiveDataFavoriteSets()

    var inventoryBricks: LiveData<List<Brick>> = brickDao.getLiveDataInventoryBricks()


    //Métodos del fragmento de búsqueda
    suspend fun publicSearchBricks(query: String){
        queryBrick.value = query
        withContext(Dispatchers.IO) {
            val request = BricksRequest(search = query)

            networkService.searchBricks(request).execute().body()?.let {
                 brickDao.insertAll(it.results.map { apiBrick -> apiBrick.toBrick() })
            }
        }
    }

    suspend fun publicSearchBrickSets(query: String){
        queryBrick.value = query
        withContext(Dispatchers.IO) {
            val request = SearchRequest(search = query)

            networkService.searchSet(request).execute().body()?.let {
                setDao.insertAll(it.results.map { apiSet -> apiSet.toSet() })
            }
        }
    }

    suspend fun publicGetBricksOfBrickSet(brickSetId: String) {
        getBricksOfBrickSet(brickSetId)
    }

    private suspend fun getBricksOfBrickSet(brickSetId: String) {

    }


    //Métodos del theme y category
    suspend fun publicGetCategories() {
        getCategories()
    }
    private suspend fun getCategories() {
        withContext(Dispatchers.IO) {
            val request = CategoriesRequest()
            networkService.getCategories(request).execute().body()?.let {
                categoryDao.insertAll(it.results.map { cat -> cat.toCategory() })
            }
        }
    }

    suspend fun publicGetCategoryName(categoryId: Int): Category? {
        return getCategoryName(categoryId)
    }
    private suspend fun getCategoryName(categoryId: Int): Category? {
        return categoryDao.getCategoryById(categoryId)
    }

    suspend fun publicGetThemes() {
        getThemes()
    }
    private suspend fun getThemes() {
        withContext(Dispatchers.IO) {
            val request = ThemesRequest(1, 1000)
            networkService.getThemes(request).execute().body()?.let {
                themeDao.insertAll(it.results.map { theme -> theme.toTheme() })
            }
        }
    }

    suspend fun publicGetThemeName(themeId: Int): Theme? {
        return getThemeName(themeId)
    }
    private suspend fun getThemeName(themeId: Int): Theme? {
        return themeDao.getThemeById(themeId)
    }

    //Métodos del fragmento inventario

    fun publicGetInventoryBricks() {
        inventoryBricks = brickDao.getLiveDataInventoryBricks()
    }

    fun publicGetFilteredInventoryBricks(selectedCategory: Int) {
        inventoryBricks = brickDao.getLiveDataFilteredInventoryBricks(selectedCategory)
    }

    fun publicGetSearchedInventoryBricks(query: String) {
        inventoryBricks = brickDao.getLiveDataSearchedInventoryBricks(query)
    }

    fun publicGetSearchedFilteredInventoryBricks(query: String, selectedCategory: Int) {
        inventoryBricks = brickDao.getLiveDataSearchedFilteredInventoryBricks(query, selectedCategory)
    }

    suspend fun publicInsertBrick(brick: Brick) {
        insertBrick(brick)
    }

    private suspend fun insertBrick(brick: Brick) {
        brickDao.insert(brick)
    }

    suspend fun publicGetBrickById(brickId: String): Brick {
        return getBrickById(brickId)
    }
    private suspend fun getBrickById(brickId: String): Brick {
        return brickDao.findById(brickId)
    }

    suspend fun publicGetBrickSetById(brickSetId: String): BrickSet {
        return getBrickSetById(brickSetId)
    }

    private suspend fun getBrickSetById(brickSetId: String): BrickSet{
        return setDao.findById(brickSetId)
    }

    suspend fun publicInsertBrickSet(brickSet: BrickSet) {
        insertBrickSet(brickSet)
    }
    private suspend fun insertBrickSet(brickSet: BrickSet) {
        setDao.insert(brickSet)
    }

    companion object {
    }
}
