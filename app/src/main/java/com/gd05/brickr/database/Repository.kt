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


class Repository private constructor(
    private val brickDao: BrickDao,
    private val setDao: BrickSetDao,
    private val categoryDao: CategoryDao,
    private val themeDao: ThemeDao,
    private val networkService: RebrickableService
) {
    var searchedBricks:     MutableLiveData<List<Brick>> = MutableLiveData()
    var searchedSets:       MutableLiveData<List<BrickSet>> = MutableLiveData()
    var BricksOfBrickSet:   MutableLiveData<List<Brick>> = MutableLiveData()
    var favoriteSets:       MutableLiveData<List<BrickSet>> = MutableLiveData()
    var inventoryBricks:    MutableLiveData<List<Brick>> = MutableLiveData()
    var categories:         MutableLiveData<List<Category>> = MutableLiveData()
    var themes:             MutableLiveData<List<Theme>> = MutableLiveData()


    //Métodos del fragmento de búsqueda
    suspend fun publicSearchBricks(query: String){
        searchBricks(query)
    }
    private suspend fun searchBricks(query: String) {
        withContext(Dispatchers.IO) {
            var loadedBricks: List<Brick> = brickDao.getBricksByName(query)
            if(loadedBricks.isEmpty()){
                val request = BricksRequest(search = query)

                networkService.searchBricks(request).execute().body()?.let {
                    loadedBricks = it.results.map { apiBrick -> apiBrick.toBrick() }
                }
                brickDao.insertAll(loadedBricks)
                searchedBricks.postValue(loadedBricks)
            }
            else{
                searchedBricks.postValue(loadedBricks)
            }
        }
    }

    suspend fun publicSearchBrickSets(query: String){
        searchBrickSets(query)
    }
    private suspend fun searchBrickSets(query: String) {
        withContext(Dispatchers.IO) {
            var loadedSets: List<BrickSet> = setDao.getSetsByName(query)
            if(loadedSets.isEmpty()){
                val request = SearchRequest(search = query)

                networkService.searchSet(request).execute().body()?.let {
                    loadedSets = it.results.map { apiSet -> apiSet.toSet() }
                }
                setDao.insertAll(loadedSets)
                searchedSets.postValue(loadedSets)
            }
            else{
                searchedSets.postValue(loadedSets)
            }
        }
    }

    suspend fun publicGetBricksOfBrickSet(brickSetId: String) {
        getBricksOfBrickSet(brickSetId)
    }

    private suspend fun getBricksOfBrickSet(brickSetId: String) {

    }


    //Métodos del fragmento de favoritos
    suspend fun publicFavoriteBrickSet() {
        getFavoriteBrickSet()
    }
    private suspend fun getFavoriteBrickSet() {
        var loadedSets: List<BrickSet> = setDao.findFavorites()
        favoriteSets.postValue(loadedSets)
    }

    //Métodos del theme y category
    suspend fun publicGetCategories() {
        getCategories()
    }
    private suspend fun getCategories() {
        withContext(Dispatchers.IO) {
            var loadedCategories: List<Category> = categoryDao.getAllCategories()
            if(loadedCategories.isEmpty()){
                val request = CategoriesRequest()

                networkService.getCategories(request).execute().body()?.let {
                    loadedCategories = it.results.map { cat -> cat.toCategory() }
                }
                categoryDao.insertAll(loadedCategories)
                categories.postValue(loadedCategories)
            }
            else{
                categories.postValue(loadedCategories)
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
            var loadedThemes: List<Theme> = themeDao.getAllThemes()
            if(loadedThemes.isEmpty()){
                val request = ThemesRequest(1, 1000)

                networkService.getThemes(request).execute().body()?.let {
                    loadedThemes = it.results.map { theme -> theme.toTheme() }
                }
                themeDao.insertAll(loadedThemes)
                themes.postValue(loadedThemes)
            }
            else{
                themes.postValue(loadedThemes)
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

    suspend fun publicGetInventoryBricks() {
        getInventoryBricks()
    }
    private suspend fun getInventoryBricks() {
        var loadedInventory: List<Brick> = brickDao.getInventoryBricks()
        inventoryBricks.postValue(loadedInventory)
    }

    suspend fun publicGetFilteredInventoryBricks(selectedCategory: Int) {
        getFilteredBricks(selectedCategory)
    }
    private suspend fun getFilteredBricks(selectedCategory: Int) {
        var loadedInventory: List<Brick> = brickDao.getFilteredInventoryBricks(selectedCategory)
        inventoryBricks.postValue(loadedInventory)
    }

    suspend fun publicGetSearchedInventoryBricks(query: String) {
        getSearchedInventoryBricks(query)
    }
    private suspend fun getSearchedInventoryBricks(query: String) {
        var loadedInventory: List<Brick> = brickDao.getSearchedInventoryBricks(query)
        inventoryBricks.postValue(loadedInventory)
    }

    suspend fun publicGetSearchedFilteredInventoryBricks(query: String, selectedCategory: Int) {
        getSearchedFilteredInventoryBricks(query, selectedCategory)
    }
    private suspend fun getSearchedFilteredInventoryBricks(query: String, selectedCategory: Int) {
        var loadedInventory: List<Brick> = brickDao.getSearchedFilteredInventoryBricks(query, selectedCategory)
        inventoryBricks.postValue(loadedInventory)
    }

    suspend fun publicInsertBrick(brick: Brick) {
        insertBrick(brick)
    }

    private suspend fun insertBrick(brick: Brick) {
        brickDao.insert(brick)
    }

    suspend fun publicGetBrick(brickId: String): Brick {
        return getBrick(brickId)
    }
    private suspend fun getBrick(brickId: String): Brick {
        return brickDao.findById(brickId)
    }

    suspend fun publicInsertBrickSet(brickSet: BrickSet) {
        insertBrickSet(brickSet)
    }
    private suspend fun insertBrickSet(brickSet: BrickSet) {
        setDao.insert(brickSet)
    }

    companion object {
        private const val MIN_TIME_FROM_LAST_FETCH_MILLIS: Long = 30000
        @Volatile
        private var INSTANCE: Repository? = null
        fun getInstance(
            brickDao: BrickDao,
            brickSetDao: BrickSetDao,
            categoryDao: CategoryDao,
            themeDao: ThemeDao,
            rbrickrAPI: RebrickableService

        ): Repository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Repository(brickDao, brickSetDao, categoryDao, themeDao, rbrickrAPI).also { INSTANCE = it }
            }
        }
    }
}
