package com.gd05.brickr.ui.home

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

import com.gd05.brickr.R
import com.gd05.brickr.api.RebrickableService
import com.gd05.brickr.data.api.CategoriesRequest
import com.gd05.brickr.data.api.ThemesRequest
import com.gd05.brickr.data.mapper.toBrick
import com.gd05.brickr.data.mapper.toCategory
import com.gd05.brickr.data.mapper.toTheme
import com.gd05.brickr.database.BrickrDatabase
import com.gd05.brickr.databinding.ActivityHomeBinding
import com.gd05.brickr.model.Brick
import com.gd05.brickr.model.BrickSet
import com.gd05.brickr.ui.favorite.FavoriteFragmentDirections
import com.gd05.brickr.ui.search.SearchFragment
import com.gd05.brickr.ui.search.SearchFragmentDirections
import com.gd05.brickr.ui.favorite.OnFavoriteClickListener
import com.gd05.brickr.util.BACKGROUND
import kotlinx.coroutines.launch

/** HomeActivity is a class that define the Activity where we are going to deploy different fragments */
class HomeActivity : AppCompatActivity(), InventoryFragment.OnInventoryClickListener, SearchFragment.OnSearchClickListener, BrickSetPartsFragment.OnBrickSetPartsClickListener,
    OnFavoriteClickListener {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var db: BrickrDatabase

    /** We define the navController val in charge of handle everything related to navigation
     * we assign the nav_host_fragment we define in activity_home and returns as NavHostFragment*/
    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = BrickrDatabase.getInstance(applicationContext)!!
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //carga de datos falsos de prueba
        loadDatabase()
        setUpTheme()
        setUpUI()
        setUpListeners()

    }

    fun setUpTheme(){
        val sp = getSharedPreferences("com.gd05.brickr_preferences", Context.MODE_PRIVATE)

        // Dark mode theme
        if (sp.getBoolean(SettingsFragment.DARK_MODE_KEY, false))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // Text size
        when(sp.getInt(SettingsFragment.TEXT_SIZE_KEY, 1)){
            0 -> {setTheme(R.style.Theme_Brickr_Small)}
            1 -> {setTheme(R.style.Theme_Brickr_Medium)}
            2 -> {setTheme(R.style.Theme_Brickr_Large)}
        }
    }

    fun setUpUI() {
        /**We link the bottonNavigation with the NavController*/
        binding.bottomNavigation.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.FavoriteFragment,
                R.id.SearchFragment,
                R.id.InventoryFragment
            )
        )
        /**We link the toolbar(our instance) to the ActionBar(Class from androidX)-->*/
        setSupportActionBar(binding.toolbar)
        /**We link the ActionBar(from androidX) to the navController(define in this app from androidX)*/
        setupActionBarWithNavController(navController, appBarConfiguration)
        /**The text for the ToolBar will be the label of the botton_bar_nav_graph.xml(Define in strings.xml)*/
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    /**This method is in charge of handle the click on the menu*/
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            // User chooses the "Settings" item. Show the app settings UI.
            val action = SearchFragmentDirections.actionHomeToSettingsFragment()
            navController.navigate(action)
            true
        }

        else -> {
            // The user's action isn't recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    override fun onBrickSetBricksClick(brickId : String){
        BACKGROUND.submit{
            RebrickableService.searchBrickById(brickId).execute().body().let {
                val brick = it?.toBrick()
                val action = BrickSetPartsFragmentDirections.actionBrickSetPartsFragmentToBrickDetailFragment(brick!!)
                lifecycleScope.launch {
                    navController.navigate(action)
                }

            }
        }
        /*val action = BrickSetPartsFragmentDirections.actionBrickSetPartsFragmentToBrickDetailFragment(brick)
        navController.navigate(action)*/
    }

    override fun onInventoryBrickClick(brick: Brick){
        val action = InventoryFragmentDirections.actionInventoryFragmentToBrickDetailFragment(brick)
        navController.navigate(action)
    }

    override fun onSearchBrickClick(brick: Brick){
        val action = SearchFragmentDirections.actionSearchFragmentToBrickDetailFragment(brick)
        navController.navigate(action)
    }

    override fun onSearchSetClick(set: BrickSet){
        val action = SearchFragmentDirections.actionSearchFragmentToBrickDetailSetDetailFragment(set)
        navController.navigate(action)
    }

    private fun loadCategories(){
        BACKGROUND.submit {
            val request = CategoriesRequest()
            RebrickableService.getCategories(request).execute().body().let {
                val loadedCategories = it?.results?.map { category -> category.toCategory() }
                lifecycleScope.launch {
                    loadedCategories?.forEach { category ->
                        db.categoryDao().insertCategory(category)
                    }
                }
            }
        }
    }

    private fun loadThemes(){
        BACKGROUND.submit {
            val request = ThemesRequest()
            RebrickableService.getThemes(request).execute().body().let {
                val loadedThemes = it?.results?.map { theme -> theme.toTheme() }
                lifecycleScope.launch {
                    loadedThemes?.forEach { theme ->
                        db.themeDao().insertTheme(theme)
                    }
                }
            }
        }
    }


    private fun loadDatabase(){
        lifecycleScope.launch {
            loadCategories()
            loadThemes()
        }
    }


    fun setUpListeners() {
        //nothing to do
    }

    override fun onFavoriteClickListener(set: BrickSet) {
        val action = FavoriteFragmentDirections.actionFavoriteFragmentToBrickDetailSetDetailFragment(set)
        navController.navigate(action)
    }

}