package com.gd05.brickr.ui.home

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

import com.gd05.brickr.R
import com.gd05.brickr.database.BrickrDatabase
import com.gd05.brickr.databinding.ActivityHomeBinding
import com.gd05.brickr.model.Brick
import com.gd05.brickr.model.Category
import kotlinx.coroutines.launch

/** HomeActivity is a class that define the Activity where we are going to deploy different fragments */
class HomeActivity : AppCompatActivity(), InventoryFragment.OnInventoryClickListener, SearchFragment.OnSearchClickListener {
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

    //carga de datos falsos de prueba en la base de datos
    private fun loadDatabase(){
        lifecycleScope.launch {
            var cat = Category(9, "Baseplates")
            db.categoryDao().insertCategory(cat)

            cat = Category(11, "Bricks")
            db.categoryDao().insertCategory(cat)

            cat = Category(1, "Plates Special")
            db.categoryDao().insertCategory(cat)

            var brickExample = Brick("4", "prueba1", 9, 2002, 2006, "b", "a", 5)
            db.brickDao().insert(brickExample)
            brickExample = Brick("4545", "prueba2", 11, 2002, 20065, "b", "a", 5)
            db.brickDao().insert(brickExample)
            brickExample = Brick("5", "prueba3", 1, 202, 20061, "b", "a", 3)
            db.brickDao().insert(brickExample)
            brickExample = Brick("8", "AAprueba", 1, 202, 20061, "b", "a", 3)
            db.brickDao().insert(brickExample)
        }
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

    override fun onBrickClick(brick: Brick){
        val action = InventoryFragmentDirections.actionInventoryFragmentToBrickDetailFragment(brick)
        navController.navigate(action)
    }

    override fun onSearchBrickClick(brick: Brick){
        val action = SearchFragmentDirections.actionSearchFragmentToBrickDetailFragment(brick)
        navController.navigate(action)
    }


    fun setUpListeners() {
        //nothing to do
    }

}